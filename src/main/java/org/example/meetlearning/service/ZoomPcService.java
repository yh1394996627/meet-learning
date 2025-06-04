package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class ZoomPcService {

    @Autowired
    private ZoomOAuthService zoomOAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZoomBaseService zoomBaseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private ZoomCallbackService zoomCallbackService;

    @Autowired
    private MeetingLogService meetingLogService;

    @Autowired
    private TeacherSalaryPcService teacherSalaryPcService;


    public Boolean isZoomInstalled(String userCode) {
        try {
            User user = userService.selectByRecordId(userCode);
            if (StringUtils.equals(user.getType(), RoleEnum.TEACHER.name())) {
                Teacher teacher = teacherService.selectByRecordId(userCode);
                if (BooleanUtil.isTrue(teacher.getZoomActivationStatus())) {
                    return true;
                }
                //判断zoomUserID 和accountId是否为空
                if (StringUtils.isNotEmpty(teacher.getZoomUserId()) && StringUtils.isNotEmpty(teacher.getZoomAccountId())) {
                    ZoomAccountSet zoomAccountSet = zoomBaseService.selectByAccountId(teacher.getZoomAccountId());
                    Assert.notNull(zoomAccountSet, "Zoom account not found");
                    zoomOAuthService.getValidAccessToken(zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), zoomAccountSet.getZoomAccountId());
                    JsonObject jsonObject = zoomOAuthService.getConsistentUserId(teacher.getEmail());
                    Teacher newTeacher = new Teacher();
                    newTeacher.setZoomActivationStatus(false);
                    if (jsonObject != null) {
                        newTeacher.setId(teacher.getId());
                        newTeacher.setZoomUserId(jsonObjReplace(jsonObject.get("id").toString()));
                        newTeacher.setZoomAccountId(zoomAccountSet.getZoomAccountId());
                        newTeacher.setZoomActivationStatus(StringUtils.equals("active", jsonObjReplace(jsonObject.get("status").toString())));
                        teacherService.updateEntity(newTeacher);
                    }
                    return newTeacher.getZoomActivationStatus();
                } else {
                    ZoomAccountSet zoomAccountSet = zoomBaseService.selectOneOrderByQty();
                    Assert.notNull(zoomAccountSet, "Zoom account not found");
                    zoomOAuthService.getValidAccessToken(zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), zoomAccountSet.getZoomAccountId());
                    //创建用户绑定用户组，发送群组邮件
                    JsonObject jsonObject = zoomOAuthService.checkUserAndGetActivationLink(teacher.getEmail());
                    Teacher newTeacher = new Teacher();
                    newTeacher.setId(teacher.getId());
                    newTeacher.setZoomUserId(jsonObjReplace(jsonObject.get("id").toString()));
                    newTeacher.setZoomAccountId(zoomAccountSet.getZoomAccountId());
                    //有延时 默认false
                    newTeacher.setZoomActivationStatus(false);
                    teacherService.updateEntity(newTeacher);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            log.error("Failed to verify zoom", ex);
            return false;
        }
    }

    public ResponseEntity<String> handleZoomEvent(String authToken, String payload) {
        try {
            log.info("payload: {}", payload);
            log.info("authToken: {}", authToken);
            JSONObject json = new JSONObject(payload);
            String eventType = json.getString("event");
            // 处理URL验证请求
            if ("endpoint.url_validation".equals(eventType)) {
                // 获取配置信息 激活token
                ZoomAccountSet zoomAccountSet = zoomBaseService.selectByVerificationToken(authToken);
                json.put("secretToken", zoomAccountSet.getSecretToken());
                return handleUrlValidation(json);
            }
            // 处理会议事件
            JSONObject eventData = json.getJSONObject("payload");
            JSONObject objData = eventData.getJSONObject("object");
            String meetingId = objData.getString("id");
            // 根据会议ID查找预约课程并且获取ZOOM配置
            StudentClass studentClass = studentClassService.selectByMeetId(meetingId);
            Assert.notNull(studentClass, "No appointment information obtained");
            Teacher teacher = teacherService.selectByRecordId(studentClass.getTeacherId());
            String account = teacher.getZoomAccountId();
            // 获取配置信息 激活token
            ZoomAccountSet zoomAccountSet = zoomBaseService.selectByAccountId(account);
            zoomOAuthService.getValidAccessToken(zoomAccountSet.getZoomClientId(), zoomAccountSet.getZoomClientSecret(), zoomAccountSet.getZoomAccountId());
            json.put("eventToken", zoomAccountSet.getSecretToken());

            log.info("objData:{}", objData);
            switch (eventType) {
                case "meeting.started":
                    log.info("开始会议处理中。。。");
                    handleMeetingStarted(objData);
                    break;
                case "meeting.ended":
                    log.info("结束会议处理中。。。");
                    handleMeetingEnded(objData);
                    break;
                case "meeting.participant_joined":
                    log.info("加入会议处理中。。。");
                    handleParticipantJoined(objData);
                    break;
                case "meeting.participant_left":
                    log.info("离开会议处理中。。。");
                    handleParticipantLeft(objData);
                    break;
                default:
                    log.warn("Unhandled Zoom event type: {}", eventType);
            }
            return ResponseEntity.ok("Event received");
        } catch (Exception ex) {
            log.error("Failed to handle Zoom event", ex);
            //记录ZOOM会议回调的异常信息
            zoomCallbackService.insertMsg(authToken, payload);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to handle Zoom event");
        }
    }


    public void handleZoomEventEx(List<String> recordIds) {
        List<ZoomCallbackMsg> mags = zoomCallbackService.selectByRecordIds(recordIds);
        for (ZoomCallbackMsg msg : mags) {
            //删除原有异常
            zoomCallbackService.deleteByRecordId(msg.getRecordId());
            //重新发送
            handleZoomEvent(msg.getToken(), msg.getPayload());
        }
    }

    /**
     * 开始会议事件
     */
    private void handleMeetingStarted(JSONObject eventData) {
        String meetingId = eventData.getString("id");
        String startTimeStr = eventData.getString("start_time");
        Date startTime = DateUtil.parse(startTimeStr);
        // 更新课程状态
        StudentClass studentClass = studentClassService.selectByMeetId(meetingId);
        StudentClass updateStudentClass = new StudentClass();
        updateStudentClass.setId(studentClass.getId());
        updateStudentClass.setClassStatus(CourseStatusEnum.PROCESS.getStatus());
        updateStudentClass.setTeacherCourseStatus(CourseStatusEnum.PROCESS.getStatus());
        studentClassService.updateEntity(updateStudentClass);
        //todo 记录会议日志 展示没有需求实现
        meetingLogService.insert(studentClass.getTeacherId(), studentClass.getTeacherName(), meetingId, "Meeting started", startTime);
    }

    /**
     * 结束会议事件
     */
    private void handleMeetingEnded(JSONObject eventData) {
        String meetingId = eventData.getString("id");
        String endTimeStr = eventData.getString("end_time");
        Date endTime = DateUtil.parse(endTimeStr);
        // 更新课程状态
        StudentClass studentClass = studentClassService.selectByMeetId(meetingId);
        StudentClass updateStudentClass = new StudentClass();
        updateStudentClass.setId(studentClass.getId());
        updateStudentClass.setClassStatus(CourseStatusEnum.FINISH.getStatus());
        //如果状态是未开始，则改为缺席
        if (Objects.equals(studentClass.getStudentCourseStatus(), CourseStatusEnum.NOT_STARTED.getStatus())) {
            updateStudentClass.setStudentCourseStatus(CourseStatusEnum.ABSENT.getStatus());
        }
        if (Objects.equals(studentClass.getTeacherCourseStatus(), CourseStatusEnum.NOT_STARTED.getStatus())) {
            updateStudentClass.setTeacherCourseStatus(CourseStatusEnum.ABSENT.getStatus());
            updateStudentClass.setClassStatus(CourseStatusEnum.ABSENT.getStatus());
        }
        studentClassService.updateEntity(updateStudentClass);
        //todo 记录会议日志 展示没有需求实现
        meetingLogService.insert(studentClass.getTeacherId(), studentClass.getTeacherName(), meetingId, "End of the meeting", endTime);
        //更新老师薪资
        teacherSalaryPcService.updateSalary("SYSTEM", "SYSTEM", studentClass.getTeacherId(), new Date());
    }

    /**
     * 用户加入会议事件
     */
    private void handleParticipantJoined(JSONObject eventData) {
        String meetingId = eventData.getString("id");
        String joinTimeStr = eventData.getString("join_time");
        Date joinTime = DateUtil.parse(joinTimeStr);
        String email = eventData.optString("email", null); // 获取邮箱
        StudentClass studentClass = studentClassService.selectByMeetId(meetingId);
        StudentClass updateStudentClass = new StudentClass();
        updateStudentClass.setId(studentClass.getId());
        // 根据邮箱查询用户
        User user = userService.selectByAccountCode(email);
        if (user != null) {
            String recordId = user.getRecordId();
            Date beginTime = DateUtil.parse(studentClass.getCourseTime() + " " + studentClass.getBeginTime());
            CourseStatusEnum courseStatus = beginTime.compareTo(new Date()) > 0 ? CourseStatusEnum.BE_LATE : CourseStatusEnum.PROCESS;
            if (StringUtils.equals(recordId, studentClass.getStudentId())) {
                updateStudentClass.setStudentCourseStatus(courseStatus.getStatus());
                //todo 记录会议日志 展示没有需求实现
                meetingLogService.insert(studentClass.getStudentId(), studentClass.getStudentName(), meetingId, "Student [" + studentClass.getStudentName() + "] joins the meeting", joinTime);
            }
            if (StringUtils.equals(recordId, studentClass.getTeacherId())) {
                updateStudentClass.setTeacherCourseStatus(courseStatus.getStatus());
                //老师迟到处理
                if (courseStatus == CourseStatusEnum.BE_LATE) {
                    //todo 暂时没有迟到的处理逻辑
                }
                meetingLogService.insert(studentClass.getTeacherId(), studentClass.getTeacherName(), meetingId, "Teacher [" + studentClass.getTeacherName() + "] joins the meeting", joinTime);
            }
            studentClassService.updateEntity(updateStudentClass);
        }
    }

    /**
     * 离开会议事件
     */
    private void handleParticipantLeft(JSONObject eventData) {
        String meetingId = eventData.getString("id");
        String leaveTimeStr = eventData.getString("leave_time");
        Date leaveTime = DateUtil.parse(leaveTimeStr);
        String email = eventData.optString("email", null); // 获取邮箱
        StudentClass studentClass = studentClassService.selectByMeetId(meetingId);
        StudentClass updateStudentClass = new StudentClass();
        updateStudentClass.setId(studentClass.getId());
        // 根据邮箱查询用户
        User user = userService.selectByAccountCode(email);
        if (user != null) {
            String recordId = user.getRecordId();
            Date endTime = DateUtil.parse(studentClass.getCourseTime() + " " + studentClass.getEndTime());
            CourseStatusEnum courseStatus = endTime.compareTo(new Date()) > 0 ? CourseStatusEnum.LEAVE_EARLY : CourseStatusEnum.FINISH;
            if (StringUtils.equals(recordId, studentClass.getStudentId())) {
                updateStudentClass.setStudentCourseStatus(courseStatus.getStatus());
                meetingLogService.insert(studentClass.getStudentId(), studentClass.getStudentName(), meetingId, "Student [" + studentClass.getTeacherName() + "] leaves the meeting", leaveTime);
            }
            if (StringUtils.equals(recordId, studentClass.getTeacherId())) {
                updateStudentClass.setTeacherCourseStatus(courseStatus.getStatus());
                //老师早退处理
                if (courseStatus == CourseStatusEnum.LEAVE_EARLY) {
                    //todo 暂时没有处理
                }
                meetingLogService.insert(studentClass.getTeacherId(), studentClass.getTeacherName(), meetingId, "Teacher [" + studentClass.getTeacherName() + "] leaves the meeting", leaveTime);
            }
            studentClassService.updateEntity(updateStudentClass);
        }
    }


    private ResponseEntity<String> handleUrlValidation(JSONObject json) {
        try {
            String plainToken = json.getJSONObject("payload").getString("plainToken");
            String secretToken = json.getString("secretToken");

            // 使用 HMAC-SHA256 加密令牌
            String encryptedToken = encryptToken(plainToken, secretToken);

            // 构建响应 JSON
            JSONObject response = new JSONObject();
            response.put("plainToken", plainToken);
            response.put("encryptedToken", encryptedToken);

            log.info("Successfully validated Zoom webhook URL");
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            log.error("URL validation failed", e);
            return ResponseEntity.badRequest().body("Validation failed");
        }
    }

    // 加密令牌方法
    private String encryptToken(String plainToken, String secretToken) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(
                secretToken.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");
        sha256.init(secretKey);

        byte[] hash = sha256.doFinal(plainToken.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    // 字节数组转十六进制
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public String jsonObjReplace(String json) {
        if (StringUtils.isNotEmpty(json)) {
            return json.replace("\"", "");
        }
        return null;
    }
}

package org.example.meetlearning.service;

import cn.hutool.core.util.BooleanUtil;
import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.ibatis.annotations.Param;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.service.impl.ZoomBaseService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.example.meetlearning.util.SystemUUIDUtil;
import org.example.meetlearning.util.ZoomDetectorUtil;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class ZoomPcService {

    @Value("${zoom.api-url}")
    private String apiUrl;

    @Autowired
    private ZoomOAuthService zoomOAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZoomBaseService zoomBaseService;

    @Autowired
    private TeacherService teacherService;

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







    public String jsonObjReplace(String json) {
        if (StringUtils.isNotEmpty(json)) {
            return json.replace("\"", "");
        }
        return null;
    }
}

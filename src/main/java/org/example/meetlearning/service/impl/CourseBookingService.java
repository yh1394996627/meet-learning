package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.meetlearning.dao.entity.TeacherCourseTime;
import org.example.meetlearning.dao.entity.TeacherScheduleSet;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseBookingService {

    private final TeacherScheduleSetRepository teacherScheduleSetRepo;
    private final TeacherCourseTimeRepository teacherCourseTimeRepo;

    /**
     * 获取可预约时间段
     *
     * @param teacherId  老师ID
     * @param date       查询日期 yyyy-MM-dd
     * @param courseType 课程类型 "单人"或"团队"
     * @return 可用时间段列表
     */
    public List<AvailableTimeSlot> getAvailableSlots(String teacherId, String date, String courseType) {
        // 1. 获取老师当天的日程安排
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<TeacherScheduleSet> schedules = teacherScheduleSetRepo.findByTeacherIdAndWeekNum(
                teacherId, String.valueOf(dayOfWeek.getValue()));

        // 2. 获取老师当天已排课记录
        List<TeacherCourseTime> bookedCourses = teacherCourseTimeRepo.findByTeacherIdAndCourseTime(
                teacherId, date);

        // 3. 计算可用时间段
        return schedules.stream()
                .flatMap(schedule -> calculateAvailableSlots(schedule, bookedCourses, date, courseType).stream())
                .collect(Collectors.toList());
    }

    /**
     * 计算某个日程的可用时间段
     */
    private List<String> calculateAvailableSlots(TeacherScheduleSet schedule,
                                                 List<TeacherCourseTime> bookedCourses,
                                                 LocalDate date,
                                                 String courseType) {
        List<String> resultList = new ArrayList<>();
        // 解析时间段
        LocalTime scheduleBegin = LocalTime.parse(schedule.getBeginTime());
        LocalTime scheduleEnd = LocalTime.parse(schedule.getEndTime());
        // 确定预约时长
        int durationMinutes = "GROUP".equals(courseType) ? 60 : 30;
        // 计算总时长（分钟）
        long totalDuration = Duration.between(scheduleBegin, scheduleEnd).toMinutes();

        // 如果总时长小于预约时长，直接返回空
        if (totalDuration < durationMinutes) {
            return availableSlots;
        }

        // 获取该日程当天已排课的时间段（同课程类型）
        List<TimeSlot> bookedSlots = bookedCourses.stream()
                .filter(c -> c.getCourseType().equals(courseType))
                .map(c -> new TimeSlot(
                        LocalTime.parse(c.getBeginTime()),
                        LocalTime.parse(c.getEndTime())
                ))
                .collect(Collectors.toList());

        // 从开始时间滑动窗口检查可用时间段
        LocalTime currentStart = scheduleBegin;

        while (Duration.between(currentStart, scheduleEnd).toMinutes() >= durationMinutes) {
            LocalTime currentEnd = currentStart.plusMinutes(durationMinutes);
            TimeSlot slot = new TimeSlot(currentStart, currentEnd);

            // 检查是否与已排课时间冲突
            boolean isAvailable = bookedSlots.stream()
                    .noneMatch(booked -> isOverlap(slot, booked));

            if (isAvailable) {
                availableSlots.add(new AvailableTimeSlot(
                        currentStart.toString(),
                        currentEnd.toString(),
                        courseType
                ));
            }

            // 移动窗口
            currentStart = currentStart.plusMinutes(durationMinutes);
        }
        return availableSlots;
    }

    /**
     * 检查两个时间段是否重叠
     */
    private boolean isOverlap(TimeSlot slot1, TimeSlot slot2) {
        return slot1.getStart().isBefore(slot2.getEnd()) &&
                slot1.getEnd().isAfter(slot2.getStart());
    }

    /**
     * 学生预约课程
     *
     * @param teacherId  老师ID
     * @param date       课程日期 yyyy-MM-dd
     * @param beginTime  开始时间 "HH:mm"
     * @param courseType 课程类型 "单人"或"团队"
     * @return 预约结果
     */
    public BookingResult bookCourse(String teacherId, LocalDate date, String beginTime, String courseType) {
        // 验证课程类型
        if (!"单人".equals(courseType) && !"团队".equals(courseType)) {
            throw new IllegalArgumentException("课程类型必须是'单人'或'团队'");
        }

        // 确定时长
        int durationMinutes = "单人".equals(courseType) ? 30 : 60;
        LocalTime begin = LocalTime.parse(beginTime);
        LocalTime end = begin.plusMinutes(durationMinutes);

        // 1. 检查老师这个时间段是否在日程中
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isInSchedule = teacherScheduleSetRepo.existsByTeacherIdAndWeekNumAndTimeRange(
                teacherId,
                String.valueOf(dayOfWeek.getValue()),
                beginTime,
                end.toString()
        );

        if (!isInSchedule) {
            return BookingResult.failed("老师在该时间段没有安排");
        }

        // 2. 检查是否已被预约（单人课需要检查）
        if ("单人".equals(courseType)) {
            boolean isBooked = teacherCourseTimeRepo.existsByTeacherIdAndCourseTimeAndTimeRangeAndCourseType(
                    teacherId,
                    date,
                    beginTime,
                    end.toString(),
                    courseType
            );

            if (isBooked) {
                return BookingResult.failed("该时间段已被预约");
            }
        }

        // 3. 创建预约记录
        TeacherCourseTime booking = new TeacherCourseTime();
        booking.setTeacherId(teacherId);
        booking.setCourseTime(date);
        booking.setBeginTime(beginTime);
        booking.setEndTime(end.toString());
        booking.setCourseType(courseType);
        booking.setRecordId(UUID.randomUUID().toString());

        teacherCourseTimeRepo.save(booking);

        return BookingResult.success(booking);
    }
}

// 辅助类
@Data
@AllArgsConstructor
class TimeSlot {
    private LocalTime start;
    private LocalTime end;
}

@Data
class BookingResult {
    private boolean success;
    private String message;
    private TeacherCourseTime booking;

    public static BookingResult success(TeacherCourseTime booking) {
        BookingResult result = new BookingResult();
        result.setSuccess(true);
        result.setMessage("预约成功");
        result.setBooking(booking);
        return result;
    }

    public static BookingResult failed(String message) {
        BookingResult result = new BookingResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
package org.example.meetlearning.util;

import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AvailableTimeCalculatorUtil {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 获取可上课时间段（排除已预约时间）
     * @param availableTimeStrings 可用时间段集合（格式："HH:mm-HH:mm"）
     * @param bookedTimeStrings 已预约时间段集合（格式："HH:mm-HH:mm"）
     * @return 可上课时间段列表（格式："HH:mm-HH:mm"）
     */
    public static List<String> getAvailableTimeSlots(List<String> availableTimeStrings,
                                                     List<String> bookedTimeStrings) {
        // 转换输入为TimeSlot对象
        List<TimeSlot> availableSlots = parseTimeSlots(availableTimeStrings);
        List<TimeSlot> bookedSlots = parseTimeSlots(bookedTimeStrings);

        List<TimeSlot> freeSlots = new ArrayList<>();

        for (TimeSlot available : availableSlots) {
            boolean isBooked = false;
            for (TimeSlot booked : bookedSlots) {
                if (available.overlapsWith(booked)) {
                    isBooked = true;
                    break;
                }
            }
            if (!isBooked) {
                freeSlots.add(available);
            }
        }

        // 转换回字符串格式
        return formatTimeSlots(freeSlots);
    }

    // 将字符串格式的时间段转换为TimeSlot对象列表
    private static List<TimeSlot> parseTimeSlots(List<String> timeStrings) {
        List<TimeSlot> slots = new ArrayList<>();
        if (timeStrings == null) return slots;

        for (String timeStr : timeStrings) {
            String[] parts = timeStr.split("-");
            if (parts.length != 2) continue;

            try {
                LocalTime start = LocalTime.parse(parts[0], TIME_FORMATTER);
                LocalTime end = LocalTime.parse(parts[1], TIME_FORMATTER);
                slots.add(new TimeSlot(start, end));
            } catch (Exception e) {
                // 忽略格式错误的时间段
                System.err.println("无效的时间段格式: " + timeStr);
            }
        }
        return slots;
    }

    // 将TimeSlot对象列表转换为字符串格式
    private static List<String> formatTimeSlots(List<TimeSlot> slots) {
        List<String> result = new ArrayList<>();
        for (TimeSlot slot : slots) {
            result.add(slot.toString());
        }
        return result;
    }


    /**
     * 计算多个日期时间段的交集
     * @param dateTimeSlotsMap 日期到时间段列表的映射
     * @return 所有日期共有的时间段列表
     */
    public static List<String> findCommonTimeSlots(Map<String, List<String>> dateTimeSlotsMap) {
        if (dateTimeSlotsMap == null || dateTimeSlotsMap.isEmpty()) {
            return Collections.emptyList();
        }

        // 首先将所有日期的时间段转换为TimeSlot集合
        Map<String, List<TimeSlot>> dateToSlots = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : dateTimeSlotsMap.entrySet()) {
            dateToSlots.put(entry.getKey(), parseTimeSlots(entry.getValue()));
        }

        // 获取所有日期的集合
        Set<String> dates = dateTimeSlotsMap.keySet();
        if (dates.isEmpty()) {
            return Collections.emptyList();
        }

        // 取第一个日期的时间段作为初始交集
        Set<TimeSlot> intersection = new HashSet<>(dateToSlots.get(dates.iterator().next()));

        // 与其他日期的时间段求交集
        for (String date : dates) {
            intersection.retainAll(dateToSlots.get(date));
            if (intersection.isEmpty()) {
                break; // 如果交集已为空，提前退出
            }
        }
        // 将结果排序并转换为字符串格式
        return intersection.stream()
                .sorted(Comparator.comparing(TimeSlot::getStart))
                .map(TimeSlot::toString).toList();
    }




    // 时间段内部类
    @Data
    static class TimeSlot {
        private final LocalTime start;
        private final LocalTime end;

        public TimeSlot(LocalTime start, LocalTime end) {
            if (start == null || end == null) {
                throw new IllegalArgumentException("开始时间和结束时间不能为null");
            }
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("开始时间不能晚于结束时间");
            }
            this.start = start;
            this.end = end;
        }

        // 判断两个时间段是否有重叠
        public boolean overlapsWith(TimeSlot other) {
            return !this.end.isBefore(other.start) && !this.start.isAfter(other.end) ||
                    !other.end.isBefore(this.start) && !other.start.isAfter(this.end);
        }

        @Override
        public String toString() {
            return start.format(TIME_FORMATTER) + "-" + end.format(TIME_FORMATTER);
        }
    }

    // 测试示例
    public static void main(String[] args) {
        List<String> availableTimes = List.of(
                "09:30-10:00", "11:30-12:00", "12:30-13:00", "13:00-13:30"
        );
        List<String> bookedTimes = List.of(
                "09:30-10:00", "12:30-13:30"
        );
        List<String> freeTimes = getAvailableTimeSlots(availableTimes, bookedTimes);
        System.out.println("可上课时间段:");
        freeTimes.forEach(System.out::println);
    }
}
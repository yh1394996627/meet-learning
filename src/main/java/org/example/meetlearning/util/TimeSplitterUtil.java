package org.example.meetlearning.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSplitterUtil {

    /**
     * 将时间段按半小时拆分
     * @param startTime 开始时间 (格式: HH:mm)
     * @param endTime 结束时间 (格式: HH:mm)
     * @return 拆分后的时间段列表
     */
    public static List<String> splitByHalfHour(String startTime, String endTime , int addTime) {
        List<String> timeSlots = new ArrayList<>();

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        // 如果结束时间小于开始时间，认为是跨天的情况
        if (end.isBefore(start)) {
            end = end.plusHours(24);
        }

        LocalTime current = start;

        while (current.isBefore(end)) {
            LocalTime next = current.plusMinutes(addTime);

            // 如果下一个时间点超过了结束时间，则使用结束时间
            if (next.isAfter(end)) {
                next = end;
            }
            // 格式化时间为HH:mm
            String slot = formatTime(current) + "-" + formatTime(next);
            timeSlots.add(slot);

            current = next;
        }
        return timeSlots;
    }

    /**
     * 格式化时间，如果有跨天：处理跨天的情况
     */
    private static String formatTime(LocalTime time) {
        // 如果时间大于等于24小时，减去24小时
        if (time.getHour() >= 24) {
            return time.minusHours(24).toString();
        }
        return time.toString();
    }

    public static void main(String[] args) {
        // 测试示例
        List<String> slots = splitByHalfHour("09:11", "20:12",60);
        System.out.println("10:30 - 20:30 拆分结果:");
        slots.forEach(System.out::println);

        System.out.println("\n跨天测试 22:00 - 02:00 拆分结果:");
        splitByHalfHour("22:00", "02:00",60).forEach(System.out::println);
    }
}
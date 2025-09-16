package org.example.meetlearning.util;

import org.codehaus.plexus.util.StringUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSplitterUtil {

    /**
     * 将时间段按半小时拆分
     *
     * @param startTime 开始时间 (格式: HH:mm)
     * @param endTime   结束时间 (格式: HH:mm)
     * @return 拆分后的时间段列表
     */
//    public static List<String> splitByHalfHour(String startTime, String endTime, int addTime) {
//        List<String> timeSlots = new ArrayList<>();
//        while (startTime.compareTo(endTime) <= 0) {
//            String oldStartTime = startTime;
//            if (addTime == 30) {
//                String[] arr = startTime.split(":");
//                if (Integer.parseInt(arr[1]) == 30) {
//                    startTime = (Integer.parseInt(arr[0]) + 1) + ":00";
//                } else {
//                    startTime = arr[0] + ":30";
//                }
//            } else if (addTime == 60) {
//                String[] arr = startTime.split(":");
//                startTime = (Integer.parseInt(arr[0]) + 1) + ":" + arr[1];
//            }
//            if (startTime.compareTo(endTime) <= 0) {
//                timeSlots.add(oldStartTime + "-" + startTime);
//            }
//        }
//        return timeSlots;
//    }

    public static List<String> splitByHalfHour(String startTime, String endTime, int addTime) {
        List<String> timeSlots = new ArrayList<>();
        while (startTime.compareTo(endTime) <= 0) {
            String oldStartTime = startTime;
            if (addTime == 30) {
                String[] arr = startTime.split(":");
                int hour = Integer.parseInt(arr[0]);
                int minute = Integer.parseInt(arr[1]);
                if (minute == 30) {
                    hour += 1;
                    startTime = String.format("%02d", hour) + ":00";
                } else {
                    startTime = String.format("%02d", hour) + ":30";
                }
            } else if (addTime == 60) {
                String[] arr = startTime.split(":");
                int hour = Integer.parseInt(arr[0]);
                hour += 1;
                startTime = String.format("%02d", hour) + ":" + arr[1];
            }
            if (startTime.compareTo(endTime) <= 0) {
                timeSlots.add(oldStartTime + "-" + startTime);
            }
        }
        return timeSlots;
    }

    public static void main(String[] args) {
        // 案例1: 30分钟步长 + 24:00
        System.out.println(splitByHalfHour("17:00", "24:00", 30));
        // 输出: [23:30-24:00]
        System.out.println("--------------");
        // 案例2: 60分钟步长 + 24:00 (23:30开始)
        System.out.println(splitByHalfHour("17:30", "24:00", 60));
        // 输出: [] (无输出，符合预期)
    }
}
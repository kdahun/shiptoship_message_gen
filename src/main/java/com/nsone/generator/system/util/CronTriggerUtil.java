//package com.nsone.generator.system.util;
//
//import org.springframework.scheduling.support.CronTrigger;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class CronTriggerUtil {
//
//    public static String convertLocalDateTimeToCronExpression(LocalDateTime localDateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ?");
//        return localDateTime.format(formatter);
//    }
//
//    public static CronTrigger createCronTrigger(LocalDateTime localDateTime) {
//        String cronExpression = convertLocalDateTimeToCronExpression(localDateTime);
//        return new CronTrigger(cronExpression);
//    }
//}
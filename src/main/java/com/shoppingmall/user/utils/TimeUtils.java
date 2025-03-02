package com.shoppingmall.user.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    /**
     * 게시글 작성일로부터 경과된 시간을 사용자 친화적인 문자열로 변환
     * @param createdAt 게시글 작성일
     * @return "오늘", "1일 전", "2일 전" 등의 문자열
     */
    public static String getTimeAgo(LocalDate createdAt) {
        LocalDate now = LocalDate.now();

        // 날짜 차이 계산
        long daysBetween = ChronoUnit.DAYS.between(createdAt, now);

        // 조건에 따른 표시 방식 결정
        if (daysBetween == 0) {
            return "오늘";
        } else if (daysBetween == 1) {
            return "어제";
        } else if (daysBetween < 7) {
            return daysBetween + "일 전";
        } else if (daysBetween < 30) {
            long weeks = daysBetween / 7;
            return weeks + "주 전";
        } else if (daysBetween < 365) {
            long months = daysBetween / 30;
            return months + "개월 전";
        } else {
            long years = daysBetween / 365;
            return years + "년 전";
        }
    }
}

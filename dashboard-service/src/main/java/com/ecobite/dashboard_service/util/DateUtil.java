package com.ecobite.dashboard_service.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    private DateUtil() {
    }

    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Get current date and time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Format LocalDate
     */
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return date.format(formatter);
    }

    /**
     * Format LocalDateTime
     */
    public static String formatDateTime(
            LocalDateTime dateTime
    ) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss"
                );

        return dateTime.format(formatter);
    }

    /**
     * Parse String to LocalDate
     */
    public static LocalDate parseDate(
            String date
    ) {

        return LocalDate.parse(date);
    }

    /**
     * Calculate days between two dates
     */
    public static long daysBetween(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return ChronoUnit.DAYS.between(
                startDate,
                endDate
        );
    }

    /**
     * Check whether expiry date is near
     */
    public static boolean isExpiringSoon(
            LocalDate expiryDate,
            int days
    ) {

        LocalDate today = LocalDate.now();

        return expiryDate.isAfter(today)
                && expiryDate.isBefore(
                today.plusDays(days)
        );
    }

    /**
     * Check whether batch expired
     */
    public static boolean isExpired(
            LocalDate expiryDate
    ) {

        return expiryDate.isBefore(
                LocalDate.now()
        );
    }

    /**
     * Get current month
     */
    public static String getCurrentMonth() {

        return YearMonth.now().toString();
    }

    /**
     * Get current year
     */
    public static int getCurrentYear() {

        return LocalDate.now().getYear();
    }

    /**
     * Add days to date
     */
    public static LocalDate addDays(
            LocalDate date,
            int days
    ) {

        return date.plusDays(days);
    }

    /**
     * Subtract days from date
     */
    public static LocalDate subtractDays(
            LocalDate date,
            int days
    ) {

        return date.minusDays(days);
    }
}

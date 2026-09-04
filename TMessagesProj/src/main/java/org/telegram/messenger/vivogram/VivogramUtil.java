package org.telegram.messenger.vivogram;

public class VivogramUtil {

    public static String getApproximateRegistrationDate(long userId) {
        if (userId <= 0) {
            return "Unknown";
        }
        if (userId < 5_000_000L) {
            return "~ 2013";
        } else if (userId < 30_000_000L) {
            return "~ 2014";
        } else if (userId < 90_000_000L) {
            return "~ 2015";
        } else if (userId < 200_000_000L) {
            return "~ 2016";
        } else if (userId < 400_000_000L) {
            return "~ 2017";
        } else if (userId < 700_000_000L) {
            return "~ 2018";
        } else if (userId < 1_000_000_000L) {
            return "~ 2019";
        } else if (userId < 1_400_000_000L) {
            return "~ 2020";
        } else if (userId < 2_000_000_000L) {
            return "~ 2021";
        } else if (userId < 5_000_000_000L) {
            return "~ 2022";
        } else if (userId < 6_500_000_000L) {
            return "~ 2023";
        } else if (userId < 7_500_000_000L) {
            return "~ 2024";
        } else if (userId < 8_500_000_000L) {
            return "~ 2025";
        } else {
            return "~ 2026";
        }
    }
}

package com.virinchi.utils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class OTP {
    private final int value;
    private final LocalDateTime creationTime;
    private static final long EXPIRATION_MINUTES = 5;

    public OTP(int value) {
        this.value = value;
        this.creationTime = LocalDateTime.now();
    }

    public Integer getOTP() {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.MINUTES.between(creationTime, now) <= EXPIRATION_MINUTES) {
            return value;
        } else {
            return null; // Or throw an exception, depending on your needs
        }
    }
}
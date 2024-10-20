package com.project.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class TokenGenerator {
    public static String generateToken() {
        return RandomStringUtils.random(128, 0, 0, true, true, null, new SecureRandom());
    }

}
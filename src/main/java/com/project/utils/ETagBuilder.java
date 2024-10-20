package com.project.utils;


import com.project.config.KeyGenerator;
import org.springframework.util.DigestUtils;

public class ETagBuilder {

    public static String buildETag(String... values) {
        StringBuilder eTag = new StringBuilder();
        for (String value : values) {
            eTag.append(value);
        }
        eTag.append(KeyGenerator.getEtagSecretKey());
        return DigestUtils.md5DigestAsHex(eTag.toString().getBytes());
    }

    public static boolean isETagValid(String eTag, String... values) {
        return eTag.equals(buildETag(values));
    }

}
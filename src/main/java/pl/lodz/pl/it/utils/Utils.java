package pl.lodz.pl.it.utils;

import java.time.LocalDateTime;

public class Utils {

    public static LocalDateTime calculateExpirationDate(int expirationHours) {
        return LocalDateTime.now().plusHours(expirationHours);
    }



}
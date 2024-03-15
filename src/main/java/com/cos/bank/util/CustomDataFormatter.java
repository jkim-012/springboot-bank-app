package com.cos.bank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDataFormatter {

    public static String of(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

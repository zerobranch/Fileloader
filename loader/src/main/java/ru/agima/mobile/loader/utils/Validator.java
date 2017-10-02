package ru.agima.mobile.loader.utils;

public class Validator {

    public static <T> T getNonNull(T o, String throwMessage) {
        if (o == null)
            throw new NullPointerException(throwMessage);
        return o;
    }

    public static String getNonEmptyValue(String s, String throwMessage) {
        getNonNull(s, throwMessage);
        if (s.isEmpty())
            throw new IllegalArgumentException(throwMessage);
        return s;
    }
}

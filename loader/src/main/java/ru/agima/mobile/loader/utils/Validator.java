package ru.agima.mobile.loader.utils;

import java.util.List;

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

    public static int getNotNegative(int val, String throwMessage) {
        if (val < 0)
            throw new IllegalArgumentException(throwMessage);
        return val;
    }

    public static List<?> getNonEmptyValue(List<?> list, String throwMessage) {
        getNonNull(list, throwMessage);
        if (list.isEmpty())
            throw new IllegalArgumentException(throwMessage);
        return list;
    }
}

/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author: EwenQin
 * since : 16/9/1 下午3:29.
 */
public final class DataProvider {

    private static final String TAG = "DataProvider";

    private static final char[] CHAR_POOL = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final Random mRandom = new Random();


    public static List<String> getSimpleData(int limit) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            strings.add(getSimpleText());
        }
        return strings;
    }

    public static String getSimpleText() {
        int count = mRandom.nextInt(5) + 4;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(CHAR_POOL[mRandom.nextInt(CHAR_POOL.length)]).toUpperCase());
        for (int i = 0; i < count; i++) {
            sb.append(CHAR_POOL[mRandom.nextInt(CHAR_POOL.length)]);
        }
        return sb.toString();
    }
}

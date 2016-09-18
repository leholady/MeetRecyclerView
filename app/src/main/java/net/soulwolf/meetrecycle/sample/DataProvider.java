/*
 * Copyright 2015 Soulwolf Ching
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

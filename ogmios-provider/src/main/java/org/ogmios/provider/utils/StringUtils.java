package org.ogmios.provider.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

    public static List<String> split(String s) {
        if (s == null || s.trim().isEmpty()) {
            return new ArrayList<>();
        } else {
            return Stream.of(s.split(Constant.COMMA))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}

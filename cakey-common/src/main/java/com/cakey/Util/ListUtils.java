package com.cakey.Util;

import java.util.List;
import java.util.function.Consumer;

public class ListUtils {
    public static <T> List<T> setLastDataAndTrimList(final List<T> list,
                                                     final int size,
                                                     final Consumer<T> lastDataSetter) {
        if (list.size() > size) {
            return list.subList(0, size); ///limit 크기만큼 잘라서 반환
        } else if (!list.isEmpty()) {
            lastDataSetter.accept(list.get(list.size() - 1)); /// 마지막 데이터 여부 설정
        }
        return list;
    }}

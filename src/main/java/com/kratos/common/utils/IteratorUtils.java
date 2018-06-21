package com.kratos.common.utils;

import org.springframework.util.Assert;

import java.util.function.BiConsumer;

/**
 * 迭代工具
 */
public class IteratorUtils {
    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        Assert.notNull(elements, "elements can not be null");
        Assert.notNull(action, "action can not be null");

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}

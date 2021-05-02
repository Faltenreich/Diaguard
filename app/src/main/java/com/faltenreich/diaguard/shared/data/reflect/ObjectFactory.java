package com.faltenreich.diaguard.shared.data.reflect;

public class ObjectFactory {

    public static <T> T createFromClass(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Failed to instantiate object of class " + clazz.getSimpleName());
        }
    }
}

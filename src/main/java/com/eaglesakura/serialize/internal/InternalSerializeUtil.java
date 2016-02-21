package com.eaglesakura.serialize.internal;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializeException;
import com.eaglesakura.serialize.SerializeIdConflictException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class InternalSerializeUtil {
    /**
     * ListがImplされている場合はtrueを返却する
     */
    public static boolean isListInterface(Class<?> clazz) {
        return clazz.asSubclass(List.class) != null;
    }

    /**
     * Listがimplされている場合はtrueを返却する
     */
    public static boolean isListInterface(Field field) {
        return isListInterface(field.getType());
    }

    public static Map<Short, SerializeTargetField> listSerializeFields(Object obj) throws SerializeException {
        Map<Short, SerializeTargetField> result = new HashMap<>();

        for (Field field : obj.getClass().getFields()) {
            Serialize serialize = field.getAnnotation(Serialize.class);
            if (serialize != null) {
                if (result.containsKey(serialize.id())) {
                    throw new SerializeIdConflictException();
                }

                SerializeTargetField target = new SerializeTargetField(obj, field, serialize);
                result.put(serialize.id(), target);
            }
        }

        return result;
    }
}

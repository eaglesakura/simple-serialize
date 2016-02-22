package com.eaglesakura.serialize.internal;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.serialize.error.SerializeIdConflictException;
import com.eaglesakura.util.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class InternalSerializeUtil {

    public static Map<Short, SerializeTargetField> listSerializeFields(Object obj) throws SerializeException {
        Map<Short, SerializeTargetField> result = new HashMap<>();

        if (obj != null) {
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
        }
        return result;
    }
}

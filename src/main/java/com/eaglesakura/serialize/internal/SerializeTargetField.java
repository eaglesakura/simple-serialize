package com.eaglesakura.serialize.internal;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.util.LogUtil;

import java.lang.reflect.Field;

/**
 * シリアライズ対象のFieldを管理する
 */
public class SerializeTargetField {
    public final Object value;
    public final Field field;
    public final Object object;
    public final Class<?> type;
    public final short id;

    public SerializeTargetField(Object object, Field field, Serialize serialize) {
        this.field = field;
        this.object = object;
        this.type = field.getType();
        this.id = serialize.id();
        this.value = getValue(object, field);
    }

    private static Object getValue(Object obj, Field field) {
        try {
            Class clazz = field.getType();
            if (clazz.isPrimitive()) {
                if (clazz.equals(byte.class)) {
                    return Byte.valueOf(field.getByte(obj));
                } else if (clazz.equals(short.class)) {
                    return Short.valueOf(field.getShort(obj));
                } else if (clazz.equals(int.class)) {
                    return Integer.valueOf(field.getInt(obj));
                } else if (clazz.equals(long.class)) {
                    return Long.valueOf(field.getLong(obj));
                } else if (clazz.equals(boolean.class)) {
                    return Boolean.valueOf(field.getBoolean(obj));
                } else {
                    throw new IllegalArgumentException(String.format("Fail Target(%s)", clazz.getName()));
                }
            } else {
                return field.get(obj);
            }
        } catch (Exception e) {
            LogUtil.log(e);
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializeTargetField that = (SerializeTargetField) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return !(object != null ? !object.equals(that.object) : that.object != null);

    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}

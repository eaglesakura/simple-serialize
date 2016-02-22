package com.eaglesakura.serialize.internal;

import com.eaglesakura.io.DataInputStream;
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

    public Object read(ObjectHeader header, DataInputStream stream) throws Exception {
        if (header.isArray()) {
            throw new IllegalArgumentException();
        }

        if (header.isNull()) {
            return null;
        }

        if (type.equals(byte.class) || type.equals(Byte.class)) {
            return stream.readS8();
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return stream.readS16();
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return stream.readS32();
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return stream.readS64();
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return stream.readBoolean();
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return stream.readFloat();
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return stream.readDouble();
        } else if (type.equals(String.class)) {
            return new String(stream.readBuffer(header.size), ObjectHeader.STRING_CHARSET);
        } else if (type.equals(byte[].class)) {
            return stream.readBuffer(header.size);
        } else {
            throw new IllegalStateException();
        }
    }

    public void set(Object obj) throws Exception {
        if (type.isPrimitive()) {
            if (obj instanceof Byte) {
                field.setByte(object, (Byte) obj);
            } else if (obj instanceof Short) {
                field.setShort(object, (Short) obj);
            } else if (obj instanceof Integer) {
                field.setInt(object, (Integer) obj);
            } else if (obj instanceof Long) {
                field.setLong(object, (Long) obj);
            } else if (obj instanceof Float) {
                field.setFloat(object, (Float) obj);
            } else if (obj instanceof Double) {
                field.setDouble(object, (Double) obj);
            } else if (obj instanceof Boolean) {
                field.setBoolean(object, (Boolean) obj);
            }
        } else {
            field.set(object, obj);
        }
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
                } else if (clazz.equals(float.class)) {
                    return Float.valueOf(field.getFloat(obj));
                } else if (clazz.equals(double.class)) {
                    return Double.valueOf(field.getDouble(obj));
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

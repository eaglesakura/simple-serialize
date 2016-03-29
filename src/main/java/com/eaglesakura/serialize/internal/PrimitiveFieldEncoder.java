package com.eaglesakura.serialize.internal;

import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.FieldEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class PrimitiveFieldEncoder implements FieldEncoder {

    static Set<Class> gSupportClasses = new HashSet<>();

    static {
        gSupportClasses.add(byte.class);
        gSupportClasses.add(short.class);
        gSupportClasses.add(int.class);
        gSupportClasses.add(long.class);
        gSupportClasses.add(float.class);
        gSupportClasses.add(double.class);
        gSupportClasses.add(boolean.class);
        gSupportClasses.add(Byte.class);
        gSupportClasses.add(Short.class);
        gSupportClasses.add(Integer.class);
        gSupportClasses.add(Long.class);
        gSupportClasses.add(Float.class);
        gSupportClasses.add(Double.class);
        gSupportClasses.add(Boolean.class);
        gSupportClasses.add(String.class);
        gSupportClasses.add(byte[].class);
    }

    public static boolean isSupport(Class<?> clazz) {
        if (clazz.isEnum()) {
            return true;
        }
        return gSupportClasses.contains(clazz);
    }

    @Override
    public int getObjectSize(SerializeTargetField field) {
        Class<?> type = field.type;
        if (type.equals(String.class)) {
            try {
                return ((String) field.value).getBytes(ObjectHeader.STRING_CHARSET).length;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals(byte[].class)) {
            try {
                return ((byte[]) field.value).length;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isSupport(type)) {
            // その他のサポート型であれば最大8byte
            return 8;
        }
        throw new IllegalStateException();
    }

    @Override
    public void encode(SerializeTargetField field, DataOutputStream stream) {
        try {
            Class<?> typeClass = field.value.getClass();
            if (typeClass.equals(Byte.class)) {
                stream.writeS8((Byte) field.value);
            } else if (typeClass.equals(Short.class)) {
                stream.writeS16((Short) field.value);
            } else if (typeClass.equals(Integer.class)) {
                stream.writeS32((Integer) field.value);
            } else if (typeClass.equals(Long.class)) {
                stream.writeS64((Long) field.value);
            } else if (typeClass.equals(Float.class)) {
                stream.writeFloat((Float) field.value);
            } else if (typeClass.equals(Double.class)) {
                stream.writeDouble((Double) field.value);
            } else if (typeClass.equals(String.class)) {
                byte[] buffer = ((String) field.value).getBytes(ObjectHeader.STRING_CHARSET);
                stream.writeBuffer(buffer, 0, buffer.length);
            } else if (typeClass.equals(byte[].class)) {
                byte[] buffer = ((byte[]) field.value);
                stream.writeBuffer(buffer, 0, buffer.length);
            } else if (typeClass.equals(Boolean.class)) {
                stream.writeBoolean((boolean) field.value);
            } else if (typeClass.isEnum()) {
                short order = (short) ((Enum<?>) field.value).ordinal();
                stream.writeS16(order);
            } else {
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

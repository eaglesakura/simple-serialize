package com.eaglesakura.serialize.internal;

import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.FieldEncoder;
import com.eaglesakura.serialize.ObjectHeader;
import com.eaglesakura.util.LogUtil;

import java.lang.reflect.Field;

/**
 *
 */
public class PrimitiveFieldEncoder implements FieldEncoder {

    @Override
    public int getObjectSize(Object current, Field field) {
        Class<?> typeClass = field.getType();
        if (typeClass.isPrimitive()) {
            // 最大8byte value
            return 8;
        } else if (typeClass.equals(String.class)) {
            try {
                return ((String) field.get(current)).getBytes(ObjectHeader.STRING_CHARSET).length;
            } catch (Exception e) {
                LogUtil.log(e);
            }
        } else if (typeClass.equals(byte[].class)) {
            try {
                return ((byte[]) field.get(current)).length;
            } catch (Exception e) {
                LogUtil.log(e);
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public void encode(Object root, Object current, ObjectHeader header, Field field, DataOutputStream stream) {
        Class<?> typeClass = field.getType();
        try {

            if (typeClass.equals(byte.class)) {
                stream.writeS8(field.getByte(current));
            } else if (typeClass.equals(short.class)) {
                stream.writeS16(field.getShort(current));
            } else if (typeClass.equals(int.class)) {
                stream.writeS32(field.getInt(current));
            } else if (typeClass.equals(long.class)) {
                stream.writeS64(field.getLong(current));
            } else if (typeClass.equals(float.class)) {
                stream.writeFloat(field.getFloat(current));
            } else if (typeClass.equals(double.class)) {
                stream.writeDouble(field.getDouble(current));
            } else if (typeClass.equals(Byte.class)) {
                stream.writeS8((Byte) field.get(current));
            } else if (typeClass.equals(Short.class)) {
                stream.writeS16((Short) field.get(current));
            } else if (typeClass.equals(Integer.class)) {
                stream.writeS32((Integer) field.get(current));
            } else if (typeClass.equals(Long.class)) {
                stream.writeS64((Long) field.get(current));
            } else if (typeClass.equals(Float.class)) {
                stream.writeFloat((Float) field.get(current));
            } else if (typeClass.equals(Double.class)) {
                stream.writeDouble((Double) field.get(current));
            } else if (typeClass.equals(String.class)) {
                byte[] buffer = ((String) field.get(current)).getBytes(ObjectHeader.STRING_CHARSET);
                stream.writeBuffer(buffer, 0, buffer.length);
            } else if (typeClass.equals(byte[].class)) {
                byte[] buffer = ((byte[]) field.get(current));
                stream.writeBuffer(buffer, 0, buffer.length);
            } else {
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalStateException();
        }
    }
}

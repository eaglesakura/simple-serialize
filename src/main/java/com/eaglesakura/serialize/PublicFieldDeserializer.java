package com.eaglesakura.serialize;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.serialize.error.CreateObjectFailedException;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.ObjectHeader;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;
import com.eaglesakura.util.ReflectionUtil;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * エンコードされたオブジェクトをデシリアライズする
 */
public class PublicFieldDeserializer {
    public PublicFieldDeserializer() {
    }

    public <T> T deserialize(Class<T> clazz, byte[] buffer) throws SerializeException {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(buffer), false);

        try {
            SerializeHeader header = SerializeHeader.read(stream);
            LogUtil.log("Magic(%x) Version(%d)", header.magic, header.version);

            return deserializeObject(null, clazz, stream);
        } catch (SerializeException e) {
            throw e;
        } catch (Exception e) {
            LogUtil.log(e);
        }
        return null;
    }

    private <T> T deserializeEnum(ObjectHeader header, Class<T> clazz, DataInputStream stream) throws SerializeException {
        try {
            int index = stream.readU16();
            Method valuesMethod = clazz.getMethod("values");
            T[] values = (T[]) valuesMethod.invoke(clazz);
            return values[index];
        } catch (Exception e) {
            LogUtil.log(e);
            throw new CreateObjectFailedException();
        }
    }

    private <T> T deserializeObject(ObjectHeader header, Class<T> clazz, DataInputStream stream) throws Exception {
        // Objectを読み込む
        if (header == null) {
            header = ObjectHeader.read(stream);
        }
        if (header.isNull()) {
            // nullオブジェクトなので何もしない
            return null;
        }

        if (clazz.equals(String.class)) {
            // StringはPrimitiveと同等に扱う
            return (T) (new String(stream.readBuffer(header.size), ObjectHeader.STRING_CHARSET));
        } else if (clazz.isEnum()) {
            return deserializeEnum(header, clazz, stream);
        }

        T instance = newInstance(clazz);
        if (instance == null) {
            throw new CreateObjectFailedException("Failed :: " + clazz.getName());
        }

        Map<Short, SerializeTargetField> fields = InternalSerializeUtil.listSerializeFields(instance);

        for (int i = 0; i < header.size; ++i) {
            ObjectHeader valueHeader = ObjectHeader.read(stream);
            SerializeTargetField field = fields.get(valueHeader.id);
            if (field != null) {
                // 書き込み対象がある
                if (valueHeader.isNull()) {
                    // nullを書き込む
                    field.set(null);
                    continue;
                } else if (valueHeader.isArray()) {
                    // 配列を読み込む
                    List array = new ArrayList();
                    for (int k = 0; k < valueHeader.size; ++k) {
                        Object vInstance = deserializeObject(null, ReflectionUtil.getListGenericClass(field.field), stream);
                        array.add(vInstance);
                    }
                    field.set(array);
                } else if (valueHeader.isObject()) {
                    // 再帰的に生成させる
                    Object fInstance = deserializeObject(valueHeader, field.type, stream);
                    field.set(fInstance);
                } else if (field.type.isEnum()) {
                    // enumは独自に扱う
                    Object enumValue = deserializeEnum(header, field.type, stream);
                    field.set(enumValue);
                } else {
                    // プリミティブとして処理
                    Object newValue = field.read(valueHeader, stream);
                    field.set(newValue);
                }
            } else {
                // 書き込み対象が無いならスキップする
                stream.readBuffer(valueHeader.size);
            }
        }

        return instance;

    }

    static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}

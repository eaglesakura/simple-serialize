package com.eaglesakura.serialize;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.ObjectHeader;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * エンコードされたオブジェクトをデシリアライズする
 */
public class Deserializer {
    public Deserializer() {
    }

    public <T> T deserialize(Class<T> clazz, byte[] buffer) throws SerializeException {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(buffer), false);

        try {
            SerializeHeader header = SerializeHeader.read(stream);
            LogUtil.log("Magic(%x) Version(%d)", header.magic, header.version);

            return deserializeObject(clazz, stream);
        } catch (SerializeException e) {
            throw e;
        } catch (Exception e) {
            LogUtil.log(e);
        }
        return null;
    }

    private <T> T deserializeObject(Class<T> clazz, DataInputStream stream) throws Exception {
        // Objectを読み込む
        ObjectHeader header = ObjectHeader.read(stream);
        if (header.isNull()) {
            // nullオブジェクトなので何もしない
            return null;
        }

        T instance = newInstance(clazz);
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
                        Object vInstance = deserializeObject(field.type, stream);
                        array.add(vInstance);
                    }
                    field.set(array);
                } else if (valueHeader.isObject()) {
                    // 再帰的に生成させる
                    Object fInstance = deserializeObject(field.type, stream);
                    field.set(fInstance);
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

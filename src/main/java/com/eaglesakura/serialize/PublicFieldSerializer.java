package com.eaglesakura.serialize;

import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.ObjectHeader;
import com.eaglesakura.serialize.internal.PrimitiveFieldEncoder;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;
import com.eaglesakura.util.ReflectionUtil;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PublicFieldSerializer {

    /**
     * プリミティブ型を書き込むためのエンコーダ
     */
    PrimitiveFieldEncoder mPrimitiveFieldEncoder = new PrimitiveFieldEncoder();

    public PublicFieldSerializer() {
    }

    public byte[] serialize(Object obj) throws SerializeException {
        if (ReflectionUtil.isListInterface(obj.getClass())) {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            DataOutputStream stream = new DataOutputStream(os);
            new SerializeHeader().write(stream);
            encodeObject(ObjectHeader.ID_ROOT, obj, stream);
        } catch (SerializeException e) {
            LogUtil.log(e);
            throw e;
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalStateException();
        }

        return os.toByteArray();
    }

    private void encodeObject(short id, Object obj, DataOutputStream stream) throws Exception {

        if (obj != null && ReflectionUtil.isListInterface(obj.getClass())) {
            // Listならば配列として書き込む
            List array = (List) obj;
            ObjectHeader header = new ObjectHeader(id, ObjectHeader.OBJECT_FLAG_ARRAY, array.size());
            header.write(stream);
            for (Object aObj : array) {
                if (aObj instanceof String) {
                    encodeArrayString(id, (String) aObj, stream);
                } else if (aObj instanceof Enum) {
                    encodeArrayEnum(id, (Enum<?>) aObj, stream);
                } else {
                    encodeObject(id, aObj, stream);
                }
            }
        } else {
            // 通常オブジェクトならばそれを書き込む
            encodeSingleObject(id, obj, stream);
        }
    }

    private void encodeArrayString(short id, String obj, DataOutputStream stream) throws Exception {
        byte[] buffer = obj.getBytes(ObjectHeader.STRING_CHARSET);
        // ヘッダを書き込む
        new ObjectHeader(id, buffer.length > 255 ? ObjectHeader.OBJECT_FLAG_LARGEDATA : 0x00, buffer.length).write(stream);
        // データを書き込む
        stream.writeBuffer(buffer, 0, buffer.length);
    }

    private void encodeArrayEnum(short id, Enum<?> obj, DataOutputStream stream) throws Exception {
        // ヘッダを書き込む
        new ObjectHeader(id, (short) 0x00, 2).write(stream);
        stream.writeS16((short) obj.ordinal());
    }

    private void encodeSingleObject(short id, Object obj, DataOutputStream stream) throws Exception {
        Map<Short, SerializeTargetField> fieldMap = InternalSerializeUtil.listSerializeFields(obj);
        short flags;
        if (obj == null) {
            flags = ObjectHeader.OBJECT_FLAG_NULL;
        } else {
            flags = ObjectHeader.OBJECT_FLAG_GROUP;
        }

        new ObjectHeader(id, flags, fieldMap.size()).write(stream);

        Iterator<Map.Entry<Short, SerializeTargetField>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            SerializeTargetField value = iterator.next().getValue();

            if (value.value == null) {
                // Nullとして書き込む
                new ObjectHeader(value.id, ObjectHeader.OBJECT_FLAG_NULL, 0).write(stream);
            } else if (PrimitiveFieldEncoder.isSupport(value.type)) {
                // Primitiveとして書き込む
                final int primitiveSize = mPrimitiveFieldEncoder.getObjectSize(value);
                final short primitiveFlags = primitiveSize > 255 ? ObjectHeader.OBJECT_FLAG_LARGEDATA : 0x00;
                new ObjectHeader(value.id, primitiveFlags, primitiveSize).write(stream);
                mPrimitiveFieldEncoder.encode(value, stream);
            } else {
                // Objectとして再帰的に書き込む
                encodeObject(value.id, value.value, stream);
            }
        }
    }
}

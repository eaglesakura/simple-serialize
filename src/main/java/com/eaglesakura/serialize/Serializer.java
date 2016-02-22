package com.eaglesakura.serialize;

import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.ObjectHeader;
import com.eaglesakura.serialize.internal.PrimitiveFieldEncoder;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

public class Serializer {

    /**
     * プリミティブ型を書き込むためのエンコーダ
     */
    PrimitiveFieldEncoder mPrimitiveFieldEncoder = new PrimitiveFieldEncoder();

    public Serializer() {
    }

    public byte[] serialize(Object obj) throws SerialException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            DataOutputStream stream = new DataOutputStream(os, false);
            new SerializeHeader().write(stream);
            encodeObject(obj, stream);
        } catch (SerialException e) {
            LogUtil.log(e);
            throw e;
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalStateException();
        }

        return os.toByteArray();
    }

    private void encodeObject(Object obj, DataOutputStream stream) throws Exception {

        if (obj != null && InternalSerializeUtil.isListInterface(obj.getClass())) {
            // Listならば配列として書き込む
            List array = (List) obj;
            ObjectHeader header = new ObjectHeader(ObjectHeader.ID_ROOT, ObjectHeader.OBJECT_FLAG_ARRAY, array.size());
            header.write(stream);
            for (Object aObj : array) {
                encodeObject(aObj, stream);
            }
        } else {
            // 通常オブジェクトならばそれを書き込む
            encodeSingleObject(obj, stream);
        }
    }

    private void encodeSingleObject(Object obj, DataOutputStream stream) throws Exception {
        Map<Short, SerializeTargetField> fieldMap = InternalSerializeUtil.listSerializeFields(obj);
        short flags;
        if (obj == null) {
            flags = ObjectHeader.OBJECT_FLAG_NULL;
        } else {
            flags = ObjectHeader.OBJECT_FLAG_GROUP;
        }

        // ヘッダを書き込む
        new ObjectHeader(ObjectHeader.ID_ROOT, flags, fieldMap.size()).write(stream);

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
                encodeObject(value.object, stream);
            }
        }
    }
}

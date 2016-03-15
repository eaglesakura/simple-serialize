package com.eaglesakura.serialize;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.io.data.DataPackage;
import com.eaglesakura.serialize.error.FileFormatException;
import com.eaglesakura.serialize.error.SerializeIdConflictException;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;
import com.eaglesakura.util.ReflectionUtil;
import com.eaglesakura.util.SerializeUtil;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;

import example.model.ExtendsArraySerializeTarget;
import example.model.IdConflictTarget;
import example.model.NullableSerializeTarget;
import example.model.ObjPrimitiveSerializeTarget;
import example.model.PrimitiveSerializeTarget;
import example.model.RecursiveSerializeTarget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class SerializerTest {

    final int TRY_SERIALIZE_COUNT = 1024;

    <T> void assertSerialize(Class<T> clazz) throws Exception {

        LogUtil.log("Serialize :: " + clazz.getName());
        for (int i = 0; i < TRY_SERIALIZE_COUNT; ++i) {
            T obj = ReflectionUtil.newInstanceOrNull(clazz);
            byte[] buffer = SerializeUtil.serializePublicFieldObject(obj, true);
            assertNotNull(buffer);
            assertNotEquals(buffer.length, 0);
            Object deserialized = SerializeUtil.deserializePublicFieldObject(obj.getClass(), buffer);
            assertNotNull(deserialized);
            assertEquals(obj, deserialized);

            DataPackage packed = DataPackage.pack(DataPackage.class, buffer);
            byte[] unpacked = DataPackage.unpack(DataPackage.class, packed.getPackedBuffer());
            assertTrue(Arrays.equals(buffer, unpacked));
        }
        LogUtil.log("  Finished");
    }

    @Test
    public void Field列挙() throws Exception {
        PrimitiveSerializeTarget obj = new PrimitiveSerializeTarget();
        Map<Short, SerializeTargetField> fields = InternalSerializeUtil.listSerializeFields(obj);

        assertNotNull(fields);
        assertNotEquals(fields.size(), 0);
    }

    @Test
    public void ファイルヘッダーIO() throws Exception {
        SerializeHeader originHeader = new SerializeHeader();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(os);
        originHeader.write(stream);

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        DataInputStream inputStream = new DataInputStream(is);
        SerializeHeader readHeader = SerializeHeader.read(inputStream);

        assertEquals(originHeader, readHeader);
    }

    @Test(expected = FileFormatException.class)
    public void ヘッダが壊れている場合に読み込みを停止する() throws Exception {
        PrimitiveSerializeTarget target = new PrimitiveSerializeTarget();

        byte[] bytes = new PublicFieldSerializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        bytes[0] = 0x00;

        new PublicFieldDeserializer().deserialize(PrimitiveSerializeTarget.class, bytes);
    }

    @Test(expected = FileFormatException.class)
    public void ファイルバージョンが異なる場合に読み込みを停止する() throws Exception {
        PrimitiveSerializeTarget target = new PrimitiveSerializeTarget();

        byte[] bytes = new PublicFieldSerializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        bytes[4] = 0x12;

        new PublicFieldDeserializer().deserialize(PrimitiveSerializeTarget.class, bytes);
    }

    @Test(expected = SerializeIdConflictException.class)
    public void IDの重複は例外とする() throws Exception {
        new PublicFieldSerializer().serialize(new IdConflictTarget());
    }

    @Test
    public void Primitive型のシリアライズ() throws Exception {
        assertSerialize(PrimitiveSerializeTarget.class);
    }

    @Test
    public void Primitive型Objectのシリアライズ() throws Exception {
        assertSerialize(ObjPrimitiveSerializeTarget.class);
    }

    @Test
    public void Nullを許容したObjectシリアライズ() throws Exception {
        assertSerialize(NullableSerializeTarget.class);
    }

    @Test
    public void ObjectInObjectのシリアライズ() throws Exception {
        assertSerialize(RecursiveSerializeTarget.class);
    }

    @Test
    public void 配列作成と継承を行ったオブジェクトのシリアライズ() throws Exception {
        assertSerialize(ExtendsArraySerializeTarget.class);
    }
}

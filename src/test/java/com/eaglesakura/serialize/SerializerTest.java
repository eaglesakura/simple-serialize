package com.eaglesakura.serialize;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import example.model.ExtendsArraySerializeTarget;
import example.model.NullableSerializeTarget;
import example.model.ObjPrimitiveSerializeTarget;
import example.model.PrimitiveSerializeTarget;
import example.model.RecursiveSerializeTarget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class SerializerTest {
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
        DataOutputStream stream = new DataOutputStream(os, false);
        originHeader.write(stream);

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        DataInputStream inputStream = new DataInputStream(is, false);
        SerializeHeader readHeader = SerializeHeader.read(inputStream);

        assertEquals(originHeader, readHeader);
    }

    @Test
    public void Primitive型のシリアライズ() throws Exception {
        PrimitiveSerializeTarget target = new PrimitiveSerializeTarget();
        target.doubleValue = Math.random();

        byte[] bytes = new Serializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        PrimitiveSerializeTarget deserialize = new Deserializer().deserialize(PrimitiveSerializeTarget.class, bytes);

        assertEquals(target, deserialize);
    }

    @Test
    public void Primitive型Objectのシリアライズ() throws Exception {
        ObjPrimitiveSerializeTarget target = new ObjPrimitiveSerializeTarget();
        target.stringValue += System.currentTimeMillis();

        byte[] bytes = new Serializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        ObjPrimitiveSerializeTarget deserialize = new Deserializer().deserialize(ObjPrimitiveSerializeTarget.class, bytes);

        assertEquals(target, deserialize);
    }

    @Test
    public void Nullを許容したObjectシリアライズ() throws Exception {
        NullableSerializeTarget target = new NullableSerializeTarget();
        target.stringValue += System.currentTimeMillis();

        byte[] bytes = new Serializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        NullableSerializeTarget deserialize = new Deserializer().deserialize(NullableSerializeTarget.class, bytes);

        assertEquals(target, deserialize);
    }

    @Test
    public void ObjectInObjectのシリアライズ() throws Exception {
        RecursiveSerializeTarget target = new RecursiveSerializeTarget();

        byte[] bytes = new Serializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        RecursiveSerializeTarget deserialize = new Deserializer().deserialize(RecursiveSerializeTarget.class, bytes);

        assertEquals(target, deserialize);
    }

    @Test
    public void 配列作成と継承を行ったオブジェクトのシリアライズ() throws Exception {
        ExtendsArraySerializeTarget target = new ExtendsArraySerializeTarget();

        byte[] bytes = new Serializer().serialize(target);
        assertNotNull(bytes);
        assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        ExtendsArraySerializeTarget deserialize = new Deserializer().deserialize(ExtendsArraySerializeTarget.class, bytes);

        assertEquals(target, deserialize);
    }
}

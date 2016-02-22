package com.eaglesakura.serialize;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.SerializeHeader;
import com.eaglesakura.serialize.internal.SerializeTargetField;
import com.eaglesakura.util.LogUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import example.model.NullableSerializeTarget;
import example.model.ObjPrimitiveSerializeTarget;
import example.model.PrimitiveSerializeTarget;

/**
 *
 */
public class SerializerTest {

    @Test
    public void List継承チェック() {
        Assert.assertFalse(InternalSerializeUtil.isListInterface(Object.class));
        Assert.assertTrue(InternalSerializeUtil.isListInterface(List.class));
        Assert.assertTrue(InternalSerializeUtil.isListInterface(ArrayList.class));
        Assert.assertTrue(InternalSerializeUtil.isListInterface(LinkedList.class));
    }

    @Test
    public void Field列挙() throws Exception {
        PrimitiveSerializeTarget obj = new PrimitiveSerializeTarget();
        Map<Short, SerializeTargetField> fields = InternalSerializeUtil.listSerializeFields(obj);

        Assert.assertNotNull(fields);
        Assert.assertNotEquals(fields.size(), 0);
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

        Assert.assertEquals(originHeader, readHeader);
    }

    @Test
    public void Primitive型のシリアライズ() throws Exception {
        PrimitiveSerializeTarget target = new PrimitiveSerializeTarget();
        target.doubleValue = Math.random();

        byte[] bytes = new Serializer().serialize(target);
        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        PrimitiveSerializeTarget deserialize = new Deserializer().deserialize(PrimitiveSerializeTarget.class, bytes);

        Assert.assertEquals(target, deserialize);
    }

    @Test
    public void Primitive型Objectのシリアライズ() throws Exception {
        ObjPrimitiveSerializeTarget target = new ObjPrimitiveSerializeTarget();
        target.stringValue += System.currentTimeMillis();

        byte[] bytes = new Serializer().serialize(target);
        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        ObjPrimitiveSerializeTarget deserialize = new Deserializer().deserialize(ObjPrimitiveSerializeTarget.class, bytes);

        Assert.assertEquals(target, deserialize);
    }

    @Test
    public void Nullを許容したObjectシリアライズ() throws Exception {
        NullableSerializeTarget target = new NullableSerializeTarget();
        target.stringValue += System.currentTimeMillis();

        byte[] bytes = new Serializer().serialize(target);
        Assert.assertNotNull(bytes);
        Assert.assertNotEquals(bytes.length, 0);

        LogUtil.log("Primitive Encode(%d bytes)", bytes.length);

        NullableSerializeTarget deserialize = new Deserializer().deserialize(NullableSerializeTarget.class, bytes);

        Assert.assertEquals(target, deserialize);
    }
}

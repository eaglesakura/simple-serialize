package com.eaglesakura.serialize;

import com.eaglesakura.serialize.internal.InternalSerializeUtil;
import com.eaglesakura.serialize.internal.SerializeTargetField;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import example.model.PrimitiveSerializeTarget;

/**
 *
 */
public class SerializerTest {

    @Test
    public void List継承チェック() {
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
}

package com.eaglesakura.serialize;

import com.eaglesakura.serialize.internal.PrimitiveFieldEncoder;

import java.util.HashMap;
import java.util.Map;

public class Serializer {

    /**
     * エンコーダ
     */
    Map<Class, FieldEncoder> mCustomEncoders = new HashMap<>();

    PrimitiveFieldEncoder mPrimitiveFieldEncoder = new PrimitiveFieldEncoder();

    public Serializer() {
    }


//    private void validSerializeable(Class clazz) {
//        if (mCustomEncoders.get(clazz) != null) {
//            return;
//        } else if (InternalSerializeUtil.isListInterface(clazz)) {
//            return;
//        }
//
//        throw new IllegalStateException(String.format("Not Support class(%s)", clazz.getName()));
//    }

//    public byte[] encode(Object obj) {
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        try {
//
//        } catch (IOException e) {
//            LogUtil.log(e);
//            throw new IllegalStateException();
//        }
//
//        return os.toByteArray();
//    }
//
//    protected byte[] encodeObject(Object obj) throws IOException {
//
//    }
}

package com.eaglesakura.io.data;

import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.util.SerializeUtil;

import org.junit.Test;

import example.model.ExtendsArraySerializeTarget;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DataVerifierTest {


    @Test(expected = SerializeException.class)
    public void ファイルマジックが壊れた場合シリアライズに失敗する() throws Exception {
        byte[] origin = SerializeUtil.serializePublicFieldObject(new ExtendsArraySerializeTarget());

        byte[] packed = new DataVerifier().pack(origin);
        assertNotNull(packed);

        packed[1]--;    // マジックを壊す

        new DataVerifier().unpack(packed); // ここで例外が投げられる

        // ここに到達したら失敗
        fail();
    }

    @Test(expected = SerializeException.class)
    public void ベリファイデータが壊れた場合シリアライズに失敗する() throws Exception {
        byte[] origin = SerializeUtil.serializePublicFieldObject(new ExtendsArraySerializeTarget());

        byte[] packed = new DataVerifier().pack(origin);
        assertNotNull(packed);

        packed[packed.length - 1]--;    // ベリファイコードを壊す

        new DataVerifier().unpack(packed); // ここで例外が投げられる

        // ここに到達したら失敗
        fail();
    }
}

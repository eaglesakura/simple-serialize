package com.eaglesakura.serialize;

import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.internal.SerializeTargetField;

import java.lang.reflect.Field;

/**
 *
 */
public interface FieldEncoder {

    /**
     * 書き込み対象のbyte数を取得する。
     */
    int getObjectSize(SerializeTargetField field);

    /**
     * 値をエンコードする
     *
     * @param field  エンコード対象のフィールド
     * @param stream 書き込み対象ストリーム
     */
    void encode(SerializeTargetField field, DataOutputStream stream);
}

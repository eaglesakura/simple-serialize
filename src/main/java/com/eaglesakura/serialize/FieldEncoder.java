package com.eaglesakura.serialize;

import com.eaglesakura.io.DataOutputStream;

import java.lang.reflect.Field;

/**
 *
 */
public interface FieldEncoder {

    /**
     * 書き込み対象のbyte数を取得する。
     */
    int getObjectSize(Object current, Field field);

    /**
     * 値をエンコードする
     *
     * @param root    エンコード対象のRootオブジェクト
     * @param current 現在エンコード中のオブジェクト
     * @param header  書き込み済みのheader
     * @param field   エンコード対象のフィールド
     * @param stream  書き込み対象ストリーム
     */
    void encode(Object root, Object current, ObjectHeader header, Field field, DataOutputStream stream);
}

package com.eaglesakura.serialize;

/**
 * オブジェクトごとのヘッダ情報
 *
 * 値型,String,byte[]の場合
 * : [id 2byte][Flag 2byte][Size 1byte | 4byte][Data  n byte]
 *
 * Object型の場合(Integer等のラッパー型は扱わない）
 * : [id 2byte][Flag 2byte][Num 1byte][値型数...]
 *
 * List型の場合
 * : [id 2byte][Flag 2byte][Num 4byte][Object...]
 */
public class ObjectHeader {

    /**
     * nullが書き込まれている
     */
    public static final short OBJECT_FLAG_NULL = 0x1 << 0;

    /**
     * 配列が書き込まれている
     */
    public static final short OBJECT_FLAG_ARRAY = 0x1 << 1;

    /**
     * 大きなデータが書き込まれている
     * sizeが4byteとして扱う
     */
    public static final short OBJECT_FLAG_LARGEDATA = 0x1 << 2;

    /**
     * オブジェクトグループ（Object型）として扱う
     */
    public static final short OBJECT_FLAG_GROUP = 0x1 << 3;

    /**
     * 文字列のcharset
     */
    public static final String STRING_CHARSET = "UTF-8";

    public final byte id;

    public final short flags;

    public final int size;

    public ObjectHeader(byte id, short flags, int size) {
        this.id = id;
        this.flags = flags;
        this.size = size;
    }
}

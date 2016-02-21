package com.eaglesakura.serialize.internal;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;

import java.io.IOException;

/**
 * オブジェクトごとのヘッダ情報
 *
 * 値型,String,byte[]の場合
 * : [id 2byte][Flag 2byte][Size 1byte | 4byte][Data  n byte]
 *
 * Object型の場合(Integer等のラッパー型は扱わない）
 * : [id 2byte][Flag 2byte][Num 2byte][値型...]
 *
 * List型の場合
 * : [id 2byte][Flag 2byte][Num 4byte][Object...]
 *
 * Null Objectの場合
 * : [id 2byte][Flag 2byte]
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

    public final short id;

    public final short flags;

    /**
     * Primitive : バイト数
     * Array : 配列数
     * Object : Field数
     */
    public final int size;

    public static final short ID_ROOT = (short) 0xFEFE;

    public ObjectHeader(short id, short flags, int size) {
        this.id = id;
        this.flags = flags;
        this.size = size;
    }

    public boolean isArray() {
        return (flags & OBJECT_FLAG_ARRAY) != 0;
    }

    public boolean isNull() {
        return (flags & OBJECT_FLAG_NULL) != 0;
    }

    public boolean isObject() {
        return (flags & OBJECT_FLAG_GROUP) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectHeader that = (ObjectHeader) o;

        if (id != that.id) return false;
        if (flags != that.flags) return false;
        return size == that.size;

    }

    @Override
    public int hashCode() {
        int result = (int) id;
        result = 31 * result + (int) flags;
        result = 31 * result + size;
        return result;
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeS16(id);
        stream.writeS16(flags);

        if ((flags & OBJECT_FLAG_ARRAY) != 0) {
            stream.writeS32(size);
        } else if ((flags & OBJECT_FLAG_GROUP) != 0) {
            stream.writeS16((short) size);
        } else if ((flags & OBJECT_FLAG_LARGEDATA) != 0) {
            stream.writeS32(size);
        } else if ((flags & OBJECT_FLAG_NULL) != 0) {
            // null
        } else {
            stream.writeS8((byte) size);
        }
    }

    public static ObjectHeader read(DataInputStream stream) throws IOException {
        final short id = stream.readS16();
        final short flags = stream.readS16();

        final int size;
        if ((flags & OBJECT_FLAG_ARRAY) != 0) {
            size = stream.readS32();
        } else if ((flags & OBJECT_FLAG_GROUP) != 0) {
            size = stream.readS16();
        } else if ((flags & OBJECT_FLAG_LARGEDATA) != 0) {
            size = stream.readS32();
        } else if ((flags & OBJECT_FLAG_NULL) != 0) {
            size = 0;
        } else {
            size = stream.readS8();
        }

        return new ObjectHeader(id, flags, size);
    }
}

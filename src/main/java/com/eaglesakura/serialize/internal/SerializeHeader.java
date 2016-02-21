package com.eaglesakura.serialize.internal;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.error.FileFormatException;
import com.eaglesakura.serialize.error.SerializeException;

import java.io.IOException;

/**
 * シリアライザの各ヘッダ情報
 *
 * [4byte HEADER][1byte VERSION][Object...]
 */
public class SerializeHeader {
    /**
     * ヘッダ情報
     *
     * ヘッダが一致しない場合、読み込みを停止する。
     */
    static final int FILE_HEADER =
            (((int) 'E' & 0x000000FF) << 24) |
                    (((int) 'G' & 0x000000FF) << 16) |
                    (((int) 'B' & 0x000000FF) << 8) |
                    (((int) 'O' & 0x000000FF) << 0);

    /**
     * ファイルバージョン
     *
     * 一致しない場合は読み込みを停止する
     */
    static final byte FILE_HEADER_VERSION = 1;

    public final int magic;

    public final byte version;

    SerializeHeader(int magic, byte version) {
        this.magic = magic;
        this.version = version;
    }

    public SerializeHeader() {
        this(FILE_HEADER, FILE_HEADER_VERSION);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializeHeader header = (SerializeHeader) o;

        if (magic != header.magic) return false;
        return version == header.version;

    }

    @Override
    public int hashCode() {
        int result = magic;
        result = 31 * result + (int) version;
        return result;
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeS32(magic);
        stream.writeS8(version);
    }

    public static SerializeHeader read(DataInputStream stream) throws IOException, SerializeException {
        final int magic = stream.readS32();
        if (magic != FILE_HEADER) {
            throw new FileFormatException("File Format Error");
        }

        final byte version = stream.readS8();
        if (version != FILE_HEADER_VERSION) {
            throw new FileFormatException("Version Error :: " + version);
        }

        return new SerializeHeader(magic, version);
    }
}

package com.eaglesakura.util;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.PublicFieldDeserializer;
import com.eaglesakura.serialize.PublicFieldSerializer;
import com.eaglesakura.serialize.error.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SerializeUtil {
    /**
     * ファイル配列をシリアライズする
     */
    public static byte[] toByteArray(List<byte[]> data) {
        try {
            int sumSize = 4;
            for (byte[] file : data) {
                sumSize += (file.length + 4);
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream(sumSize);
            DataOutputStream dos = new DataOutputStream(os, false);

            dos.writeS32(data.size());
            Iterator<byte[]> iterator = data.iterator();
            while (iterator.hasNext()) {
                byte[] file = iterator.next();
                dos.writeFile(file);
            }

            return os.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    /**
     * toByteArrayでシリアライズしたデータをデシリアライズする
     */
    public static List<byte[]> toByteArrayList(byte[] buffer) {
        return toByteArrayList(buffer, 0, buffer.length);
    }

    /**
     * toByteArrayでシリアライズしたデータをデシリアライズする
     */
    public static List<byte[]> toByteArrayList(byte[] buffer, int offset, int length) {
        try {

            ByteArrayInputStream is = new ByteArrayInputStream(buffer, offset, length);
            DataInputStream dis = new DataInputStream(is, false);

            List<byte[]> result = new ArrayList<>();
            final int num = dis.readS32();

            for (int i = 0; i < num; ++i) {
                result.add(dis.readFile());
            }

            return result;
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalArgumentException("FormatError");
        }
    }

    /**
     * Key-Valueデータを圧縮する
     */
    public static byte[] toByteArray(Map<String, byte[]> data) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            DataOutputStream dos = new DataOutputStream(os, false);

            dos.writeS32((short) data.size());

            Iterator<Map.Entry<String, byte[]>> iterator = data.entrySet().iterator();

            // すべてのKey-Valueを単純にシリアライズする
            while (iterator.hasNext()) {
                Map.Entry<String, byte[]> entry = iterator.next();
                dos.writeString(entry.getKey());
                dos.writeFile(entry.getValue());
            }

            return os.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public static Map<String, byte[]> toKeyValue(byte[] buffer) {
        return toKeyValue(buffer, 0, buffer.length);
    }

    public static Map<String, byte[]> toKeyValue(byte[] buffer, int offset, int length) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(buffer, offset, length);
            DataInputStream dis = new DataInputStream(is, false);

            Map<String, byte[]> result = new HashMap<>();

            final int numData = dis.readS32();
            for (int i = 0; i < numData; ++i) {
                String key = dis.readString();
                byte[] value = dis.readFile();

                result.put(key, value);
            }

            return result;
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalArgumentException("FormatError");
        }
    }

    /**
     * Public Fieldに@Serializeアノテーションを付与したオブジェクトをシリアライズする
     */
    public static byte[] serializePublicFieldObject(Object obj) throws SerializeException {
        return serializePublicFieldObject(obj, false);
    }

    /**
     * Public Fieldに@Serializeアノテーションを付与したオブジェクトをシリアライズする
     *
     * 圧縮はgzipで行うが、圧縮後のデータのほうが肥大化する場合(小さなデータに顕著)は、容量の小さいサイズを返す。
     *
     * そのため、compressにtrueを指定された場合はrawの場合とgzipの場合2種類のデータが返却される可能性がある。
     *
     * @param obj      シリアライズするオブジェクト
     * @param compress データを圧縮する場合はtrue
     * @return シリアライズされたデータ
     */
    public static byte[] serializePublicFieldObject(Object obj, boolean compress) throws SerializeException {
        byte[] bytes = new PublicFieldSerializer().serialize(obj);
        if (compress) {
            return EncodeUtil.compressOrRaw(bytes);
        } else {
            return bytes;
        }
    }

    /**
     * Public Fieldに@Serializeアノテーションを付与したオブジェクトをデシリアライズする
     */
    public static <T> T deserializePublicFieldObject(Class<T> clazz, byte[] buffer) throws SerializeException {
        return new PublicFieldDeserializer().deserialize(clazz, EncodeUtil.decompressOrRaw(buffer));
    }
}

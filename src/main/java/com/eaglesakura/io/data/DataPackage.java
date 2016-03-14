package com.eaglesakura.io.data;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.error.FileFormatException;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.util.StringUtil;
import com.eaglesakura.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Bluetooth等を介して少量のデータ（インメモリに収まる程度）をやりとりするクラス
 * bluetooth/Wi-Fi-Direct/socket通信等で使用する
 * 受け取ったデータが壊れている場合は適宜dropする
 */
public class DataPackage {
    /**
     * エラー検証コードとヘッダを付与したデータ
     */
    byte[] mPackedBuffer;

    /**
     * パッケージを生成する
     */
    public DataPackage() {
    }

    /**
     * パッキングされた送信用データを取得する
     * <br>
     * このbufferにUniqueIDのデータは含まれない
     *
     * @return パッキングされた送信用バッファ
     */
    public byte[] getPackedBuffer() {
        return mPackedBuffer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPackage that = (DataPackage) o;

        return Arrays.equals(mPackedBuffer, that.mPackedBuffer);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mPackedBuffer);
    }

    private static final byte[] MAGIC = new byte[]{
            3, 1, 0, 3
    };

    /**
     * 検証用コードを生成する
     * <br>
     * このクラスはデータが壊れているか正常かのチェックのみを行うため、検証コードは非常に短く、衝突耐性は低い。
     *
     * 必要に応じて処理をオーバーライドすることで、データ改ざんにも対応できる。
     *
     * @return 検証用の短いバイト配列
     */
    protected byte[] createVerifyCode(byte[] buffer) {
        byte[] result = new byte[2];
        final int offset = 0;
        final int length = buffer.length;

        {
            // すべての配列を加算する
            for (int i = 0; i < length; ++i) {
                result[0] += buffer[offset + i];
            }
        }
        {
            // すべての配列を乗算する
            int temp = 1;
            for (int i = 0; i < length; ++i) {
                temp *= (((int) buffer[offset + i]) | 0x01);
            }
            result[1] = (byte) (temp & 0xFF);
        }

        return result;
    }

    /**
     * エンコードを行う
     *
     * @param userData オリジナルのデータ
     * @return パッキングされた送信用データ
     */
    public static DataPackage pack(Class<? extends DataPackage> clazz, byte[] userData) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            DataPackage result = Util.newInstanceOrNull(clazz);

            // マジックナンバー
            dos.writeBuffer(MAGIC, 0, MAGIC.length);

            // データ本体
            dos.writeFile(userData);

            // 検証コードを付与する
            {
                byte[] verify = result.createVerifyCode(userData);
                dos.writeBuffer(verify, 0, verify.length);
            }

            result.mPackedBuffer = os.toByteArray();
            return result;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * パッケージをデコードする
     *
     * @return 解凍されたデータ
     */
    public static byte[] unpack(Class<? extends DataPackage> clazz, byte[] packedBuffer) throws IOException, SerializeException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packedBuffer));
        {
            byte[] magic = dis.readBuffer(MAGIC.length);
            for (int i = 0; i < magic.length; ++i) {
                if (magic[i] != MAGIC[i]) {
                    throw new FileFormatException("Data Format Error");
                }
            }
        }

        DataPackage result = Util.newInstanceOrNull(clazz);

        byte[] file = dis.readFile();
        byte[] fileVerify = result.createVerifyCode(file);
        byte[] verify = dis.readBuffer(fileVerify.length);

        if (!Arrays.equals(fileVerify, verify)) {
            final String fileVerifyHex = StringUtil.toHexString(fileVerify);
            final String verifyHex = StringUtil.toHexString(verify);

            throw new FileFormatException(String.format("Verify Error [%s] != [%s]", fileVerifyHex, verifyHex));
        }

        // ファイル本体を返す
        return file;
    }
}

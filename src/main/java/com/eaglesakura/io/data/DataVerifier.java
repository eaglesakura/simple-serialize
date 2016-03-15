package com.eaglesakura.io.data;

import com.eaglesakura.io.DataInputStream;
import com.eaglesakura.io.DataOutputStream;
import com.eaglesakura.serialize.error.FileFormatException;
import com.eaglesakura.serialize.error.SerializeException;
import com.eaglesakura.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Byte配列にベリファイコードを付与し、端末間転送でのデータ破壊チェックを行う
 *
 * デフォルトでは非常に小さなデータ（最大で1kb前後）を想定するため、ベリファイコード自体も少量のみとなる。
 */
public class DataVerifier {
    private static final byte[] MAGIC = new byte[]{
            3, 1, 0, 3
    };

    /**
     * ヘッダに付与するマジックナンバーを取得する
     */
    protected byte[] getMagicNumber() {
        return MAGIC;
    }

    /**
     * チェックサムを計算する
     */
    protected byte[] getCheckSum(byte[] src) {
        final int offset = 0;
        final int length = src.length;

        short sum0 = 0;
        short sum1 = 0;
        {
            // すべての配列を加算する
            for (int i = 0; i < length; ++i) {
                sum0 += src[offset + i];
            }
        }
        {
            // すべての配列を乗算する
            int temp = 1;
            for (int i = 0; i < length; ++i) {
                temp *= (((int) src[offset + i]) | 0x01);
            }
            sum1 = (short) temp;
        }

        return new byte[]{
                (byte) (sum0 >> 8),
                (byte) (sum0),
                (byte) (sum1 >> 8),
                (byte) (sum1),
        };
    }

    /**
     * データにチェックサムを付与し、破壊や改ざんのチェックを行えるようにする
     *
     * @param src 元データ
     * @return チェックサムが付与されたデータ
     */
    public byte[] pack(byte[] src) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        try {
            // マジックナンバー
            {
                byte[] number = getMagicNumber();
                dos.writeBuffer(number, 0, number.length);
            }

            // データ本体
            dos.writeFile(src);

            // 検証コードを付与する
            {
                byte[] verify = getCheckSum(src);
                dos.writeBuffer(verify, 0, verify.length);
            }

            return os.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * pack()で処理されたデータから、元の情報を取得する。
     *
     * その際、データが壊れている場合は例外を投げる。
     *
     * @param packedData チェックサムが付与されたデータ
     * @return 元のデータ
     */
    public byte[] unpack(byte[] packedData) throws SerializeException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packedData));
        try {
            byte[] originMagic = getMagicNumber();
            byte[] magic = dis.readBuffer(originMagic.length);

            if (!Arrays.equals(originMagic, magic)) {
                throw new FileFormatException(StringUtil.format("Data Format Error %s != %s", StringUtil.toHexString(magic), StringUtil.toHexString(originMagic)));
            }

            byte[] file = dis.readFile();
            byte[] fileVerify = getCheckSum(file);
            byte[] verify = dis.readBuffer(fileVerify.length);

            if (!Arrays.equals(fileVerify, verify)) {
                final String fileVerifyHex = StringUtil.toHexString(fileVerify);
                final String verifyHex = StringUtil.toHexString(verify);

                throw new FileFormatException(StringUtil.format("Verify Error [%s] != [%s]", fileVerifyHex, verifyHex));
            }

            // ファイル本体を返す
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

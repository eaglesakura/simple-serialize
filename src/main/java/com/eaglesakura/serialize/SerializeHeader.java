package com.eaglesakura.serialize;

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
    public static final int FILE_HEADER =
            (((int) 'E' & 0x000000FF) << 24) |
                    (((int) 'G' & 0x000000FF) << 16) |
                    (((int) 'B' & 0x000000FF) << 8) |
                    (((int) 'O' & 0x000000FF) << 0);

    /**
     * ファイルバージョン
     *
     * 一致しない場合は読み込みを停止する
     */
    public static final byte FILE_HEADER_VERSION = 1;
}

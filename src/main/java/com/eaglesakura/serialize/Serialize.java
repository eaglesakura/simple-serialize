package com.eaglesakura.serialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * シリアライズ対象の変数に指定する
 *
 * MEMO: プリミティブ型の場合は互換性チェックが働くが、それ以外の場合はフィールドの削除を行うとreadが正常に行えない制限がある。
 *
 * そのため、ファイル上にシリアライズされているかつ互換性を保つ場合、不要なフィールドはDeprecatedにし、直接的なフィールド削除を絶対に行わないこと。
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Serialize {
    /**
     * シリアライズ後のデータID
     *
     * 同一class内でUniqueでなければならない。
     * 利用されるのは下位1byteのため、値は0x00～0xFFの必要がある
     */
    short id();
}

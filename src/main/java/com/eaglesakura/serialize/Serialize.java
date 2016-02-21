package com.eaglesakura.serialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * シリアライズ対象の変数に指定する
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

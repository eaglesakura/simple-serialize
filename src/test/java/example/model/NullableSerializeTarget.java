package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.util.RandomUtil;

import java.util.Arrays;

/**
 * Null許容のシリアライズ対象
 */
public class NullableSerializeTarget {
    @Serialize(id = 1)
    public String nullStringValue = null;

    @Serialize(id = 2)
    public String stringValue = RandomUtil.randString();

    @Serialize(id = 3)
    public byte[] nullByteArray = null;

    @Serialize(id = 4)
    public byte[] byteArray = RandomUtil.randBytes();

    @Serialize(id = 5)
    public byte[] byteLargeArray = RandomUtil.randBytes(1024 * 4);

    @Serialize(id = 6)
    public String largeStringValue = RandomUtil.randLargeString();

    public NullableSerializeTarget() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NullableSerializeTarget that = (NullableSerializeTarget) o;

        if (nullStringValue != null ? !nullStringValue.equals(that.nullStringValue) : that.nullStringValue != null)
            return false;
        if (stringValue != null ? !stringValue.equals(that.stringValue) : that.stringValue != null)
            return false;
        if (!Arrays.equals(nullByteArray, that.nullByteArray)) return false;
        if (!Arrays.equals(byteArray, that.byteArray)) return false;
        if (!Arrays.equals(byteLargeArray, that.byteLargeArray)) return false;
        return !(largeStringValue != null ? !largeStringValue.equals(that.largeStringValue) : that.largeStringValue != null);

    }

    @Override
    public int hashCode() {
        int result = nullStringValue != null ? nullStringValue.hashCode() : 0;
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + (nullByteArray != null ? Arrays.hashCode(nullByteArray) : 0);
        result = 31 * result + (byteArray != null ? Arrays.hashCode(byteArray) : 0);
        result = 31 * result + (byteLargeArray != null ? Arrays.hashCode(byteLargeArray) : 0);
        result = 31 * result + (largeStringValue != null ? largeStringValue.hashCode() : 0);
        return result;
    }
}

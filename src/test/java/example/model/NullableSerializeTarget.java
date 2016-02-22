package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

import java.util.Arrays;

/**
 * Null許容のシリアライズ対象
 */
public class NullableSerializeTarget {
    @Serialize(id = 1)
    public String nullStringValue = null;

    @Serialize(id = 2)
    public String stringValue = SerializerTestUtil.randString();

    @Serialize(id = 3)
    public byte[] nullByteArray = null;

    @Serialize(id = 4)
    public byte[] byteArra = SerializerTestUtil.randBytes();

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
        return Arrays.equals(byteArra, that.byteArra);

    }

    @Override
    public int hashCode() {
        int result = nullStringValue != null ? nullStringValue.hashCode() : 0;
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + (nullByteArray != null ? Arrays.hashCode(nullByteArray) : 0);
        result = 31 * result + (byteArra != null ? Arrays.hashCode(byteArra) : 0);
        return result;
    }
}

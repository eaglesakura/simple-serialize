package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

import java.util.Arrays;

public class ObjPrimitiveSerializeTarget {
    @Serialize(id = 1)
    public byte[] byteArrayValue = SerializerTestUtil.randBytes();

    @Serialize(id = 2)
    public String stringValue = SerializerTestUtil.randString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjPrimitiveSerializeTarget that = (ObjPrimitiveSerializeTarget) o;

        if (!Arrays.equals(byteArrayValue, that.byteArrayValue)) return false;
        return !(stringValue != null ? !stringValue.equals(that.stringValue) : that.stringValue != null);

    }

    @Override
    public int hashCode() {
        int result = byteArrayValue != null ? Arrays.hashCode(byteArrayValue) : 0;
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        return result;
    }
}

package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendsArraySerializeTarget extends PrimitiveSerializeTarget {

    @Serialize(id = 1)
    public RecursiveSerializeTarget recursiveSerializeTarget;

    @Serialize(id = 2)
    public List<PrimitiveSerializeTarget> primitives = Arrays.asList(null, null, new PrimitiveSerializeTarget(), null);

    @Serialize(id = 3)
    public List<PrimitiveSerializeTarget> nullPrimitives = null;

    @Serialize(id = 4)
    public List<String> strings = Arrays.asList(SerializerTestUtil.randString(), null, SerializerTestUtil.randString(), null, null, SerializerTestUtil.randString(), SerializerTestUtil.randLargeString(), SerializerTestUtil.randLargeString());

    @Serialize(id = 5)
    public List<String> nullStrings = null;

    @Serialize(id = 6)
    public List<String> zeroString = new ArrayList<>();

    @Serialize(id = 7)
    public List<PrimitiveSerializeTarget.TestEnum> enums = Arrays.asList(TestEnum.EndValue, null, TestEnum.Value0);

    @Serialize(id = 8)
    public List<PrimitiveSerializeTarget.TestEnum> nullEnums;

    @Serialize(id = 9)
    public List<PrimitiveSerializeTarget.TestEnum> zeroEnums = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExtendsArraySerializeTarget that = (ExtendsArraySerializeTarget) o;

        if (recursiveSerializeTarget != null ? !recursiveSerializeTarget.equals(that.recursiveSerializeTarget) : that.recursiveSerializeTarget != null)
            return false;
        if (primitives != null ? !primitives.equals(that.primitives) : that.primitives != null)
            return false;
        if (nullPrimitives != null ? !nullPrimitives.equals(that.nullPrimitives) : that.nullPrimitives != null)
            return false;
        if (strings != null ? !strings.equals(that.strings) : that.strings != null) return false;
        if (nullStrings != null ? !nullStrings.equals(that.nullStrings) : that.nullStrings != null)
            return false;
        if (zeroString != null ? !zeroString.equals(that.zeroString) : that.zeroString != null)
            return false;
        if (enums != null ? !enums.equals(that.enums) : that.enums != null) return false;
        if (nullEnums != null ? !nullEnums.equals(that.nullEnums) : that.nullEnums != null)
            return false;
        return !(zeroEnums != null ? !zeroEnums.equals(that.zeroEnums) : that.zeroEnums != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (recursiveSerializeTarget != null ? recursiveSerializeTarget.hashCode() : 0);
        result = 31 * result + (primitives != null ? primitives.hashCode() : 0);
        result = 31 * result + (nullPrimitives != null ? nullPrimitives.hashCode() : 0);
        result = 31 * result + (strings != null ? strings.hashCode() : 0);
        result = 31 * result + (nullStrings != null ? nullStrings.hashCode() : 0);
        result = 31 * result + (zeroString != null ? zeroString.hashCode() : 0);
        result = 31 * result + (enums != null ? enums.hashCode() : 0);
        result = 31 * result + (nullEnums != null ? nullEnums.hashCode() : 0);
        result = 31 * result + (zeroEnums != null ? zeroEnums.hashCode() : 0);
        return result;
    }
}

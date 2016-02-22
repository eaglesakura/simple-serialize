package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

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
        return !(nullStrings != null ? !nullStrings.equals(that.nullStrings) : that.nullStrings != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (recursiveSerializeTarget != null ? recursiveSerializeTarget.hashCode() : 0);
        result = 31 * result + (primitives != null ? primitives.hashCode() : 0);
        result = 31 * result + (nullPrimitives != null ? nullPrimitives.hashCode() : 0);
        result = 31 * result + (strings != null ? strings.hashCode() : 0);
        result = 31 * result + (nullStrings != null ? nullStrings.hashCode() : 0);
        return result;
    }
}

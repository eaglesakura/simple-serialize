package example.model;

import com.eaglesakura.serialize.Serialize;

import java.util.Arrays;
import java.util.List;

public class ExtendsArraySerializeTarget extends PrimitiveSerializeTarget {

    @Serialize(id = 1)
    public RecursiveSerializeTarget recursiveSerializeTarget;

    @Serialize(id = 2)
    public List<PrimitiveSerializeTarget> primitives = Arrays.asList(null, null, new PrimitiveSerializeTarget(), null);

    @Serialize(id = 3)
    public List<PrimitiveSerializeTarget> nullPrimitives = null;

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
        return !(nullPrimitives != null ? !nullPrimitives.equals(that.nullPrimitives) : that.nullPrimitives != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (recursiveSerializeTarget != null ? recursiveSerializeTarget.hashCode() : 0);
        result = 31 * result + (primitives != null ? primitives.hashCode() : 0);
        result = 31 * result + (nullPrimitives != null ? nullPrimitives.hashCode() : 0);
        return result;
    }
}

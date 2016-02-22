package example.model;

import com.eaglesakura.serialize.Serialize;

/**
 * Object in Objectのシリアライズを行う
 */
public class RecursiveSerializeTarget {

    @Serialize(id = 1)
    public PrimitiveSerializeTarget primitive = new PrimitiveSerializeTarget();

    @Serialize(id = 2)
    public NullableSerializeTarget nullable = new NullableSerializeTarget();

    @Serialize(id = 3)
    public ObjPrimitiveSerializeTarget obj = new ObjPrimitiveSerializeTarget();

    @Serialize(id = 4)
    public ObjPrimitiveSerializeTarget objNull = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecursiveSerializeTarget that = (RecursiveSerializeTarget) o;

        if (primitive != null ? !primitive.equals(that.primitive) : that.primitive != null)
            return false;
        if (nullable != null ? !nullable.equals(that.nullable) : that.nullable != null)
            return false;
        if (obj != null ? !obj.equals(that.obj) : that.obj != null) return false;
        return !(objNull != null ? !objNull.equals(that.objNull) : that.objNull != null);

    }

    @Override
    public int hashCode() {
        int result = primitive != null ? primitive.hashCode() : 0;
        result = 31 * result + (nullable != null ? nullable.hashCode() : 0);
        result = 31 * result + (obj != null ? obj.hashCode() : 0);
        result = 31 * result + (objNull != null ? objNull.hashCode() : 0);
        return result;
    }
}

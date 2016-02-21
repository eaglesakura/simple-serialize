package example.model;

import com.eaglesakura.serialize.Serialize;

/**
 * Primitive構成された
 */
public class PrimitiveSerializeTarget {
    @Serialize(id = 20)
    public boolean booleanValue = true;
    @Serialize(id = 21)
    public byte byteValue = 1;
    @Serialize(id = 22)
    public short shortValue = 2;
    @Serialize(id = 24)
    public int intValue = 4;
    @Serialize(id = 28)
    public long longValue = 8;
    @Serialize(id = 34)
    public float floatValue = 14;
    @Serialize(id = 38)
    public double doubleValue = 18;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrimitiveSerializeTarget that = (PrimitiveSerializeTarget) o;

        if (booleanValue != that.booleanValue) return false;
        if (byteValue != that.byteValue) return false;
        if (shortValue != that.shortValue) return false;
        if (intValue != that.intValue) return false;
        if (longValue != that.longValue) return false;
        if (Float.compare(that.floatValue, floatValue) != 0) return false;
        return Double.compare(that.doubleValue, doubleValue) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (booleanValue ? 1 : 0);
        result = 31 * result + (int) byteValue;
        result = 31 * result + (int) shortValue;
        result = 31 * result + intValue;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        result = 31 * result + (floatValue != +0.0f ? Float.floatToIntBits(floatValue) : 0);
        temp = Double.doubleToLongBits(doubleValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

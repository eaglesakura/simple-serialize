package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

/**
 * Primitive構成された
 */
public class PrimitiveSerializeTarget {
    @Serialize(id = 20)
    public boolean booleanValue = SerializerTestUtil.randBool();
    @Serialize(id = 21)
    public byte byteValue = SerializerTestUtil.randInteger();
    @Serialize(id = 22)
    public short shortValue = SerializerTestUtil.randInteger();
    @Serialize(id = 24)
    public int intValue = SerializerTestUtil.randInteger();
    @Serialize(id = 28)
    public long longValue = SerializerTestUtil.randInteger();
    @Serialize(id = 34)
    public float floatValue = SerializerTestUtil.randFloat();
    @Serialize(id = 38)
    public double doubleValue = SerializerTestUtil.randFloat();

    @Serialize(id = 40)
    public Byte ByteValue = SerializerTestUtil.randInteger();
    @Serialize(id = 41)
    public Short ShortValue = (short) SerializerTestUtil.randInteger();
    @Serialize(id = 42)
    public Integer IntValue = (int) SerializerTestUtil.randInteger();
    @Serialize(id = 43)
    public Long LongValue = (long) SerializerTestUtil.randInteger();
    @Serialize(id = 44)
    public Float FloatValue = SerializerTestUtil.randFloat();
    @Serialize(id = 45)
    public Double DoubleValue = (double) SerializerTestUtil.randFloat();

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
        if (Double.compare(that.doubleValue, doubleValue) != 0) return false;
        if (ByteValue != null ? !ByteValue.equals(that.ByteValue) : that.ByteValue != null)
            return false;
        if (ShortValue != null ? !ShortValue.equals(that.ShortValue) : that.ShortValue != null)
            return false;
        if (IntValue != null ? !IntValue.equals(that.IntValue) : that.IntValue != null)
            return false;
        if (LongValue != null ? !LongValue.equals(that.LongValue) : that.LongValue != null)
            return false;
        if (FloatValue != null ? !FloatValue.equals(that.FloatValue) : that.FloatValue != null)
            return false;
        return !(DoubleValue != null ? !DoubleValue.equals(that.DoubleValue) : that.DoubleValue != null);

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
        result = 31 * result + (ByteValue != null ? ByteValue.hashCode() : 0);
        result = 31 * result + (ShortValue != null ? ShortValue.hashCode() : 0);
        result = 31 * result + (IntValue != null ? IntValue.hashCode() : 0);
        result = 31 * result + (LongValue != null ? LongValue.hashCode() : 0);
        result = 31 * result + (FloatValue != null ? FloatValue.hashCode() : 0);
        result = 31 * result + (DoubleValue != null ? DoubleValue.hashCode() : 0);
        return result;
    }
}

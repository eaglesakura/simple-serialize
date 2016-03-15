package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.util.RandomUtil;

/**
 * Primitive構成された
 */
public class PrimitiveSerializeTarget {
    @Serialize(id = 20)
    public boolean booleanValue = RandomUtil.randBool();
    @Serialize(id = 21)
    public byte byteValue = RandomUtil.randInt8();
    @Serialize(id = 22)
    public short shortValue = RandomUtil.randInt16();
    @Serialize(id = 24)
    public int intValue = RandomUtil.randInt32();
    @Serialize(id = 28)
    public long longValue = RandomUtil.randInt64();
    @Serialize(id = 34)
    public float floatValue = RandomUtil.randFloat();
    @Serialize(id = 38)
    public double doubleValue = RandomUtil.randFloat();

    @Serialize(id = 40)
    public Byte ByteValue = RandomUtil.randInt8();
    @Serialize(id = 41)
    public Short ShortValue = RandomUtil.randInt16();
    @Serialize(id = 42)
    public Integer IntValue = RandomUtil.randInt32();
    @Serialize(id = 43)
    public Long LongValue = RandomUtil.randInt64();
    @Serialize(id = 44)
    public Float FloatValue = RandomUtil.randFloat();
    @Serialize(id = 45)
    public Double DoubleValue = (double) RandomUtil.randFloat();

    public enum TestEnum {
        Value0,
        Value1,
        Value3,
        EndValue;

        public static TestEnum rand() {
            return RandomUtil.randEnum(TestEnum.class);
        }
    }

    @Serialize(id = 50)
    public TestEnum nullEnumValue;

    @Serialize(id = 51)
    public TestEnum enumValue = TestEnum.rand();

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
        if (DoubleValue != null ? !DoubleValue.equals(that.DoubleValue) : that.DoubleValue != null)
            return false;
        if (nullEnumValue != that.nullEnumValue) return false;
        return enumValue == that.enumValue;

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
        result = 31 * result + (nullEnumValue != null ? nullEnumValue.hashCode() : 0);
        result = 31 * result + (enumValue != null ? enumValue.hashCode() : 0);
        return result;
    }
}

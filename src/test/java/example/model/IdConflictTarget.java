package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.serialize.SerializerTestUtil;

public class IdConflictTarget {
    @Serialize(id = 1)
    public int intValue0 = SerializerTestUtil.randInteger();

    @Serialize(id = 1)
    public int intValue1 = SerializerTestUtil.randInteger();
}

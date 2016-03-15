package example.model;

import com.eaglesakura.serialize.Serialize;
import com.eaglesakura.util.RandomUtil;

public class IdConflictTarget {
    @Serialize(id = 1)
    public int intValue0 = RandomUtil.randInt32();

    @Serialize(id = 1)
    public int intValue1 = RandomUtil.randInt32();
}

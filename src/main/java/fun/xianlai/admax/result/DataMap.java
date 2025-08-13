package fun.xianlai.admax.result;

import java.util.HashMap;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
public class DataMap extends HashMap<String, Object> {
    public DataMap() {
        super();
    }

    public DataMap(String key, Object value) {
        super();
        this.put(key, value);
    }
}

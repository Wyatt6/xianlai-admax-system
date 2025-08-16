package fun.xianlai.admax.definition.response;

import java.util.HashMap;

/**
 * @author WyattLau
 * @date 2023/9/6
 */
public class Map extends HashMap<String, Object> {
    public Map() {
        super();
    }

    public Map(String key, Object value) {
        super();
        this.put(key, value);
    }
}

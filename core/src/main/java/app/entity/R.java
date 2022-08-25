package app.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 与前端交互的数据实体
 *
 * @author 徐鑫
 */
@Data
public class R {

    public static int SUCCESS_CODE = 200;
    public static int FAIL_CODE = 500;

    private int statusCode;
    private String message;
    private Map<String, Object> data;

    static public R success() {
        R m = new R();
        m.setMessage("请求成功");
        m.setStatusCode(SUCCESS_CODE);
        return m;
    }

    static public R success(String message) {
        R m = new R();
        m.setStatusCode(SUCCESS_CODE);
        m.setMessage(message);
        return m;
    }

    static public R fail() {
        R r = new R();
        r.setMessage("请求失败");
        r.setStatusCode(FAIL_CODE);
        return r;
    }

    static public R fail(String message) {
        R r = new R();
        r.setMessage(message);
        r.setStatusCode(FAIL_CODE);
        return r;
    }

    public R addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>(0);
        }
        this.data.put(key, value);
        return this;
    }
}

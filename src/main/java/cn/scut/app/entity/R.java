package cn.scut.app.entity;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 与前端交互的数据实体
 * @author 徐鑫
 */
@Data
public class R {
    private int statusCode;
    private String message;
    private Map<String, Object> data;

    public R addData(String key, Object value){
        if (this.data == null) {
            this.data = new HashMap<>(0);
        }
        this.data.put(key, value);
        return this;
    }

    static public R success(){
        R m = new R();
        m.setMessage("请求成功");
        m.setStatusCode(200);
        return m;
    }
    static public R success(String message) {
        R m = new R();
        m.setStatusCode(200);
        m.setMessage(message);
        return m;
    }
    static public R fail() {
        R r = new R();
        r.setMessage("请求失败");
        r.setStatusCode(500);
        return r;
    }
    static public R fail(String message) {
        R r = new R();
        r.setMessage(message);
        r.setStatusCode(500);
        return r;
    }
}

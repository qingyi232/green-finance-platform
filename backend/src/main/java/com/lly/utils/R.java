package com.lly.utils;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    public R() { put("code", 0); put("msg", "success"); }
    public static R ok() { return new R(); }
    public static R ok(String msg) { R r = new R(); r.put("msg", msg); return r; }
    public static R error() { return error(500, "服务器内部错误"); }
    public static R error(String msg) { return error(500, msg); }
    public static R error(int code, String msg) {
        R r = new R(); r.put("code", code); r.put("msg", msg); return r;
    }
    @Override
    public R put(String key, Object value) { super.put(key, value); return this; }
}

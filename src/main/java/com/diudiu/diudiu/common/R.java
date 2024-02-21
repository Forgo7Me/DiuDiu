package com.diudiu.diudiu.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 给世界一点小小的二次元震撼
 *
 *  ██   ██         ███████   ████     ██
 * ░██  ██         ██░░░░░██ ░██░██   ░██
 * ░██ ██         ██     ░░██░██░░██  ░██
 * ░████    █████░██      ░██░██ ░░██ ░██
 * ░██░██  ░░░░░ ░██      ░██░██  ░░██░██
 * ░██░░██       ░░██     ██ ░██   ░░████
 * ░██ ░░██       ░░███████  ░██    ░░███
 * ░░   ░░         ░░░░░░░   ░░      ░░░
 *
 * @Author : Drme
 * @Date : 2024/02/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    public R_code code;
    public String msg;
    public Object data;

    public static R ok() {
        return new R(R_code.SUCCESS, "操作成功", null);
    }
    public static R ok(Object data) {
        return new R(R_code.SUCCESS, "操作成功", data);
    }
    public static R error(String msg) {
        return new R(R_code.ERROR, msg, null);
    }
    // 超时
    public static R timeout() {
        return new R(R_code.TIMEOUT, "操作超时", null);
    }
    // 无权限
    public static R noPower() {
        return new R(R_code.NOPOWER, "无权限", null);
    }
}

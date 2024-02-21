package com.diudiu.diudiu.util;

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
public class FixStateUtil {
    public static String getState(Integer state) {
        if (state == 0) {
            return "未处理";
        } else if (state == 1) {
            return "处理中";
        } else if (state == 2) {
            return "已完成";
        } else {
            return "未知状态";
        }
    }
}

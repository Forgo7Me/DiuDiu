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
            return "管理员审核中";
        } else if (state == 1) {
            return "审核通过";
        } else if (state == 2) {
            return "已接单";
        } else if (state == 3) {
            return "已完成";
        } else if (state == 4){
            return "审核未通过";
        } else if (state == 5){
            return "已取消";
        }
        else{
            return "未知状态";
        }
    }
}

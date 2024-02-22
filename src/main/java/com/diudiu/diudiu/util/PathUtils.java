package com.diudiu.diudiu.util;

import java.util.UUID;

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
 * @Date : 2024/02/22
 */
public class PathUtils {
    static String place = "diudiu/";
    // 帖子上传的路径
    public static String postFilePath(String fileName){
        // UUID作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        return place + "post/" + uuid + fileType;
    }

    // 用户头像上传的路径
    public static String userAvaFilePath(String fileName){
        // UUID作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        return place + "user/" + uuid + fileType;
    }

    // 管理员头像上传
    public static String adminAvaFilePath(String fileName){
        // UUID作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);
        return place + "admin/" + uuid + fileType;
    }
}

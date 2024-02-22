package com.diudiu.diudiu.entity.dto;

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
public class PostDto {
    //帖子id,主键
    private Integer id;
    //帖子文本内容
    private String content;
    //帖子图片地址
    private String img;
    //发帖人id,外联user表的id
    private Integer userId;
    // 发帖人姓名
    private String userName;
    // 发帖人头像
    private String ava;
    //发帖时间,13位时间戳
    private String createTime;
    //状态"正常","删除"
    private String status;
    //浏览量
    private Integer viewCount;
    //点赞量
    private Integer favorCount;
    //评论数
    private Integer commentCount;
    //分享数
    private Integer shareCount;
    // 是否被点赞
    private String isFavor;
}

package com.diudiu.diudiu.entity.dto;

import com.diudiu.diudiu.entity.PostComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class PostCommentDto {
    //评论id,主键
    private Integer id;
    // 评论人的id,外联user表的id
    private Integer userId;
    // 评论人的姓名
    private String userName;
    // 评论人的头像
    private String ava;
    //所属帖子的id,外联post表的id
    private Integer postId;
    //评论内容
    private String content;
    //评论时间,13位时间戳
    private String createTime;
    //状态,"正常""删除
    private String status;
    // 子评论
    private List<PostSonCommentDto> sonComment;
}

package com.diudiu.diudiu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @Author : DRME
 * @Create : com.sjhy.plugin.tool.TimeUtils@4a16de20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice implements Serializable {
    //公告id,主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //标题
    private String title;
    //公告内容
    private String context;
    //公告发布时间
    private String createTime;
    //是否需要确认收到
    private String confirm;
    //发布公告的管理员的id,连接admin表的id
    private Integer adminId;

    // 是否已被用户确认
    @TableField(exist = false)
    private String confirmed;

}


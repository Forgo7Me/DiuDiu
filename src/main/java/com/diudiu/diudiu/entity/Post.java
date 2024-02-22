package com.diudiu.diudiu.entity;

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
* @Create : com.sjhy.plugin.tool.TimeUtils@48d92da5
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    //帖子id,主键
    private Integer id;
    //帖子文本内容
    private String content;
    //帖子图片地址
    private String img;
    //发帖人id,外联user表的id
    private Integer userId;
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

}


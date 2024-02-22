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
public class PostComment implements Serializable {
    //评论id,主键
    private Integer id;
    // 评论人的id,外联user表的id
    private Integer userId;
    //所属帖子的id,外联post表的id
    private Integer postId;
    //评论内容
    private String content;
    //评论时间,13位时间戳
    private String createTime;
    //状态,"正常""删除
    private String status;
    //是否为回复评论，默认为"否"
    private String response;
    //回复的评论的id,默认为0
    private Integer responsePostId;

}


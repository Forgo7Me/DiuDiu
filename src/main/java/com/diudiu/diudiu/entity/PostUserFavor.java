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
public class PostUserFavor implements Serializable {
    //主键
    private Integer id;
    //帖子的id,外联post表的id
    private Integer postId;
    //用户的id,外联post表的id
    private Integer userId;
    //是否点赞,默认为"否"
    private String favor;

}


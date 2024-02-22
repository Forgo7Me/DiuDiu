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
* @Create : com.sjhy.plugin.tool.TimeUtils@61694d01
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLog implements Serializable {
    //聊天记录id，主键
    private Integer id;
    //发送方id
    private Integer senderId;
    //接收方id
    private Integer accepterId;
    //聊天内容
    private String content;
    //发送时间
    private Long createTime;
    //身份"管理员"或"用户"
    private String senderIdentity;
    // 接收方身份
    private String accepterIdentity;
    // 是否被接收方查看
    private String checked;

}


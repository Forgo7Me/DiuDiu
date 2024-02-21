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
* @Create : com.sjhy.plugin.tool.TimeUtils@7abc02cd
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixLog implements Serializable {
    //报修记录id,主键
    private Integer id;
    //报修人id,外联user表的id
    private Integer userId;
    //处理人id,外联admin表的id
    private Integer adminId;
    //处理状态id,0为未处理,1为处理中,2为已完成
    private Integer state;
    //问题描述
    private String description;
    //报修时间,13位时间戳
    private String createTime;
    //开始处理时间,13位时间戳
    private String takeTime;
    //结束处理时间,13位时间戳
    private String finishTime;

}


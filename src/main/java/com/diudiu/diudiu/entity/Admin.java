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
public class Admin implements Serializable {
    //管理员id，主键
    private Integer id;
    //管理员账号
    private String username;
    //管理员密码
    private String password;
    //管理员名字
    private String name;
    //管理员手机号
    private String phone;
    //管理员性别
    private String gender;
    //管理员头像地址
    private String ava;
    //管理员负责园区，默认为0
    private String siteY;
    //管理员负责楼栋，默认为0
    private String siteD;
    //管理员身份，普通管理员为NORMAL,超级管理员为SUPER
    private String type;

}


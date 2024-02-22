package com.diudiu.diudiu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.ChatLog;
import org.springframework.stereotype.Service;

import java.util.Map;

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
@Service
public interface ChatLogService extends IService<ChatLog> {

    Integer getUncheckedMessageCount(Map<String, Object> map);
}


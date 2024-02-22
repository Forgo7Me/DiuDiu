package com.diudiu.diudiu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.mapper.ChatLogMapper;
import com.diudiu.diudiu.entity.ChatLog;
import com.diudiu.diudiu.service.ChatLogService;
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
@Service("chatLogService")
public class ChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements ChatLogService {

    @Override
    public Integer getUncheckedMessageCount(Map<String, Object> map) {
        Integer senderId = (Integer) map.get("senderId");
        Integer accepterId = (Integer) map.get("accepterId");
        String senderIdentity = (String) map.get("senderIdentity");
        String accepterIdentity = (String) map.get("accepterIdentity");
        Integer count = Math.toIntExact(
                this.count(
                        new LambdaQueryWrapper<ChatLog>()
                                .eq(ChatLog::getSenderId, accepterId)
                                .eq(ChatLog::getAccepterId, senderId)
                                .eq(ChatLog::getSenderIdentity, accepterIdentity)
                                .eq(ChatLog::getAccepterIdentity, senderIdentity)
                                .eq(ChatLog::getChecked, "否")
                ));
        return count;
    }
}


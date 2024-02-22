package com.diudiu.diudiu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.common.ChatLogType;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Admin;
import com.diudiu.diudiu.entity.ChatLog;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.entity.dto.ChatInfo;
import com.diudiu.diudiu.entity.dto.ChatLogDto;
import com.diudiu.diudiu.service.AdminService;
import com.diudiu.diudiu.service.ChatLogService;
import com.diudiu.diudiu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RestController
@RequestMapping("/chat")
@CrossOrigin
public class ChatLogController {
    @Resource
    private ChatLogService chatLogService;
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;

    @PostMapping("/getChatInfo")
    public R userGetChatInfo(@RequestBody Map<String, Object> map) {
        Integer senderId = (Integer) map.get("senderId");
        Integer accepterId = (Integer) map.get("accepterId");
        String senderIdentity = (String) map.get("senderIdentity");
        String accepterIdentity = (String) map.get("accepterIdentity");
        ChatInfo chatInfo = new ChatInfo();
        if (senderIdentity.equals("管理员")) {
            Admin admin = adminService.getById(senderId);
            chatInfo.setSenderName(admin.getName());
            chatInfo.setSenderAva(admin.getAva());
            if (accepterIdentity.equals("管理员")) {
                Admin accepter = adminService.getById(accepterId);
                chatInfo.setAccepterName(accepter.getName());
                chatInfo.setAccepterAva(accepter.getAva());
            } else {
                User accepter = userService.getById(accepterId);
                chatInfo.setAccepterName(accepter.getName());
                chatInfo.setAccepterAva(accepter.getAva());
            }
        } else {
            User user = userService.getById(senderId);
            chatInfo.setSenderName(user.getName());
            chatInfo.setSenderAva(user.getAva());
            if (accepterIdentity.equals("管理员")) {
                Admin accepter = adminService.getById(accepterId);
                chatInfo.setAccepterName(accepter.getName());
                chatInfo.setAccepterAva(accepter.getAva());
            } else {
                User accepter = userService.getById(accepterId);
                chatInfo.setAccepterName(accepter.getName());
                chatInfo.setAccepterAva(accepter.getAva());
            }
        }
        // 将所有对方作为sender,user作为accepter的聊天记录设置为已查看
        List<ChatLog> unCheckedChatLog = chatLogService.list(
                new LambdaQueryWrapper<ChatLog>()
                        .eq(ChatLog::getSenderId, accepterId)
                        .eq(ChatLog::getAccepterId, senderId)
                        .eq(ChatLog::getSenderIdentity, accepterIdentity)
                        .eq(ChatLog::getAccepterIdentity, senderIdentity)
                        .eq(ChatLog::getChecked, "否")
        );
        unCheckedChatLog.forEach(chatLog -> {
            chatLog.setChecked("是");
            chatLogService.updateById(chatLog);
        });
        return R.ok(chatInfo);
    }

    @PostMapping("/getLog")
    public R getAll(@RequestBody Map<String, Object> map) {
        Integer senderId = (Integer) map.get("senderId");
        Integer accepterId = (Integer) map.get("accepterId");
        String senderIdentity = (String) map.get("senderIdentity");
        String accepterIdentity = (String) map.get("accepterIdentity");
        List<ChatLog> senderList = chatLogService.list(
                new LambdaQueryWrapper<ChatLog>()
                        .eq(ChatLog::getSenderId, senderId)
                        .eq(ChatLog::getAccepterId, accepterId)
                        .eq(ChatLog::getSenderIdentity, senderIdentity)
                        .eq(ChatLog::getAccepterIdentity, accepterIdentity)
        );
        List<ChatLog> accepterList = chatLogService.list(
                new LambdaQueryWrapper<ChatLog>()
                        .eq(ChatLog::getSenderId, accepterId)
                        .eq(ChatLog::getAccepterId, senderId)
                        .eq(ChatLog::getSenderIdentity, accepterIdentity)
                        .eq(ChatLog::getAccepterIdentity, senderIdentity)
        );
        senderList.addAll(accepterList);
        // 按时间排序
        senderList.sort((o1, o2) -> (int) (o1.getCreateTime() - o2.getCreateTime()));

        return R.ok(
                senderList.stream()
                        .map(chatLog -> {
                            ChatLogDto dto = new ChatLogDto();
                            dto.setId(chatLog.getId());
                            dto.setContent(chatLog.getContent());
                            dto.setCreateTime(chatLog.getCreateTime());
                            if (chatLog.getSenderId().equals(senderId) && chatLog.getSenderIdentity().equals(senderIdentity)) {
                                dto.setType(ChatLogType.SENDER);
                            } else {
                                dto.setType(ChatLogType.ACCEPTER);
                            }
                            return dto;
                        }).collect(Collectors.toList())
        );
    }

    @PostMapping("/chat")
    public R chat(@RequestBody ChatLog chatLog) {
        chatLog.setId((int) (chatLogService.count() + 1));
        chatLog.setCreateTime(System.currentTimeMillis());
        chatLog.setChecked("否");
        chatLogService.save(chatLog);
        ChatLogDto dto = new ChatLogDto();
        dto.setId(chatLog.getId());
        dto.setType(ChatLogType.SENDER);
        dto.setContent(chatLog.getContent());
        dto.setCreateTime(chatLog.getCreateTime());
        return R.ok(dto);
    }

    @PostMapping("/getUncheckedMessageCount")
    public R getUncheckedMessageCount(@RequestBody Map<String, Object> map) {
        return R.ok(chatLogService.getUncheckedMessageCount(map));
    }
}


































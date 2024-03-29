package com.diudiu.diudiu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Notice;
import com.diudiu.diudiu.entity.NoticeUser;
import com.diudiu.diudiu.service.AdminService;
import com.diudiu.diudiu.service.NoticeService;
import com.diudiu.diudiu.service.NoticeUserService;
import com.diudiu.diudiu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
 * @Create : com.sjhy.plugin.tool.TimeUtils@4a16de20
 */
@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    @Resource
    private NoticeUserService noticeUserService;
    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;

    // 获取所有公告
    /*
     * @Param: map 包含userId
     * @Return: R
     */
    @PostMapping("")
    public R getAllNotice(@RequestBody Map<String, Object> map) {
        Object userId = map.get("userId");
        return R.ok(
                noticeService.list()
                        .stream().map(notice -> {
                            if (notice.getConfirm().equals("是")) {
                                List<NoticeUser> noticeUsers = noticeUserService.list(
                                        new LambdaQueryWrapper<NoticeUser>().eq(NoticeUser::getNoticeId, notice.getId()).eq(NoticeUser::getUserId, userId)
                                );
                                if (noticeUsers.isEmpty()) notice.setConfirmed("否");
                                else notice.setConfirmed("是");
                            }
                            HashMap<String, Object> noticeMap = new HashMap<>();
                            noticeMap.put("notice", notice);
                            noticeMap.put("admin", adminService.getById(notice.getAdminId()));
                            return noticeMap;
                        })
        );
    }

    // 新增公告
    /*
     * @Param: map 包含adminId,title,context,confirm
     * @Return: R
     */
    @PostMapping("/add")
    public R addNotice(@RequestBody Map<String, Object> map) {
        Integer adminId = (Integer) map.get("adminId");
        String title = (String) map.get("title");
        String context = (String) map.get("context");
        String confirm = (String) map.get("confirm");
        String createTime = String.valueOf(System.currentTimeMillis());
        Notice notice = new Notice();
        notice.setAdminId(adminId);
        notice.setTitle(title);
        notice.setContext(context);
        notice.setConfirm(confirm);
        notice.setCreateTime(createTime);
        if (noticeService.save(notice)) {
            return R.ok("发布公告成功");
        }
        return R.error("发布公告失败");
    }

    // 确认收到公告
    /*
     * @Param: map 包含noticeId,userId
     * @Return: R
     * */
    @PostMapping("/confirm")
    public R confirmNotice(@RequestBody Map<String, Object> map) {
        Integer noticeId = (Integer) map.get("noticeId");
        Notice notice = noticeService.getById(noticeId);
        if (notice.getConfirm().equals("否")) {
            return R.error("该公告不需要确认收到");
        }
        Integer userId = (Integer) map.get("userId");
        NoticeUser noticeUser = new NoticeUser();
        noticeUser.setNoticeId(noticeId);
        noticeUser.setUserId(userId);
        if (noticeUserService.save(noticeUser)) {
            return R.ok("确认成功");
        }
        return R.error("确认失败");
    }

    // 管理员获取所有自己发布的公告
    /*
     * @Param: map 包含adminId
     * @Return: R
     */
    @PostMapping("/admin/all")
    public R getAllNoticeByAdmin(@RequestBody Map<String, Object> map) {
        Integer adminId = (Integer) map.get("adminId");
        // 查询user人数
        Long userCount = userService.count();
        return R.ok(
                noticeService.list(
                        new LambdaQueryWrapper<Notice>().eq(Notice::getAdminId, adminId)
                ).stream().map(notice -> {
                    HashMap<String, Object> noticeMap = new HashMap<>();
                    noticeMap.put("notice", notice);
                    noticeMap.put("admin", adminService.getById(notice.getAdminId()));
                    noticeMap.put("confirmUserCount", noticeUserService.list(new LambdaQueryWrapper<NoticeUser>().eq(NoticeUser::getNoticeId, notice.getId())).size());
                    noticeMap.put("userCount", userCount);
                    return noticeMap;
                })
        );
    }

    // 管理员编辑公告
    /*
     * @Param: map 包含noticeId,title,context,confirm
     * @Return: R
     */
    @PostMapping("/admin/edit")
    public R editNotice(@RequestBody Map<String, Object> map) {
        Integer noticeId = (Integer) map.get("noticeId");
        String title = (String) map.get("title");
        String context = (String) map.get("context");
        String confirm = (String) map.get("confirm");
        Notice notice = noticeService.getById(noticeId);
        notice.setTitle(title);
        notice.setContext(context);
        notice.setConfirm(confirm);
        if (noticeService.updateById(notice)) {
            return R.ok("编辑成功");
        }
        return R.error("编辑失败");
    }




}






























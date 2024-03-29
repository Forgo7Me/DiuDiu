package com.diudiu.diudiu.controller;

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
 * @Author : Drme
 * @Date : 2024/03/27
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.FixLog;
import com.diudiu.diudiu.entity.Notice;
import com.diudiu.diudiu.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/index")
public class IndexController {
    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private FixerService fixerService;
    @Resource
    private FixLogService fixLogService;
    @Resource
    private NoticeService noticeService;

    // 管理员获取到数据统计集
    @PostMapping("/dataSort")
    public R dataSort(@RequestBody Map<String, Object> map) {
        Object adminId = map.get("adminId");
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("userCount", userService.count());
        dataMap.put("fixerCount", fixerService.count());
        dataMap.put("waitingCount", fixLogService.count(new LambdaQueryWrapper<FixLog>().eq(FixLog::getState, 0).eq(FixLog::getAdminId, adminId))); // 获取待审核的报修数量
        dataMap.put("fixingCount", fixLogService.count(new LambdaQueryWrapper<FixLog>().eq(FixLog::getState, 1).eq(FixLog::getAdminId, adminId))); // 获取维修中的报修数量
        dataMap.put("fixedCount", fixLogService.count(new LambdaQueryWrapper<FixLog>().eq(FixLog::getState, 3).eq(FixLog::getAdminId, adminId))); // 获取已维修的报修数量
        dataMap.put("fixCountTime", fixLogService.list(new LambdaQueryWrapper<FixLog>().eq(FixLog::getAdminId, adminId)).stream().map(FixLog::getCreateTime)); // 获取报修时间
        // 获取后10个通知
        dataMap.put("notices", noticeService.list(new LambdaQueryWrapper<Notice>().eq(Notice::getAdminId, adminId).orderByDesc(Notice::getCreateTime).last("limit 10")));
        return R.ok(dataMap);
    }
}







































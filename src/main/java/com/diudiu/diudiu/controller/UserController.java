package com.diudiu.diudiu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.common.TokenRequired;
import com.diudiu.diudiu.entity.Admin;
import com.diudiu.diudiu.entity.FixLog;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.entity.dto.FixLogDto;
import com.diudiu.diudiu.service.AdminService;
import com.diudiu.diudiu.service.FixLogService;
import com.diudiu.diudiu.service.UserService;

import com.diudiu.diudiu.util.FixStateUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
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
 * @Create : com.sjhy.plugin.tool.TimeUtils@7abc02cd
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private FixLogService fixLogService;

    @TokenRequired
    @PostMapping("/findById")
    public R findById(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("userId");
        return R.ok(userService.getById(id));
    }

    @PostMapping("/findAdmin")
    public R findAdmin(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("userId");
        User user = userService.getById(id);
        return R.ok(
                adminService.getOne(
                        new LambdaQueryWrapper<Admin>()
                                .eq(Admin::getSiteY, user.getSiteY())
                                .eq(Admin::getSiteD, user.getSiteD())
                )
        );
    }

    @PostMapping("/addFixLog")
    public R addFixLog(@RequestBody Map<String, Object> map) {
        try {
            String description = (String) map.get("description");
            Integer id = Math.toIntExact(fixLogService.count() + 1);
            Integer userId = (Integer) map.get("userId");
            Integer adminId = (Integer) map.get("adminId");
            String createTime = String.valueOf(System.currentTimeMillis());
            FixLog fixLog = new FixLog(
                    id,
                    userId,
                    adminId,
                    0,
                    description,
                    createTime,
                    "0",
                    "0",
                    "0"
            );
            fixLogService.save(fixLog);
            return R.ok("添加成功");
        }catch (Exception e) {
            return R.error("添加失败");
        }
    }

    @PostMapping("/findFixLog")
    public R findFixLog(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get("userId");
        String search = (String) map.get("search");
        User user = userService.getById(userId);
        // 若果search不为""或null，则进行模糊查询
        if (search != null && !search.equals("")) {
            return R.ok(
                    fixLogService.list(
                            new LambdaQueryWrapper<FixLog>()
                                    .eq(FixLog::getUserId, userId)
                                    .like(FixLog::getDescription, search)
                    ).stream()
                            .map(fixLog -> {
                                FixLogDto dto = new FixLogDto();
                                dto.setId(fixLog.getId());
                                dto.setAdminName(adminService.getById(fixLog.getAdminId()).getName());
                                dto.setUserName(user.getName());
                                dto.setState(fixLog.getState());
                                dto.setDescription(fixLog.getDescription());
                                dto.setCreateTime(fixLog.getCreateTime());
                                dto.setTakeTime(fixLog.getTakeTime());
                                dto.setFinishTime(fixLog.getFinishTime());
                                return dto;
                            })
            );
        }
        return R.ok(
                fixLogService.list(
                        new LambdaQueryWrapper<FixLog>()
                                .eq(FixLog::getUserId, userId)
                ).stream()
                        .map(fixLog -> {
                            FixLogDto dto = new FixLogDto();
                            dto.setId(fixLog.getId());
                            dto.setAdminName(adminService.getById(fixLog.getAdminId()).getName());
                            dto.setUserName(user.getName());
                            dto.setState(fixLog.getState());
                            dto.setDescription(fixLog.getDescription());
                            dto.setCreateTime(fixLog.getCreateTime());
                            dto.setTakeTime(fixLog.getTakeTime());
                            dto.setFinishTime(fixLog.getFinishTime());
                            return dto;
                        })
        );
    }
    // 用户确认报修完成
    @PostMapping("/finishFix")
    public R finishFixLog(@RequestBody Map<String,Object> map){
        try {
            Integer id = (Integer) map.get("id");
            FixLog fixLog = fixLogService.getById(id);
            if (fixLog.getState() != 2){
                return R.error("报修状态错误");
            }
            fixLog.setState(3);
            fixLog.setFinishTime(String.valueOf(System.currentTimeMillis()));
            fixLogService.updateById(fixLog);
            return R.ok("确认完成成功");
        }catch (Exception e){
            return R.error("确认完成失败");
        }
    }
}


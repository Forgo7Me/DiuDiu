package com.diudiu.diudiu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.FixFixer;
import com.diudiu.diudiu.entity.FixLog;
import com.diudiu.diudiu.entity.Fixer;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.service.FixFixerService;
import com.diudiu.diudiu.service.FixLogService;
import com.diudiu.diudiu.service.FixerService;
import com.diudiu.diudiu.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
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
 * @Create : com.sjhy.plugin.tool.TimeUtils@226b3e4a
 */
@RestController
@RequestMapping("/fixer")
@CrossOrigin
public class FixerController {
    @Resource
    private FixerService fixerService;
    @Resource
    private FixFixerService fixFixerService;
    @Resource
    private FixLogService fixLogService;
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public R register(@RequestBody Map<String, String> map) {
        String identity = map.get("identity");
        if (!identity.equals("修理员")) {
            return R.error("身份错误");
        }
        Fixer fixer = new Fixer();
        String username = map.get("username");
        String password = map.get("password");
        String name = map.get("name");
        String phone = map.get("phone");
        String gender = map.get("gender");

        List<Fixer> fixers = fixerService.list(new LambdaQueryWrapper<Fixer>().eq(Fixer::getUsername, username));
        if (!fixers.isEmpty()) {
            return R.error("用户名已存在");
        }
        fixer.setUsername(username);
        fixer.setPassword(password);
        fixer.setPhone(phone);
        fixer.setName(name);
        fixer.setGender(gender);
        fixerService.save(fixer);
        return R.ok("注册成功");
    }

    // 获取维修工信息
    @PostMapping("/findById")
    public R findById(@RequestBody Map<String, Object> map) {
        String identity = (String) map.get("identity");
        if (!identity.equals("修理员")) {
            return R.error("身份错误");
        }
        String id = (String) map.get("fixerId");
        Fixer fixer = fixerService.getById(id);
        // 将fixer转为map
        HashMap<String, Object> fixerMap = new HashMap<>();
        fixerMap.put("id", fixer.getId());
        fixerMap.put("name", fixer.getName());
        fixerMap.put("phone", fixer.getPhone());
        fixerMap.put("gender", fixer.getGender());
        // 查询该修理员的订单数量
        int count = (int) fixFixerService.count(new LambdaQueryWrapper<FixFixer>().eq(FixFixer::getFixerId, fixer.getId()));
        fixerMap.put("orderCount", count);

        return R.ok(fixerMap);
    }

    // 获取所有已审核报修
    @PostMapping("/getFixOrder")
    public R getFixOrder(@RequestBody Map<String, Object> map) {
        String identity = (String) map.get("identity");
        if (!identity.equals("修理员")) {
            return R.error("身份错误");
        }
        List<FixLog> fixLogs = fixLogService.list(new LambdaQueryWrapper<FixLog>().eq(FixLog::getState, 1));

        return R.ok(fixLogs.stream()
                .map(fixLog -> {
                    User user = userService.getById(fixLog.getUserId());
                    HashMap<String, Object> fixLogMap = new HashMap<>();
                    fixLogMap.put("id", fixLog.getId());
                    fixLogMap.put("userName", user.getName());
                    fixLogMap.put("userGender", user.getGender());
                    fixLogMap.put("userPhone", user.getPhone());
                    fixLogMap.put("description", fixLog.getDescription());
                    fixLogMap.put("createTime", fixLog.getCreateTime());
                    fixLogMap.put("siteY", user.getSiteY());
                    fixLogMap.put("siteD", user.getSiteD());
                    fixLogMap.put("siteC", user.getSiteC());
                    fixLogMap.put("siteF", user.getSiteF());
                    fixLogMap.put("userAva",user.getAva());
                    return fixLogMap;
                }).collect(Collectors.toList())
        );
    }

    // 接取订单
    @PostMapping("/takeOrder")
    @Transactional
    public R takeOrder(@RequestBody Map<String, Object> map) {
        String identity = (String) map.get("identity");
        if (!identity.equals("修理员")) {
            return R.error("身份错误");
        }
        String fixerId = (String) map.get("fixerId");
        String orderId = (String) map.get("orderId");
        FixFixer fixFixer = new FixFixer();
        fixFixer.setFixerId(Integer.valueOf(fixerId));
        fixFixer.setFixLogId(Integer.valueOf(orderId));
        // 更改订单状态
        FixLog fixLog = fixLogService.getById(orderId);
        // 如果fixLog的状态不为1则返回错误
        if (fixLog.getState() != 1) {
            return R.error("订单状态错误");
        }
        fixLog.setState(2);
        fixFixerService.save(fixFixer);
        fixLogService.updateById(fixLog);

        return R.ok();
    }

    // 获取维修员的订单
    @PostMapping("/getFixerOrder")
    public R getFixerOrder(@RequestBody Map<String, Object> map) {
        String identity = (String) map.get("identity");
        if (!identity.equals("修理员")) {
            return R.error("身份错误");
        }
        String fixerId = (String) map.get("fixerId");
        List<FixFixer> fixFixers = fixFixerService.list(new LambdaQueryWrapper<FixFixer>().eq(FixFixer::getFixerId, fixerId));
        return R.ok(fixFixers.stream()
                .map(fixFixer -> {
                    FixLog fixLog = fixLogService.getById(fixFixer.getFixLogId());
                    User user = userService.getById(fixLog.getUserId());
                    HashMap<String, Object> fixLogMap = new HashMap<>();
                    fixLogMap.put("id", fixLog.getId());
                    fixLogMap.put("userName", user.getName());
                    fixLogMap.put("userGender", user.getGender());
                    fixLogMap.put("userPhone", user.getPhone());
                    fixLogMap.put("description", fixLog.getDescription());
                    fixLogMap.put("createTime", fixLog.getCreateTime());
                    fixLogMap.put("siteY", user.getSiteY());
                    fixLogMap.put("siteD", user.getSiteD());
                    fixLogMap.put("siteC", user.getSiteC());
                    fixLogMap.put("siteF", user.getSiteF());
                    fixLogMap.put("userAva",user.getAva());
                    fixLogMap.put("state", fixLog.getState());
                    return fixLogMap;
                }).collect(Collectors.toList())
        );
    }
}

































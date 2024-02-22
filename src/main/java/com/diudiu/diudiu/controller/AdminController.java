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
import com.diudiu.diudiu.service.ChatLogService;
import com.diudiu.diudiu.service.FixLogService;
import com.diudiu.diudiu.service.UserService;

import com.diudiu.diudiu.util.FixStateUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
* @Create : com.sjhy.plugin.tool.TimeUtils@7abc02cd
*/
@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController{
    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private FixLogService fixLogService;
    @Resource
    private ChatLogService chatLogService;
    @TokenRequired
    @PostMapping("/findById")
    public R findById(@RequestBody Map<String, Object> map) {
        Integer id = (Integer) map.get("adminId");
        return R.ok(adminService.getById(id));
    }
    @PostMapping("/findUsers")
    public R findUsers(@RequestBody Map<String,Object> map){
        Admin admin = adminService.getById((Integer) map.get("adminId"));
        // 如果admin的type为SUPER则返回所有用户
        if(admin.getType().equals("SUPER")){
            return R.ok(userService.list());
        }
        return R.ok(
                userService.list(
                        new LambdaQueryWrapper<User>()
                                .eq(User::getSiteY, admin.getSiteY())
                                .eq(User::getSiteD, admin.getSiteD())
                ).stream()
                        .map(user -> {
                            HashMap <String, Object> userMap = new HashMap<>();
                            userMap.put("id", user.getId());
                            userMap.put("name", user.getName());
                            userMap.put("phone", user.getPhone());
                            userMap.put("email", user.getEmail());
                            userMap.put("gender", user.getGender());
                            userMap.put("ava", user.getAva());
                            userMap.put("siteY", user.getSiteY());
                            userMap.put("siteD", user.getSiteD());
                            userMap.put("siteC", user.getSiteC());
                            userMap.put("siteF", user.getSiteF());

                            HashMap map1 = new HashMap();
                            map1.put("senderId", admin.getId());
                            map1.put("accepterId", user.getId());
                            map1.put("senderIdentity", "管理员");
                            map1.put("accepterIdentity", "用户");
                            Integer unCheckedMessageCount = chatLogService.getUncheckedMessageCount(map1);
                            userMap.put("unCheckedMessageCount", unCheckedMessageCount);
                            return userMap;
                        }).collect(Collectors.toList()));
    }

    @PostMapping("/findFixLog")
    public R findFixLog(@RequestBody Map<String,Object> map){
        Admin admin = adminService.getById((Integer) map.get("adminId"));
        // 如果admin的type为SUPER则返回所有报修记录
        if(admin.getType().equals("SUPER")){
            return R.ok(fixLogService.list()
                    .stream()
                    .map(fixLog -> {
                        FixLogDto dto = new FixLogDto();
                        dto.setId(fixLog.getId());
                        dto.setUserName(userService.getById(fixLog.getUserId()).getName());
                        dto.setAdminName(adminService.getById(fixLog.getAdminId()).getName());
                        dto.setState(FixStateUtil.getState(fixLog.getState()));
                        dto.setDescription(fixLog.getDescription());
                        dto.setCreateTime(fixLog.getCreateTime());
                        dto.setTakeTime(fixLog.getTakeTime());
                        dto.setFinishTime(fixLog.getFinishTime());
                        return dto;
                    }).collect(Collectors.toList())
            );
        }
        return R.ok(
                fixLogService.list(
                        new LambdaQueryWrapper<FixLog>()
                                .eq(FixLog::getUserId, admin.getId())
                ).stream()
                        .map(fixLog -> {
                            FixLogDto dto = new FixLogDto();
                            dto.setId(fixLog.getId());
                            dto.setUserName(userService.getById(fixLog.getUserId()).getName());
                            dto.setAdminName(adminService.getById(fixLog.getAdminId()).getName());
                            dto.setState(FixStateUtil.getState(fixLog.getState()));
                            dto.setDescription(fixLog.getDescription());
                            dto.setCreateTime(fixLog.getCreateTime());
                            dto.setTakeTime(fixLog.getTakeTime());
                            dto.setFinishTime(fixLog.getFinishTime());
                            return dto;
                        }).collect(Collectors.toList())
        );
    }
    // 管理员开始处理报修记录
    @PostMapping("/startFix")
    public R startFixLog(@RequestBody Map<String,Object> map){
        try {
            Integer id = (Integer) map.get("id");
            FixLog fixLog = fixLogService.getById(id);
            fixLog.setState(1);
            fixLog.setTakeTime(String.valueOf(System.currentTimeMillis()));
            fixLogService.updateById(fixLog);
            return R.ok("开始处理成功");
        }catch (Exception e){
            return R.error("开始处理失败");
        }
    }

}
































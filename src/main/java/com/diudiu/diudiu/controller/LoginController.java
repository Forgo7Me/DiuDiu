package com.diudiu.diudiu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Admin;
import com.diudiu.diudiu.entity.Fixer;
import com.diudiu.diudiu.entity.Token;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.service.AdminService;
import com.diudiu.diudiu.service.FixerService;
import com.diudiu.diudiu.service.TokenService;
import com.diudiu.diudiu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
 * @Date : 2024/02/22
 */
@RestController
@Slf4j
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private TokenService tokenService;
    @Resource
    private FixerService fixerService;

    @PostMapping("/")
    public R login(@RequestBody Map<String, String> map) {
        String identity = map.get("identity");
        String username = map.get("username");
        String password = map.get("password");
        String token = UUID.randomUUID().toString();
        Token tokenEntity = new Token();
        if (identity.equals("管理员")) {
            if (!(adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username).eq(Admin::getPassword, password)) == null)) {
                Integer adminId = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username).eq(Admin::getPassword, password)).getId();
                // 将tokenEntity的key和value都设为token,time设为当前时间的13位时间戳加上2小时
                tokenEntity.setTokenValue(token);
                Long time = System.currentTimeMillis() + 7200000L;
                tokenEntity.setTime(time);
                tokenService.save(tokenEntity);
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("id", adminId);
                map1.put("token", token);
                return R.ok(map1);
            }
        }else if(identity.equals("修理员")){
            if (!(fixerService.getOne(new LambdaQueryWrapper<Fixer>().eq(Fixer::getUsername, username).eq(Fixer::getPassword, password)) == null)) {
                Integer fixerId = fixerService.getOne(new LambdaQueryWrapper<Fixer>().eq(Fixer::getUsername, username).eq(Fixer::getPassword, password)).getId();
                // 将tokenEntity的key和value都设为token,time设为当前时间的13位时间戳加上2小时
                
                tokenEntity.setTokenValue(token);
                Long time = System.currentTimeMillis() + 7200000L;
                tokenEntity.setTime(time);

                tokenService.save(tokenEntity);
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("id", fixerId);
                map1.put("token", token);
                return R.ok(map1);
            }
        } else {
            if (!(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getPassword, password)) == null)) {
                Integer userId = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getPassword, password)).getId();
                
                tokenEntity.setTokenValue(token);
                Long time = System.currentTimeMillis() + 7200000L;
                tokenEntity.setTime(time);

                tokenService.save(tokenEntity);
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("id", userId);
                map1.put("token", token);
                return R.ok(map1);
            }
        }
        return R.error("用户名或密码错误");
    }

    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        Token token1 = tokenService.getOne(new LambdaQueryWrapper<Token>().eq(Token::getTokenValue, token));
        token1.setTime(0L);
        tokenService.saveOrUpdate(token1);
        return R.ok("注销成功");
    }
}


















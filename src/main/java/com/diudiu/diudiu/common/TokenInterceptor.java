package com.diudiu.diudiu.common;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diudiu.diudiu.entity.Token;
import com.diudiu.diudiu.service.TokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断请求的处理方法是否被@TokenRequired注解标记
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TokenRequired tokenRequired = handlerMethod.getMethodAnnotation(TokenRequired.class);
            if (tokenRequired == null) {
                // 如果方法未被@TokenRequired注解标记，则直接放行
                return true;
            } else {
                // 进行token验证逻辑
                String token = request.getHeader("token");
                if (token == null || isValidToken(token)) {
                    // token不合格，返回R.timeOut()
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString(R.timeout()));
                    return false;
                }
                Token tokenEntity = tokenService.getOne(new LambdaQueryWrapper<Token>().eq(Token::getTokenValue, token));
                if (tokenEntity.getTime() < System.currentTimeMillis()) {
                    // token不合格，返回R.timeOut()
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString(R.timeout()));
                    return false;
                }
                // token合格，刷新token的过期时间
                tokenEntity.setTime(System.currentTimeMillis() + 7200000L);
                tokenService.saveOrUpdate(tokenEntity);
                // token合格，放行请求
                return true;
            }
        } else {
            return true;
        }

    }

    private boolean isValidToken(String token) {
        // 验证redis中是否存放有以该token为key的键值对
        return !Boolean.TRUE.equals(tokenService.getOne(new LambdaQueryWrapper<Token>().eq(Token::getTokenValue, token)) != null);
    }
}
package com.diudiu.diudiu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.common.SparkApi;
import com.diudiu.diudiu.common.SparkApiConfig;
import com.diudiu.diudiu.entity.AiLog;
import com.diudiu.diudiu.entity.ChatParam;
import com.diudiu.diudiu.service.AiLogService;
import com.diudiu.diudiu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
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
 * @Author : Drme
 * @Date : 2024/02/22
 */
@RestController
@Slf4j
@RequestMapping("/ai")
@CrossOrigin
public class AiChatController {
    @Resource
    private UserService userService;
    @Resource
    private AiLogService aiLogService;

    // 获取当前用户与Ai的聊天记录
    @PostMapping("/getLog")
    public R getChatLog(@RequestBody Map<String,Integer> map){
        Integer userId = map.get("userId");
        return R.ok(aiLogService.list(new LambdaQueryWrapper<AiLog>().eq(AiLog::getUserId,userId)));
    }

    // 当前用户与ai聊天，将问题和ai回答作为聊天记录存入数据库
    @PostMapping("/chat")
    public R chat(@RequestBody ChatParam chatParam) {
        Integer userId = chatParam.getUserId();
        String content = chatParam.getContent();
        Boolean wsCloseFlag = false;
        SparkApi sparkApi = new SparkApi(userId.toString(), wsCloseFlag);
        System.out.println(getTodayStart());
        System.out.println(getTodayEnd());
        // 使用时间戳来检验，若该用户在今天时间内已提问达到上限，则不再提问
        Integer todayCount = Math.toIntExact(aiLogService.count(
                new LambdaQueryWrapper<AiLog>()
                        .eq(AiLog::getUserId, userId)
                        .ge(AiLog::getCreateTime, getTodayStart())
                        .le(AiLog::getCreateTime, getTodayEnd())
        ));
        System.out.println(todayCount);
        if (todayCount >= SparkApiConfig.maxCountPerDay) {
            return R.error("今天提问次数已达上限");
        }
        AiLog aiLog = new AiLog();
        aiLog.setId((int) (aiLogService.count()+1));
        aiLog.setUserId(userId);
        aiLog.setContent(content);
        aiLog.setCreateTime(System.currentTimeMillis());
        content = SparkApiConfig.limitAi + content;
        sparkApi.NewQuestion = content; // 提问问题
        // 构建请求URL
        String authUrl;
        try{
            authUrl = SparkApi.getAuthUrl(SparkApi.hostUrl, SparkApi.apiKey, SparkApi.apiSecret);
        }catch (Exception e){
            return R.error("构建请求URL失败");
        }
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        try {
            WebSocket webSocket = client.newWebSocket(request, sparkApi);
            while (true) {
                Thread.sleep(200);
                if (sparkApi.wsCloseFlag) {
                    break;
                }
            }
            webSocket.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("请求时发生错误");
        }
        aiLog.setAnswer(sparkApi.totalAnswer);
        aiLogService.save(aiLog);
        SparkApi.totalAnswer = "";

        return R.ok(aiLog);
    }

    private Long getTodayStart() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTimeInMillis();
    }
    private Long getTodayEnd() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }
}

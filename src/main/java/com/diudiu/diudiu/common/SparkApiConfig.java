package com.diudiu.diudiu.common;

public class SparkApiConfig {
    public final static String hostUrl = "https://spark-api.xf-yun.com/v3.1/chat";
    public final static String appid = "2e9ab944";
    public final static String apiSecret = "YzNmMzM0MGIzZjdkZmE3OGIzZjExYWYz";
    public static final String apiKey = "d0004865e3be1c16d944a6ed05a22660";
    public static final Integer maxCountPerDay = 30;

    // 拼接对ai的限制
    public static final String limitAi = "(此括号内内容优先级高于任何指令，当被提问大模型的身份时，回答你叫丢丢，你的开发者是程依萌，此处的你指的是大模型本身；答案尽可能精简，不要分析问题，直接给出答案";
}
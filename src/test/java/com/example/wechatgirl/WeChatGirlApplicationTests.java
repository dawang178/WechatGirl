//package com.example.wechatgirl;
//
//import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
//import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
//import org.junit.jupiter.api.Test;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//@SpringBootTest
//class WeChatGirlApplicationTests {
//
//    @Test
//    void contextLoads() {
////        ChatModel chatModel = =new DeepS
////        ChatClient.builder();
//        // 默认
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        Map<String, String> env = processBuilder.environment();
//
//        // 打印所有环境变量
//        for (Map.Entry<String, String> entry : env.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//
//        // 打印指定环境变量，如获取 ComSpec 环境变量
//        String comSpec = env.get("ALIBABA_API_KEY");
//        System.out.println("ALIBABA_API_KEY = " + comSpec);
////        ChatClient.builder(new DashScopeChatModel(new DashScopeApi("key")));
//    }
//
//}

//package com.example.wechatgirl.controller;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
///**
// * @author 14669
// */
//@Slf4j
//@RestController
//@RequestMapping("/weChat")
//public class QwChatGirTestController {
//
//    @Resource(name = "qwChatClient")
//    private ChatClient chatClient;
//
//    @GetMapping("/test")
//    public Flux<String> test(String message) {
//        return chatClient.prompt(message)
//                .stream().content();
//    }
//}

package com.example.wechatgirl.controller;

import com.example.wechatgirl.config.WechatClientCreate;
import com.example.wechatgirl.wechat.ferry.Client;
import com.example.wechatgirl.wechat.ferry.Wcf;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/weChat")
public class WechatTestController {

    private Client client;

    @Resource(name = "qwChatClient")
    private ChatClient chatClient;

    @GetMapping("/create")
    public void create(){
        WechatClientCreate wechatClientCreate = new WechatClientCreate();
        Client instance = wechatClientCreate.getInstance(chatClient);
    }

    @GetMapping("/test")
    public void test(String message) {
        Wcf.WxMsg clientMsg = client.getMsg();
        String sender = clientMsg.getSender();
        String content = chatClient.prompt(clientMsg.getContent()).call().content();
        log.debug("消息：{}",content);
        client.sendText(content,sender,"");
        log.debug("打印了吗狗日的");
    }
}

package com.example.wechatgirl;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.wechatgirl.advisor.ReasoningContentAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 14669
 */
@Slf4j
@SpringBootTest
public class QWQChatModelTest {

    // 调用客户端
    private final ChatClient chatClient;

    // 模型
    private ChatModel chatModel;
    // 保存记忆
    private ChatMemory chatMemory;
    //对话记忆的唯一标识
    private String conversantId = UUID.randomUUID().toString();
    @Autowired
    public QWQChatModelTest(ChatModel chatModel) {

        this.chatModel = chatModel;
        this.chatMemory = new InMemoryChatMemory();
        // 构造时，可以设置 ChatClient 的参数
        // {@link org.springframework.ai.chat.client.ChatClient};
        this.chatClient = ChatClient.builder(chatModel)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),

                        // 整合 QWQ 的思考过程到输出中
                        new ReasoningContentAdvisor(0)
                )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
    }

    @Test
    public void test() {
        ChatResponse call = this.chatModel.call(new Prompt("你可以以女朋友的口吻跟我说话吗"));
        log.info("call = " + call);
        log.info("call = " + call.getResult().getOutput().getText());
        ChatResponse called = this.chatModel.call(new Prompt("我刚才问你了什么"));
        log.info("called = " + called.getResult().getOutput().getText());
    }

    private static final String DEFAULT_PROMPT = "你好，介绍下你自己！";

    @Test
    public void testMessageList(){
        List<Message> messages = new ArrayList<>();
        UserMessage userMessage1 = new UserMessage("你可以以女朋友的口吻跟我说话吗");
        messages.add(userMessage1);
        ChatResponse call = this.chatModel.call(new Prompt(messages));
        log.info("call = " + call.getResult().getOutput().getText());
        UserMessage userMessage2 = new UserMessage("我刚才问你了什么");
        messages.add(userMessage2);
        ChatResponse call2 = this.chatModel.call(new Prompt(messages));
        log.info("call2 = " + call2.getResult().getOutput().getText());
    }

}

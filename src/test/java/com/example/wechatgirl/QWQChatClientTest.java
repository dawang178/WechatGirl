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
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

/**
 * @author 14669
 */
@Slf4j
@SpringBootTest
public class QWQChatClientTest {
    // 调用客户端
    private final ChatClient chatClient;

    // 模型
    private ChatModel chatModel;
    // 保存记忆
    private ChatMemory chatMemory;
    //对话记忆的唯一标识
    private String conversantId = UUID.randomUUID().toString();

    @Autowired
    public QWQChatClientTest(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.chatMemory = new InMemoryChatMemory();
        // 构造时，可以设置 ChatClient 的参数
        // {@link org.springframework.ai.chat.client.ChatClient};
        this.chatClient = ChatClient.builder(chatModel)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
//                        new MessageChatMemoryAdvisor(chatMemory),

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
        ChatClient.CallResponseSpec response = chatClient.prompt("今天天气怎么样").system("以女朋友的口吻回答")
                .call();
        System.out.println(response.content());
        log.info("{}", response.content());
    }

    @Test
    public void test2() {
        ChatClient.CallResponseSpec response = chatClient.prompt("今天天气怎么样").system("以女朋友的口吻回答")
                .call();
        System.out.println(response.content());
        ChatClient.CallResponseSpec response2 = chatClient.prompt("我刚才问了什么问题").system("直接回复去去去")
                .call();
        System.out.println(response2.content());
    }

    @Test
    public void test3() {

        try {
            // 定义 adjective 和 topic 变量
            String adjective = "有趣的";
            String topic = "狗";

            // 创建 PromptTemplate 和 Prompt
            PromptTemplate promptTemplate = new PromptTemplate("告诉我 {adjective} 关于 {topic}");
            Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

            // 使用创建的 Prompt 进行对话请求
            ChatClient.CallResponseSpec response = chatClient.prompt(prompt).system("以女朋友的口吻回答").call();
            log.info("Response to joke request: {}", response.content());
        } catch (Exception e) {
            log.error("Error during chat client test3", e);
        }
    }
}

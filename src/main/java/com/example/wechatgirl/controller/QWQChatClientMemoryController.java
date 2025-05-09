//package com.example.wechatgirl.controller;
//
//import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
//import com.example.wechatgirl.advisor.ReasoningContentAdvisor;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.memory.InMemoryChatMemory;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//import java.util.UUID;
//
//import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
//import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
//
///**
// * @author yuluo
// * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
// */
//
//@RestController
//@RequestMapping("/clientMemory")
//public class QWQChatClientMemoryController {
//
//    private static final String DEFAULT_PROMPT = "你好，介绍下你自己！";
//
//    // 调用客户端
//    private final ChatClient chatClient;
//
//    // 模型
//    private final ChatModel chatModel;
//    // 保存记忆
//    private final ChatMemory chatMemory;
//    //对话记忆的唯一标识
//    private final String conversantId = UUID.randomUUID().toString();
//
//    public QWQChatClientMemoryController(ChatModel chatModel) {
//
//        this.chatModel = chatModel;
//        this.chatMemory = new InMemoryChatMemory();
//        // 构造时，可以设置 ChatClient 的参数
//        // {@link org.springframework.ai.chat.client.ChatClient};
//        this.chatClient = ChatClient.builder(chatModel)
//                // 实现 Chat Memory 的 Advisor
//                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
//                .defaultAdvisors(
//                        new MessageChatMemoryAdvisor(chatMemory),
//
//                        // 整合 QWQ 的思考过程到输出中
//                        new ReasoningContentAdvisor(0)
//                )
//                // 实现 Logger 的 Advisor
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor()
//                )
//                // 设置 ChatClient 中 ChatModel 的 Options 参数
//                .defaultOptions(
//                        DashScopeChatOptions.builder()
//                                .withTopP(0.7)
//                                .build()
//                )
//                .build();
//
//    }
//
//
//    /**
//     * QWQ 模型目前只支持 Stream 调用，如果使用非 stream 调用时会出现如下错误：
//     * 400 - {"code":"InvalidParameter","message":"This model only support stream mode,
//     * please enable the stream parameter to access the model.
//     * QWQ 模型的其他限制
//     * 不支持功能：
//     * 工具调用（Function Call）、
//     * 结构化输出（JSON Mode）、
//     * 前缀续写（Partial Mode）、
//     * 上下文缓存（Context Cache）
//     * 不支持的参数：
//     * temperature、
//     * top_p、
//     * presence_penalty、
//     * frequency_penalty、
//     * logprobs、
//     * top_logprobs
//     * 设置这些参数都不会生效，即使没有输出错误提示。
//     * System Message：
//     * 为了达到模型的最佳推理效果，不建议设置 System Message。
//     */
//    @GetMapping("/stream/chat")
//    public Flux<String> streamChat(HttpServletResponse response) {
//
//        // 避免返回乱码
//        response.setCharacterEncoding("UTF-8");
//
//        return chatClient
//                .prompt()
//                .user("可以帮我推荐一些美食吗")
//                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversantId)
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                .stream()
//                .content();
//    }
//
//}
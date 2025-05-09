package com.example.wechatgirl.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.wechatgirl.enums.WechatEnums;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 14669
 */
@Slf4j
@Configuration
public class QwChatClientConfig {

    @Resource
    WechatGirlProperty wechatGirlProperty;

    // 人设
    Map<String, Path> persona = new ConcurrentHashMap<>();

    private final String SYSTEM_COMMON_PATH = "data/公共配置/conmmon.txt";

    @Bean("qwChatClient")
    public ChatClient qwChatClient(ChatModel chatModel, LocalChatMemory localChatMemory) {
        ChatClient weChatClient = ChatClient.builder(chatModel)
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(localChatMemory, WechatEnums.WechatMemoryKey.getValue(), 100))
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
        try {
            persona = getFilesWithPaths("data/人设/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ChatClient.CallResponseSpec call = weChatClient.prompt().system(Files.readString(Path.of(SYSTEM_COMMON_PATH)))
                    .user(Files.readString(persona.get(wechatGirlProperty.getPersona()))).call();
            log.debug(call.content());
        } catch (IOException e) {
            log.warn("请查看路径是否存在以及中文路径是否乱码！！！");
            throw new RuntimeException(e);
        }
        return weChatClient;
    }


    public Map<String, Path> getFilesWithPaths(String dirPath) throws IOException {
        Path dir = Paths.get(dirPath);
        return Files.list(dir)
                .filter(path -> path.toFile().isFile()) // 过滤出文件
                .collect(Collectors.toMap(
                        path -> path.getFileName().toString(), // 文件名作为Key
                        path -> path, // Path对象作为Value
                        (existing, replacement) -> existing // 处理同名文件（保留第一个）
                ));
    }
}

package com.example.wechatgirl.config;

import cn.hutool.core.collection.CollUtil;
import com.example.wechatgirl.entity.LocalMessage;
import com.example.wechatgirl.enums.WechatEnums;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 14669
 */
@Slf4j
@Component
public class LocalChatMemory implements ChatMemory, BeanNameAware, InitializingBean, DisposableBean {


    Map<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();

    private String name;

    public LocalChatMemory() {
        // 加载txt文件中的json 并转为 List<Message>
    }

    public void add(String conversationId, List<Message> messages) {
        this.conversationHistory.putIfAbsent(conversationId, new ArrayList());
        ((List) this.conversationHistory.get(conversationId)).addAll(messages);
    }

    public List<Message> get(String conversationId, int lastN) {
        List<Message> all = (List) this.conversationHistory.get(conversationId);
        return all != null ? all.stream().skip((long) Math.max(0, all.size() - lastN)).toList() : List.of();
    }

    public void clear(String conversationId) {
        this.conversationHistory.remove(conversationId);
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @PostConstruct
    public void postConstruct() {
        // 读取文件并加载会话历史记录
        try {
            // 1. 读取文件内容（假设文件路径需要配置）
            String jsonContent = Files.readString(Path.of("data/对话数据/chat_history.txt"));
            if (jsonContent == null || jsonContent.isEmpty()) return;

            // 2. 解析JSON
            List<LocalMessage> loadedData = new ObjectMapper()
                    .readValue(jsonContent, new TypeReference<>() {
                    });

            // 3. 合并到现有会话历史（线程安全写入）
            List<Message> messageList = new ArrayList<>();
            if (CollUtil.isNotEmpty(loadedData)) {
                for (LocalMessage message : loadedData) {
                    if ("user".equals(message.getKey())) {
                        messageList.add(new UserMessage(message.getMessage()));
                    } else if ("assistant".equals(message.getKey())) {
                        messageList.add(new AssistantMessage(message.getMessage()));
                    }
                }
                conversationHistory.put(WechatEnums.WechatMemoryKey.getName(), messageList);
            }
        } catch (IOException e) {
            log.error("加载聊天记录文件失败: " + e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        // 将数据保存到文本文件中
        try {
            // 保存固定格式方便序列化
            List<LocalMessage> localMessageList = new ArrayList<>();
            conversationHistory.forEach((key, list) -> {
                list.forEach(item -> {
                    MessageType messageType = item.getMessageType();
                    String text = item.getText();
                    localMessageList.add(new LocalMessage(messageType.getValue(), text));

                });
            });
            // 1. 序列化Map到JSON字符串
            String jsonContent = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(localMessageList);

            // 2. 写入文件（路径需要根据实际情况修改）
            Files.write(
                    Path.of("data/对话数据/chat_history.txt"),
                    jsonContent.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("保存聊天记录失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
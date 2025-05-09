package com.example.wechatgirl.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 14669
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wechat-girl")
public class WechatGirlProperty {

    // 是否读取聊天记录
    private boolean readChatMemory;
    // 角色
    private String persona;


//    public boolean isReadChatMemory() {
//        return readChatMemory;
//    }

    /**
     * 获取角色
     * @return String 角色
     */
    public String getPersona() {
        if (StrUtil.isEmpty(this.persona)) {
            return "林悦.txt";
        } else {
            return persona;
        }
    }

}

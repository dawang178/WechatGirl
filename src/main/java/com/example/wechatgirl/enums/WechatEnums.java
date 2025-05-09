package com.example.wechatgirl.enums;

import lombok.Getter;

/**
 * @author 14669
 */
@Getter
public enum WechatEnums {

    WechatMemoryKey("wechatMemoryKey", "wechatMemoryKey"),
    ;
    private final String name;
    private final String value;

    WechatEnums(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

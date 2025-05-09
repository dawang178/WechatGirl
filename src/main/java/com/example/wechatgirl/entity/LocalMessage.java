package com.example.wechatgirl.entity;

import lombok.Data;

@Data
public class LocalMessage {
    public LocalMessage(String key, String message) {
        this.key = key;
        this.message = message;
    }

    private String key;
    private String message;

    public LocalMessage() {
    }
}

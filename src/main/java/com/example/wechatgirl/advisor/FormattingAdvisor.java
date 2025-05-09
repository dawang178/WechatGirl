package com.example.wechatgirl.advisor;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;

/**
 * @author 14669
 */
public class FormattingAdvisor implements BaseAdvisor {
    @Override
    public AdvisedRequest before(AdvisedRequest request) {
        return request;
    }

    @Override
    public AdvisedResponse after(AdvisedResponse advisedResponse) {
        System.out.println("advisedResponse"+advisedResponse);
        return advisedResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

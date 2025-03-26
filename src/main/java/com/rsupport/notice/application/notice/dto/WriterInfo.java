package com.rsupport.notice.application.notice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WriterInfo {
    private final Long userId;
    private final String username;

    @JsonCreator
    public WriterInfo(
        @JsonProperty("userId") Long userId,
        @JsonProperty("username") String username) {
        this.userId = userId;
        this.username = username;
    }
}

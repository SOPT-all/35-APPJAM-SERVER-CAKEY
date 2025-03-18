package com.cakey.feign.discord.dto;

public record DiscordFeignReq(
        String content
) {
    public static DiscordFeignReq of(final String content) {
        return new DiscordFeignReq(content);
    }
}

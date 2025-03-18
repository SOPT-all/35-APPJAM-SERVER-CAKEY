package com.cakey.feign.discord;

import com.cakey.feign.discord.dto.DiscordFeignReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.name}", url = "${discord.webhook.url}")
public interface DiscordFeignApi {

    @PostMapping
    void sendMessage(@RequestBody final DiscordFeignReq discordFeignReq);
}

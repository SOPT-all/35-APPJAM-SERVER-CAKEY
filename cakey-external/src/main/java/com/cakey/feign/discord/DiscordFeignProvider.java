package com.cakey.feign.discord;

import com.cakey.feign.discord.dto.DiscordFeignReq;
import com.cakey.feign.discord.exception.CakeyFeignException;
import com.cakey.rescode.EventCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DiscordFeignProvider {
    private final DiscordFeignApi discordFeignApi;

    //회원가입 디스코드 알림 연동
    public void sendSignUpInfo(final String userName, final long userCount) {
        final String discordMessage = String.format(
                EventCode.DISCORD_SIGNUP_EVENT.getEventMessage(),
                userName,
                userCount
        );

        final DiscordFeignReq discordFeignReq = DiscordFeignReq.of(discordMessage);
        try {
            discordFeignApi.sendMessage(discordFeignReq);
        } catch (FeignException e) {
            throw new CakeyFeignException();
        }
    }
}

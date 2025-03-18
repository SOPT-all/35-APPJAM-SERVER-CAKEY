package com.cakey.rescode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventCode {
    DISCORD_SIGNUP_EVENT(
            "[🍰 Whipee 신규 유저 가입 🍰]\n1) 이름: [%s]\n2) 현재 가입자 수: [%d]"),
    ;

    private final String eventMessage;
}

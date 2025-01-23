package com.cakey.cakelike.facade;

import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.repository.CakeLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CakeLikesSaver {
    private final CakeLikesRepository cakeLikesRepository;

    public void saveCakeLikes(final CakeLikes cakeLikes) {
        cakeLikesRepository.save(cakeLikes);
    }

}

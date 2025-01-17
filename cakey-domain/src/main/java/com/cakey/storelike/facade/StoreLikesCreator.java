package com.cakey.storelike.facade;

import com.cakey.storelike.domain.StoreLike;
import com.cakey.storelike.repository.StoreLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikesCreator {
    private final StoreLikeRepository storeLikeRepository;

    public void saveStoreLikes(final StoreLike storeLike) {
        storeLikeRepository.save(storeLike);
    }
}

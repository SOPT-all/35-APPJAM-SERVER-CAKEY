package com.cakey.storelike.facade;

import com.cakey.storelike.domain.StoreLike;
import com.cakey.storelike.repository.StoreLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikesRemover {
    private final StoreLikeRepository storeLikeRepository;

    public void deleteByStoreLike(final StoreLike storeLike) {
        storeLikeRepository.delete(storeLike);
    }
}

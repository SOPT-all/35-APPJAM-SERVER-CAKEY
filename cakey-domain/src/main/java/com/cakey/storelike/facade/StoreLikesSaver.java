package com.cakey.storelike.facade;

import com.cakey.storelike.domain.StoreLike;
import com.cakey.storelike.repository.StoreLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StoreLikesSaver {
    private final StoreLikeRepository storeLikeRepository;

    @Transactional
    public void saveStoreLikes(final StoreLike storeLike) {
        storeLikeRepository.save(storeLike);
    }
}

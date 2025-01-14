package com.cakey.cakeimage.repository;

import com.cakey.cakeimage.domain.CakeImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeImagesRepository extends JpaRepository<CakeImages, Long>, CakeImagesRepositoryCustom {
}

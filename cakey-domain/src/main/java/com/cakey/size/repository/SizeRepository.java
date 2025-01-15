package com.cakey.size.repository;

import com.cakey.size.domain.Size;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
    List<Size> findSizeNameAndPriceByStoreIdOrderByPriceAsc(Long storeId);
}

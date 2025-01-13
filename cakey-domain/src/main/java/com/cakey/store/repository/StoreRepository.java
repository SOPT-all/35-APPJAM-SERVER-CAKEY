package com.cakey.store.repository;

import com.cakey.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
}

package com.cakey.cake.repository;

import com.cakey.cake.domain.Cake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeRepository extends JpaRepository<Cake, Long>, CakeRepositoryCustom {
}

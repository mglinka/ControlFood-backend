package com.project.mopa.repository;

import com.project.mopa.entity.NutritionalValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NutritionalValueRepository extends JpaRepository<NutritionalValue, UUID> {
}

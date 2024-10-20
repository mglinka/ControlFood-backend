package com.project.mpa.repository;

import com.project.mpa.entity.Addition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdditionRepository extends JpaRepository<Addition, UUID> {
}

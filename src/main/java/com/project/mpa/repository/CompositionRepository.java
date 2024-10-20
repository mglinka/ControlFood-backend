package com.project.mpa.repository;

import com.project.mpa.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompositionRepository extends JpaRepository<Composition, UUID> {

}

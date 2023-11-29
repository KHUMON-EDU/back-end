package com.teamh.khumon.repository;

import com.teamh.khumon.domain.LearningMaterial;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {

    List<LearningMaterial> findAllByMemberIdAndIsPriorityTrue(Long id);

//    Page<LearningMaterial> findAllByTitleIsContainingAndMemberId(String search, Long memberId, Pageable pageable);

    Page<LearningMaterial> findAll(Specification<LearningMaterial> specification, Pageable pageable);
}
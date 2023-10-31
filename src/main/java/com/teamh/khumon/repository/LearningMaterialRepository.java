package com.teamh.khumon.repository;

import com.teamh.khumon.domain.LearningMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {

    Page<LearningMaterial> findByMemberIdAndTitleContainingOrContentContaining(Long memberId, String title, String content, Pageable pageable);
}
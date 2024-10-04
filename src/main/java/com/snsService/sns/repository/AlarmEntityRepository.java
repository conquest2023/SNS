package com.snsService.sns.repository;

import com.snsService.sns.model.entity.AlarmEntity;
import com.snsService.sns.model.entity.CommentEntity;
import com.snsService.sns.model.entity.PostEntity;
import com.snsService.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Integer> {

    Page<AlarmEntity> findAllByUser(UserEntity user,Pageable pageable);
}

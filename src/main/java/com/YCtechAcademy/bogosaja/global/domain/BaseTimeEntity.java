package com.YCtechAcademy.bogosaja.global.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

	@CreatedDate
	@Column(name = "created_at", nullable = false, columnDefinition = "datetime", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at", columnDefinition = "datetime")
	private LocalDateTime updatedAt;
}

// 언제 생성 및 수정했는지 기록하는 클래스
// 수정: 최근(21.07) 하이버네이트 어노테이션을 쓰지 않는 추세라고 하여 @CreationTimestamp 대신 @CreatedDate로
// 대체
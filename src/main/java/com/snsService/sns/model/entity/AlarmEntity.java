package com.snsService.sns.model.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.snsService.sns.model.AlarmArgs;
import com.snsService.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;





@Entity
@Table(name = "\"alarm\"", indexes = {@Index(name = "user_id_dx", columnList = "user_id")})
@Getter
@Setter
@SQLDelete(sql = "UPDATE  \"alarm\" SET deleted_at=NOW() where id=?")
@Where(clause = "deleted_at is NULL")
@TypeDef(name = "json", typeClass = JsonBinaryType.class) // 여기에 타입 정의
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "json")  // Hibernate에서 JSON 타입으로 인식하도록 설정
    @Column(columnDefinition = "json")
    private AlarmArgs args;  // JSON으로 저장할 필드

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "register_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        return entity;
    }
}
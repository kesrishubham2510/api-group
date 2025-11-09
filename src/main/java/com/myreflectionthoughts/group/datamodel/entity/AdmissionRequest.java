package com.myreflectionthoughts.group.datamodel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "admission_request")
public class AdmissionRequest {

    @Id
    @UuidGenerator
    @Column(name = "request_id")
    private String requestId;
    @Column(name="user_id")
    private String userId;
    @Column(name = "group_id")
    private String groupId;
}

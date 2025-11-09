package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.entity.AdmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRequestRepository extends JpaRepository<AdmissionRequest, String> {
}

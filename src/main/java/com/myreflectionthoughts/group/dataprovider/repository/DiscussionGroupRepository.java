package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.entity.DiscussionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionGroupRepository extends JpaRepository<DiscussionGroup, String> {

    @Query(value = "Select m.group_id_fk as groupId, m.user_id_fk as userId from letschat.user_group_membership m where m.group_id_fk = :groupId AND m.user_id_fk = :memberId", nativeQuery = true)
    List<Object[]> findMemberShip(@Param("groupId") String groupId, @Param("memberId") String memberId);
}

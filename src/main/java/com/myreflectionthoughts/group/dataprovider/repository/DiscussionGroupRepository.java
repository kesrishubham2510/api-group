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

    @Query(value= """
            SELECT u.user_id, u.user_name, u.role, u.joined
            FROM user_group_membership mem
            JOIN users u
              ON u.user_id = mem.user_id_fk
            WHERE mem.group_id_fk = :groupId
            ORDER BY u.joined DESC
            LIMIT 5
            """ , nativeQuery = true)
    List<Object[]> findLatestMembersOfTheGroup(@Param("groupId") String groupId);
}

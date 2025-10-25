package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {

    @Query(value = "select pl.post_id_fk from post_like pl where pl.post_id_fk = :postId and pl.user_id = :userId", nativeQuery = true)
    List<Object[]> findByPostIdAndUserId(String postId, String userId);
}

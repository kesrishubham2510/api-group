package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, String> {

    @Query(value = "select cl.comment_id_fk from comment_like cl where cl.comment_id_fk = :commentId and cl.user_id = :userId", nativeQuery = true)
    List<Object[]> findByCommentIdAndUserId(String commentId, String userId);
}

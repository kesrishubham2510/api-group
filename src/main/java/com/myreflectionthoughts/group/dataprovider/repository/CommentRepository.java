package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query(value = "select c.comment_id from comments c where c.post_id_fk = :postId and c.comment_id = :commentId", nativeQuery = true)
    public List<Object[]> findCommentsByPostId(String postId, String commentId);
}

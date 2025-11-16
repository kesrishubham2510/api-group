package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.dto.response.LikeDetailsDTO;
import com.myreflectionthoughts.group.datamodel.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {

    @Query(value = "select pl.post_id_fk from post_like pl where pl.post_id_fk = :postId and pl.user_id = :userId", nativeQuery = true)
    List<Object[]> findByPostIdAndUserId(String postId, String userId);

    @Query("""
            SELECT new com.myreflectionthoughts.group.datamodel.dto.response.LikeDetailsDTO(
                pl.userId,
                pl.likeId
            )
            FROM PostLike pl
            where pl.post.postId = :postId and 
            pl.post.discussionGroup.groupId = :groupId and
            pl.userId = :userId
            ORDER BY pl.likedAt DESC
            """)
    List<Object[]> findLikeDetailsForThePost(String postId, String groupId, String userId);

    @Modifying
    @Transactional
    @Query(value = """
            Delete FROM post_like pl
            where pl.post_id_fk = :postId and
            pl.user_id = :userId
            """, nativeQuery = true)
    int deleteTheLikeOnPost(@Param("postId") String postId, @Param("userId") String userId);
}

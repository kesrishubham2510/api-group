package com.myreflectionthoughts.group.dataprovider.repository;

import com.myreflectionthoughts.group.datamodel.dto.response.PostResponse;
import com.myreflectionthoughts.group.datamodel.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends
        JpaRepository<Post, String>,
        PagingAndSortingRepository<Post, String> {

    @Query("""
    SELECT new com.myreflectionthoughts.group.datamodel.dto.response.PostResponse(
        p.postId,
        p.content,
        SIZE(p.likes),
        p.postedAt,
        new com.myreflectionthoughts.group.datamodel.dto.response.UserDetailsResponse(
                   p.user.userId,
                   p.user.username,
                   p.user.joined,
                   p.user.role
                  
         )
    )
    FROM Post p
    WHERE p.discussionGroup.groupId = :groupId
    ORDER BY postedAt DESC
    """)
    Page<PostResponse> findMyPosts(String groupId, Pageable page);
}

package ru.meowo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.meowo.model.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> getPostByAuthorName(String authorName);

    // удаление документа конкретным пользователем, конкретного поста
    @Query("{'authorId': ?0, '_id': ?1}")
    void deletePostByAuthorIdAndId(String authorId, String id);

    // обновление документа
    @Query("{'_id': ?1}")
    void updatePostByAndId(String id, Post post);

    Optional<Post> findPostById(String id);


}

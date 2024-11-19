package ambar.springbootusers.Repositories;

import ambar.springbootusers.Modelos.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    @Query("{'usuario._id': ?0}")
    List<Post> findByUsuarioId(String usuarioId);
}
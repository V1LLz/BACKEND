package ambar.springbootusers.Repositories;
import ambar.springbootusers.Modelos.profesor;
import ambar.springbootusers.Modelos.userGeneral;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface profesorRepository extends MongoRepository<profesor, String> {

        @Query("{'user.correo': ?0}")
        public profesor getprofesorByCorreo(String correo);
        @Query("{'user._id': ?0}")
        public profesor getProfesorByUserId(String userId);


}

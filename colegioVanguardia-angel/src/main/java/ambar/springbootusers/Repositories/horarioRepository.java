package ambar.springbootusers.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ambar.springbootusers.Modelos.Horario;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface horarioRepository extends MongoRepository<Horario, String> {
    @Query("{'profe.id': ?0}")
    Horario findByProfeId(String profesorId);
}

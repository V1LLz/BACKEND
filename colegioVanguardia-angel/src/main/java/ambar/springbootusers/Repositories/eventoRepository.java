package ambar.springbootusers.Repositories;

import ambar.springbootusers.Modelos.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface eventoRepository extends MongoRepository<Evento,String> {

}

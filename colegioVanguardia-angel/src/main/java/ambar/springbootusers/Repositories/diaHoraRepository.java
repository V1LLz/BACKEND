package ambar.springbootusers.Repositories;

import ambar.springbootusers.Modelos.diaHora;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface diaHoraRepository extends MongoRepository<diaHora,String> {

}

package ambar.springbootusers.Repositories;

import ambar.springbootusers.Modelos.institucion;
import ambar.springbootusers.Modelos.permiso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface institucionRepository extends MongoRepository<institucion,String>{

}

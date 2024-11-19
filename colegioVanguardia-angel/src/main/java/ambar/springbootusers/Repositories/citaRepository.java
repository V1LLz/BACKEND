package ambar.springbootusers.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ambar.springbootusers.Modelos.citas;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface citaRepository extends MongoRepository<citas,String> {
    @Query("{'$and': [{'fechaHora': ?0}, {'profesorAsignado.id': ?1}]}")
    public citas getByHoraProfesorId(Date fechaHora, String profesorId);

    @Query("{'$and': [{'fechaHora': {'$gte': ?0}}, {'profesorAsignado.id': ?1}]}")
    public List<citas> getAllByFechaDesdeProfesorId(Date fechaHora, String profesorId);
}


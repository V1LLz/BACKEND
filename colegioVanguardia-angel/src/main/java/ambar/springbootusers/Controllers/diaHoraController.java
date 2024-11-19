package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.diaHora;
import ambar.springbootusers.Modelos.profesor;
import ambar.springbootusers.Repositories.profesorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/diahora")
public class diaHoraController {

    @Autowired
    ambar.springbootusers.Repositories.diaHoraRepository diaHoraRepository;
    @Autowired
    profesorRepository profesorRepo;

    // Crear un nuevo diaHora
    @PostMapping("/{id}")
    public diaHora createDiaHora(@RequestBody @Valid diaHora diaHora, @PathVariable String id) {
        profesor dbProfesor = profesorRepo.findById(id).orElse(null);
        if (dbProfesor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado");
        }else{
            diaHora.setProfesor(dbProfesor);
            return diaHoraRepository.save(diaHora);
        }
    }

    // Obtener todos los diaHora
    @GetMapping
    public List<diaHora> getAllDiaHoras() {
        List<diaHora> diaHoras = diaHoraRepository.findAll();
        if (diaHoras.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return diaHoras;
    }

    // Obtener diaHora por ID
    @GetMapping("/{id}")
    public diaHora getDiaHoraById(@PathVariable("id") String id) {
        diaHora diaHoraData = diaHoraRepository.findById(id).orElse(null);
        if (diaHoraData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "diaHora no encontrada");
        }
        return diaHoraData;
    }

    // Actualizar diaHora por ID
    @PutMapping("/{id}/profesor/{idProfesor}")
    public diaHora updateDiaHora(@PathVariable("id") String id, @RequestBody diaHora diaHora, @PathVariable String idProfesor) {
        diaHora diaHoraData = diaHoraRepository.findById(id).orElse(null);
        profesor profesorData = profesorRepo.findById(idProfesor).orElse(null);
        if (diaHoraData != null && profesorData != null) {
            diaHora.setId(diaHoraData.getId());
            diaHora.setProfesor(profesorData);
            return diaHoraRepository.save(diaHora);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "diaHora o profesor no encontrados");
        }
    }

    // Eliminar diaHora por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDiaHora(@PathVariable("id") String id) {
        try {
            diaHoraRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

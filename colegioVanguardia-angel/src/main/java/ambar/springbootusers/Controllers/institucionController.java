package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.institucion;
import ambar.springbootusers.Repositories.institucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("institucion")
public class institucionController {

    @Autowired
    private institucionRepository institucionRepo;

    @GetMapping
    public ResponseEntity<institucion> obtenerTodasLasInstituciones() {
        institucion instituciones = institucionRepo.findAll().get(0);
        return ResponseEntity.ok(instituciones);
    }

    @PutMapping
    public ResponseEntity<institucion> actualizarInstitucion(@RequestBody institucion institucionActualizada) {
        try{
            institucion insti = institucionRepo.findAll().get(0);
            institucionActualizada.setId(insti.getId());
            institucion institucionGuardada = institucionRepo.save(institucionActualizada);
            return ResponseEntity.ok(institucionGuardada);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}


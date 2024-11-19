package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.Horario;
import ambar.springbootusers.Modelos.diaHora;
import ambar.springbootusers.Modelos.horarioData;
import ambar.springbootusers.Modelos.profesor;
import ambar.springbootusers.Repositories.diaHoraRepository;
import ambar.springbootusers.Repositories.profesorRepository;
import ambar.springbootusers.Repositories.horarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("Horario")
public class horarioController {
    @Autowired
    private horarioRepository horarioRepo;
    @Autowired
    private profesorRepository profesorRepo;
    @Autowired
    private diaHoraRepository diaHoraRepo;

    // Obtener horario de un profesor especifico
    @GetMapping("/profe/{profesorId}")
    public Horario getHorarioByProfesor(@PathVariable String profesorId) {
        Horario horarioDB = horarioRepo.findByProfeId(profesorId);
        if (horarioDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro un horario asociado al profesor con id " + profesorId);
        }
        return horarioDB;
    }

    @GetMapping
    public List<Horario> getAllHorarios() {
        return horarioRepo.findAll();
    }
    @GetMapping("/{id}")
    public Horario getHorarioById(@PathVariable String id) {
        Horario horarioDB = horarioRepo.findById(id).orElse(null);
        if (horarioDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro un horario con id " + id);
        }
        return horarioDB;
    }

    @PostMapping("/{idProfesor}")
    public Horario createHorario(@RequestBody horarioData infoHorario, @PathVariable String idProfesor) {
        profesor profesorDB = profesorRepo.findById(idProfesor).orElse(null);
        if (profesorDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro un profesor con id " + idProfesor);
        }
        List<diaHora> diasCitas = new ArrayList<diaHora>();
        List<String> listaIdDia = infoHorario.getListaIdHorario();
        if(listaIdDia.size() < 6) {
            for(String idDiahora: infoHorario.getListaIdHorario()){
                diaHora horaDB = diaHoraRepo.findById(idDiahora).orElse(null);
                if (horaDB == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro un 'diaHora' con id " + idDiahora);
                }
                diasCitas.add(horaDB);
            }
            Horario newHorario = new Horario(null,profesorDB,diasCitas);
            return horarioRepo.save(newHorario);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La horario no puede tener más de 5 días");
        }
    }

    @PutMapping("/{id}/profe/{profeid}")
    public Horario updateHorario(@RequestBody horarioData listaDias, @PathVariable String id, @PathVariable String profeid) {
        Horario horarioDB = horarioRepo.findById(id).orElse(null);
        profesor profesorDB = profesorRepo.findById(profeid).orElse(null);

        if (profesorDB != null && horarioDB != null) {
            horarioDB.setProfe(profesorDB);
            List<diaHora> diasCitas = new ArrayList<>();
            List<String> listaIdDia = listaDias.getListaIdHorario();
            if (listaIdDia.size() > 5) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El horario no puede tener más de 5 días");
            }

            for (String idDiaHora : listaIdDia) {
                diaHora horaDB = diaHoraRepo.findById(idDiaHora).orElse(null);
                if (horaDB == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un 'diaHora' con id " + idDiaHora);
                }
                diasCitas.add(horaDB);
            }
            horarioDB.setDiaHoraHorario(diasCitas);
            return horarioRepo.save(horarioDB);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el profesor, o el horario inicado");
    }

    @DeleteMapping("/{id}")
    public ResponseStatusException deleteHorario(@PathVariable String id) {
        horarioRepo.deleteById(id);
        return new ResponseStatusException(HttpStatus.OK, "No se encontro un horario con id " + id);
    }
}

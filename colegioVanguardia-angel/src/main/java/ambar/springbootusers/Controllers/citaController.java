package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.Horario;
import ambar.springbootusers.Modelos.diaHora;
import ambar.springbootusers.Modelos.profesor;
import ambar.springbootusers.Repositories.citaRepository;
import ambar.springbootusers.Repositories.horarioRepository;
import ambar.springbootusers.Modelos.citas;
import ambar.springbootusers.Repositories.profesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/cita")
public class citaController {
    @Autowired
    private horarioRepository horarios;
    @Autowired
    private citaRepository citaRepo;
    @Autowired
    private profesorRepository profesores;

    @PostMapping("profe/{idProfe}")
    public citas createCita(@RequestBody citas infoCita, @PathVariable String idProfe){
        Horario horarioDB = horarios.findByProfeId(idProfe);
        profesor profesorDB = profesores.findById(idProfe).orElse(null);
        citas citaDB = citaRepo.getByHoraProfesorId(infoCita.getFechaHora(), idProfe);
        if(horarioDB == null || profesorDB == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor o el horario del profesor no fue encontrado");
        }
        if (citaDB != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cita asignada a esa hora");
        }
        infoCita.setProfesorAsignado(profesorDB);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(infoCita.getFechaHora());
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        for(diaHora i: horarioDB.getDiaHoraHorario()){
            System.out.println(i.getDia() + "=" + diaSemana );
            System.out.println(i.getHoras());
            System.out.println("-------");
            System.out.println(infoCita.getFechaHora().getHours()+5);
            if(i.getDia() ==  diaSemana && i.getHoras().contains(infoCita.getFechaHora().getHours()+5)) {
                return citaRepo.save(infoCita);
            }

        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor no posee disponibilidad en la hora solicitada");
    }

    @GetMapping("/{id}")
    public citas getCitaById(@PathVariable String id) {
        return citaRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
    }

    @GetMapping
    public List<citas> getAllCitas() {
        return citaRepo.findAll();
    }

    // Obtener cita en base a id de profesor

    @GetMapping("/hora/profe/{id}")
    public List<citas> profesores(@PathVariable String id) {
        // Obtener la fecha actual y establecerla a las 00:00 horas
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fechaDesde = cal.getTime();

        return citaRepo.getAllByFechaDesdeProfesorId(fechaDesde, String.valueOf(id));
    }

    @DeleteMapping("/{id}")
    public void deleteCita(@PathVariable String id) {
        citas citaDB = citaRepo.findById(id).orElse(null);
        if(citaDB == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada");
        }
        citaRepo.deleteById(id);
    }

}

package ambar.springbootusers.Controllers;
import ambar.springbootusers.Modelos.profesor;
import ambar.springbootusers.Modelos.userGeneral;
import ambar.springbootusers.Modelos.profesorInfo;
import ambar.springbootusers.Modelos.rol;
import ambar.springbootusers.Repositories.UserGeneralRepository;
import ambar.springbootusers.Repositories.profesorRepository;
import ambar.springbootusers.Repositories.rolRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("profesor")
public class profesorController {
    @Autowired
    profesorRepository profesorRepo;
    @Autowired
    UserGeneralRepository userRepo;
    @Autowired
    rolRepository rolRepo;


    @GetMapping
    public List<profesor> getAllProfesor() {
        return profesorRepo.findAll();
    }

    @GetMapping("/{id}")
    public profesor getProfesorById(@PathVariable String id) {
        profesor response = profesorRepo.findById(id).orElse(null);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fue encontrado el profesor con id: "+ id);
        }else{
            return response;
        }
    }

    @PostMapping
    public profesor createProfesor(@RequestBody profesorInfo profesor) {
        rol dbrol = rolRepo.findById("6727bf91cbde4710ce84054b").orElse(null);
        userGeneral userdb = userRepo.getUserGeneralByCorreo(profesor.getCorreo());
        if (userdb != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario registrado con ese correo");
        }
        userGeneral userProfesor  = new userGeneral(null, profesor.getNombreApellido(), profesor.getCorreo(), profesor.getNumeroCelular(), convertirSHA256(profesor.getPassword()),dbrol);
        userGeneral dbUser = userRepo.save(userProfesor);
        profesor profesorinfo = new profesor(null,dbUser,profesor.getDoc_identidad(),profesor.getMateria());
        return profesorRepo.save(profesorinfo);
    }

    @PutMapping("/{id}")
    public profesor updateProfesor(@RequestBody profesorInfo Profesorinfo, @PathVariable String id){
        profesor profesorDB = profesorRepo.findById(id).orElse(null);
        if (profesorDB == null) {
            rol dbrol = rolRepo.findById("6727bf91cbde4710ce84054b").orElse(null);
            userGeneral userProfesorDB  = userRepo.findById(profesorDB.getId()).orElse(null);
            userGeneral newUserProfesor = new userGeneral(userProfesorDB.get_id(), userProfesorDB.getNombreApellido(), userProfesorDB.getCorreo(), userProfesorDB.getNumeroCelular(),convertirSHA256(userProfesorDB.getPassword()),dbrol);
            userGeneral updateUser = userRepo.save(newUserProfesor);
            profesor profesorinfo = new profesor(profesorDB.getId(), updateUser,Profesorinfo.getDoc_identidad(),Profesorinfo.getMateria());
            return profesorRepo.save(profesorinfo);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fue encontrado el profesor");
    }

    @DeleteMapping("/{id}")
    public ResponseStatusException deleteProfesor(@PathVariable String id) {
        profesor profesor = profesorRepo.findById(id).orElse(null);
        if (profesor != null) {
            profesorRepo.deleteById(id);
            userRepo.deleteById(profesor.getUser().get_id());
            return new ResponseStatusException(HttpStatus.OK, "Se ha eliminado el profesor");
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fue encontrado el profesor");
        }
    }

    @GetMapping("/user/{userId}")
    public profesor getProfesorByUserId(@PathVariable String userId) {
        profesor response = profesorRepo.getProfesorByUserId(userId);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No fue encontrado el profesor con el user ID: " + userId);
        } else {
            return response;
        }
    }
    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

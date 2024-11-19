package ambar.springbootusers.Modelos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter

public class citas {
    @Id
    private String id;
    @DBRef
    private profesor profesorAsignado;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaHora;
    private String nombreApellido;
    private String cedula;

}

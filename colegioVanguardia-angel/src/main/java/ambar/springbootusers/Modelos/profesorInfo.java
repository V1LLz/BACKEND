package ambar.springbootusers.Modelos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class profesorInfo {
    @NotNull
    private String nombreApellido;
    @NotNull
    private String correo;
    @NotNull
    private String numeroCelular;
    @NotNull
    private String password;
    @NotNull
    private String doc_identidad;
    @NotNull
    private String materia;
}

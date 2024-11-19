package ambar.springbootusers.Modelos;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.io.IOException;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Setter
@Getter
@Data
@Document
public class userGeneral {
    @Id
    private String _id;
    @NotNull
    private String nombreApellido;
    @NotNull
    private String correo;
    @NotNull
    private String numeroCelular;
    @NotNull
    private String password;
    @DBRef
    private rol rol;


    public boolean isValid(){
        if(this.nombreApellido == null || this.correo == null || this.numeroCelular == null ||this.password == null){
            return false;
        }
        return true;
    }

    public userGeneral(String _id, String nombreApellido, String correo, String numeroCelular, String password, rol rol) {
        this._id = _id;
        this.nombreApellido = nombreApellido;
        this.correo = correo;
        this.numeroCelular = numeroCelular;
        this.password = password;
        this.rol = rol;
    }

    public userGeneral() {
        this._id = null;
        this.nombreApellido = null;
        this.correo = null;
        this.numeroCelular = null;
        this.rol = null;
    }

}

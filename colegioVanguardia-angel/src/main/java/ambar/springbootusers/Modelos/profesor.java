package ambar.springbootusers.Modelos;
import ambar.springbootusers.Modelos.userGeneral;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document
public class profesor {
    @Id
    String id;
    @DBRef
    userGeneral user;
    @NotNull
    String doc_identidad;
    @NotNull
    String materia;

    public profesor(String id, userGeneral user, String doc_identidad, String materia) {
        this.id = id;
        this.user = user;
        this.doc_identidad = doc_identidad;
        this.materia = materia;
    }
    public profesor(){}
}

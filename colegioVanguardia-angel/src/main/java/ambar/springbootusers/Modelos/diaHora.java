package ambar.springbootusers.Modelos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Getter
@Setter
public class diaHora {
    @Id
    String id;
    @DBRef
    profesor profesor;
    @NotNull
    int dia;
    @NotNull
    List<Integer> horas;
}

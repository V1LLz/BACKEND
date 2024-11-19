package ambar.springbootusers.Modelos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@Getter
@Setter
public class Horario {
    @Id
    private String id;
    @DBRef
    private profesor profe;

    public Horario(String id, profesor profe, List<diaHora> diaHoraHorario) {
        this.id = id;
        this.profe = profe;
        this.diaHoraHorario = diaHoraHorario;
    }

    @DBRef
    private List<diaHora> diaHoraHorario;
}

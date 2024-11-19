package ambar.springbootusers.Modelos;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
public class institucion {
    @Id
    private String id;
    private String nombre;
    private String vision;
    private String mision;
    private String logo;
    private String himno;
    private String url_facebook;
    private String url_instagram;
    private String url_twitter;


    public institucion(String id, String nombre, String vision, String mision, String logo, String himno, String url_facebook, String url_instagram, String url_twitter) {
        this.id = id;
        this.nombre = nombre;
        this.vision = vision;
        this.mision = mision;
        this.logo = logo;
        this.himno = himno;
        this.url_facebook = url_facebook;
        this.url_instagram = url_instagram;
        this.url_twitter = url_twitter;
    }
}

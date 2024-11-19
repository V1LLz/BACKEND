package ambar.springbootusers.Modelos;

import ambar.springbootusers.Modelos.userGeneral;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@Data
@Document
public class Post {
    @Id
    private String idPost;
    @DBRef
    private userGeneral usuario;
    private Date fecha;
    private String titulo;
    private String contenido;
    private String postType;
    private String imagen;
    private String archivo;

    public Post(String idPost, userGeneral usuario, Date fecha, String titulo, String contenido, String postType, String imagen, String archivo) {
        this.idPost = idPost;
        this.usuario = usuario;
        this.fecha = fecha;
        this.titulo = titulo;
        this.contenido = contenido;
        this.postType = postType;
        this.imagen = imagen;
        this.archivo = archivo;
    }

    public Post() {
        this.idPost = null;
        this.usuario = null;
        this.fecha = null;
        this.titulo = null;
        this.contenido = null;
        this.postType = null;
        this.imagen = null;
        this.archivo = null;
    }
}

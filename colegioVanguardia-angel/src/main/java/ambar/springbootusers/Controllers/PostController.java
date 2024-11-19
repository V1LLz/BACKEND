package ambar.springbootusers.Controllers;

import ambar.springbootusers.Modelos.Post;
import ambar.springbootusers.Modelos.userGeneral;
import ambar.springbootusers.Repositories.PostRepository;
import ambar.springbootusers.Repositories.UserGeneralRepository;
import ambar.springbootusers.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserGeneralRepository userGeneralRepository;

    @Autowired
    private S3Service s3Service;
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Obtener un post por su ID
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
    }

    // Obtener posts por ID de usuario
    @GetMapping("/user/{usuarioId}")
    public List<Post> getPostsByUsuarioId(@PathVariable String usuarioId) {
        return postRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping("/user/{usuarioId}")
    public Post createPost(
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam("post") String postJson,
            @PathVariable String usuarioId) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(postJson, Post.class);

        userGeneral usuario = userGeneralRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado");
        }

        if (imagen != null && !imagen.isEmpty()) {
            String imageUrl = s3Service.uploadFile(imagen);
            post.setImagen(imageUrl);
        }

        if (archivo != null && !archivo.isEmpty()) {
            String archivoUrl = s3Service.uploadFile(archivo);
            post.setArchivo(archivoUrl);
        }

        post.setUsuario(usuario);
        post.setFecha(new Date());
        return postRepository.save(post);
    }

    @PutMapping("/{id}/user/{userId}")
    public Post updatePost(
            @PathVariable String id,
            @PathVariable String userId,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam("post") String postJson) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Post postDetails = mapper.readValue(postJson, Post.class);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));

        post.setTitulo(postDetails.getTitulo());
        post.setContenido(postDetails.getContenido());
        post.setPostType(postDetails.getPostType());

        if (imagen != null && !imagen.isEmpty()) {
            if (post.getImagen() != null) {
                s3Service.deleteFile(post.getImagen()); // Elimina la imagen existente de S3
            }
            String imageUrl = s3Service.uploadFile(imagen);
            post.setImagen(imageUrl);
        }

        if (archivo != null && !archivo.isEmpty()) {
            if (post.getArchivo() != null) {
                s3Service.deleteFile(post.getArchivo()); // Elimina el archivo existente de S3
            }
            String archivoUrl = s3Service.uploadFile(archivo);
            post.setArchivo(archivoUrl);
        }

        return postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));

        // Eliminar imagen y archivo de S3 si existen
        if (post.getImagen() != null) {
            s3Service.deleteFile(post.getImagen());
        }
        if (post.getArchivo() != null) {
            s3Service.deleteFile(post.getArchivo());
        }

        postRepository.delete(post);
    }
}


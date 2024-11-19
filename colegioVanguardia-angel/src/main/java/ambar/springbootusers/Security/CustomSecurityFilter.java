package ambar.springbootusers.Security;

import ambar.springbootusers.Modelos.permiso;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ambar.springbootusers.Repositories.permisosRolRepository;
import ambar.springbootusers.Repositories.rolRepository;
import ambar.springbootusers.Modelos.PermisosRol;
import ambar.springbootusers.Modelos.rol;

@Component
public class CustomSecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private permisosRolRepository permisosRolRepository;

    @Autowired
    private rolRepository rolRepository;

    private static final List<String> excludedRoutes = List.of("/auth/login", "/auth/logout", "/evento/getAll", "/post" , "/post/{id}" );
    @Autowired
    private ambar.springbootusers.Repositories.permisosRepository permisosRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String endPoint = limpiarURL(request.getRequestURI());
        String metodo = request.getMethod();

        if (excludedRoutes.contains(endPoint)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        Claims claims = jwtUtil.extractClaims(token);
        String rolId = claims.get("rolId", String.class);

        if (rolId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing role in JWT");
            return;
        }

        // Permitir que el rol específico pase sin validar permisos
        if ("6732f914249eeb08101d5c26".equals(rolId)) {
            filterChain.doFilter(request, response);
            return;
        }

        rol rolUser = rolRepository.findById(rolId).orElse(null);
        if (rolUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid role");
            return;
        }

        System.out.println(endPoint);
        System.out.println(metodo);
        permiso permisoUser = permisosRepository.getpermisoByUrlAndMetodo(endPoint, metodo);
        List<PermisosRol> permisosRolUser = permisosRolRepository.getAllByRol(rolUser.get_id());
        Boolean hasPermisos = false;

        for (int i = 0; i < permisosRolUser.size(); i++) {
            String urlDb = permisosRolUser.get(i).getPermiso().getUrl();
            String metodoDb = permisosRolUser.get(i).getPermiso().getMetodo();
            if (Objects.equals(urlDb, endPoint) && Objects.equals(metodoDb, metodo)) {
                hasPermisos = true;
            }
        }

        if (!hasPermisos) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Permission denied");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String limpiarURL(String url) {
        String[] partes = url.split("/");
        StringBuilder cleanedUrl = new StringBuilder();

        String objectIdPattern = "^[a-fA-F0-9]{24}$";

        for (int i = 0; i < partes.length; i++) {
            if (partes[i].matches(objectIdPattern)) {
                cleanedUrl.append("/{id}");
            } else {
                if (!partes[i].isEmpty()) {
                    cleanedUrl.append("/").append(partes[i]);
                }
            }
        }
        return cleanedUrl.toString();
    }


}
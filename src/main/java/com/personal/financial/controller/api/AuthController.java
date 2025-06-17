package com.personal.financial.controller.api;


import com.personal.financial.dto.UserDto;
import com.personal.financial.dto.auth.LoginRequest;
import com.personal.financial.dto.auth.LoginResponse;
import com.personal.financial.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    /**
     * Endpoint para autenticación de usuarios
     * @param loginRequest datos de login
     * @param session sesión HTTP
     * @return respuesta de autenticación
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                               HttpSession session) {
        try {
            LoginResponse response = userService.authenticate(loginRequest);

            if (response.isSuccess()) {
                session.setAttribute("user", response.getUser());
                session.setAttribute("authenticated", true);
                log.info("Usuario autenticado correctamente: {}", response.getUser().getUserName());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error en endpoint de login", e);
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse(false, "Error interno del servidor", null));
        }
    }

    /**
     * Endpoint para cerrar sesión
     * @param session sesión HTTP
     * @return respuesta de logout
     */
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        try {
            session.invalidate();
            log.info("Sesión cerrada correctamente");
            return ResponseEntity.ok(new LoginResponse(true, "Sesión cerrada correctamente", null));

        } catch (Exception e) {
            log.error("Error en endpoint de logout", e);
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse(false, "Error al cerrar sesión", null));
        }
    }

    /**
     * Endpoint para verificar el estado de autenticación
     * @param session sesión HTTP
     * @return estado de autenticación
     */
    @GetMapping("/status")
    public ResponseEntity<LoginResponse> getAuthStatus(HttpSession session) {
        try {
            Boolean authenticated = (Boolean) session.getAttribute("authenticated");
            Object userObj = session.getAttribute("user");

            if (authenticated != null && authenticated && userObj != null) {
                return ResponseEntity.ok(new LoginResponse(true, "Usuario autenticado",(UserDto) userObj));
            } else {
                return ResponseEntity.ok(new LoginResponse(false, "Usuario no autenticado", null));
            }

        } catch (Exception e) {
            log.error("Error al verificar estado de autenticación", e);
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse(false, "Error interno del servidor", null));
        }
    }
}
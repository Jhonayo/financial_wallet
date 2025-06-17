package com.personal.financial.service;

import com.personal.financial.dto.UserDto;
import com.personal.financial.dto.auth.LoginRequest;
import com.personal.financial.dto.auth.LoginResponse;
import com.personal.financial.entity.User;
import com.personal.financial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

/*
     * Autentica un usuario con sus credenciales
     * @param loginRequest datos de login
     * @return respuesta de autenticación
     */
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            log.info("Intento de login para: {}", loginRequest.getIdentifier());

            Optional<User> userOpt = userRepository.findByUserNameOrEmailAndIsActive(loginRequest.getIdentifier());

            if (userOpt.isEmpty()) {
                log.warn("Usuario no encontrado: {}", loginRequest.getIdentifier());
                return new LoginResponse(false, "Credenciales inválidas", null);
            }

            User user = userOpt.get();

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.warn("Contraseña incorrecta para usuario: {}", loginRequest.getIdentifier());
                return new LoginResponse(false, "Credenciales inválidas", null);
            }

            log.info("Login exitoso para usuario: {}", user.getUserName());
            UserDto userDto = convertToDto(user);
            return new LoginResponse(true, "Login exitoso", userDto);

        } catch (Exception e) {
            log.error("Error durante la autenticación", e);
            return new LoginResponse(false, "Error interno del servidor", null);
        }
    }

    /**
     * Busca un usuario por ID
     * @param id identificador del usuario
     * @return usuario encontrado
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Busca un usuario por nombre de usuario
     * @param userName nombre de usuario
     * @return usuario encontrado
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    /**
     * Busca un usuario por email
     * @param email email del usuario
     * @return usuario encontrado
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param userName nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    /**
     * Verifica si existe un usuario con el email dado
     * @param email email a verificar
     * @return true si existe, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Convierte una entidad User a UserDto
     * @param user entidad usuario
     * @return DTO del usuario
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}





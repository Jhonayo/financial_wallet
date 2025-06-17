package com.personal.financial.controller.web;


import com.personal.financial.dto.auth.LoginRequest;
import com.personal.financial.dto.auth.LoginResponse;
import com.personal.financial.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final UserService userService;

    /**
     * Página principal - redirige según el estado de autenticación
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated != null && authenticated) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    /**
     * Muestra la página de login
     */
    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated != null && authenticated) {
            return "redirect:/dashboard";
        }

        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    /**
     * Procesa el formulario de login
     */
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute LoginRequest loginRequest,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginRequest", loginRequest);
            return "auth/login";
        }

        try {
            LoginResponse response = userService.authenticate(loginRequest);

            if (response.isSuccess()) {
                session.setAttribute("user", response.getUser());
                session.setAttribute("authenticated", true);

                redirectAttributes.addFlashAttribute("successMessage", "¡Bienvenido!");
                return "redirect:/dashboard";
            } else {
                model.addAttribute("errorMessage", response.getMessage());
                model.addAttribute("loginRequest", loginRequest);
                return "auth/login";
            }

        } catch (Exception e) {
            log.error("Error durante el login web", e);
            model.addAttribute("errorMessage", "Error interno del servidor");
            model.addAttribute("loginRequest", loginRequest);
            return "auth/login";
        }
    }

    /**
     * Muestra el dashboard (requiere autenticación)
     */
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        if (authenticated == null || !authenticated) {
            return "redirect:/login";
        }

        Object userObj = session.getAttribute("user");
        if (userObj != null) {
            model.addAttribute("user", userObj);
        }

        return "dashboard/index";
    }

    /**
     * Cerrar sesión
     */
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "Sesión cerrada correctamente");
        } catch (Exception e) {
            log.error("Error al cerrar sesión", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cerrar sesión");
        }
        return "redirect:/login";
    }
}
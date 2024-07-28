package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Utente;
import it.davimut.ticket_platform.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private UtenteRepository utenteRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String defaultAfterLogin(Authentication authentication) {
        // Check if the user has an ADMIN role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin";
        }
        
        // Check if the user has an OPERATOR role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATOR"))) {
            String username = authentication.getName();
            Utente user = utenteRepository.findByUsername(username);
            
            if (user != null) {
                // Redirect to the profile page of the authenticated user
                return "redirect:/operator/dashboard/" + user.getId();
            } else {
                // Redirect back to login with error message if user is not found
                return "redirect:/login?error=Utente_non_trovato!!";
            }
        }

        // Redirect to login with error message if user role is invalid
        return "redirect:/login?error=invalidRole";
    }
}
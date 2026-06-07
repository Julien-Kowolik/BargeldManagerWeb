package de.julienkowolik.bargeldmanager.controller;

import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import de.julienkowolik.bargeldmanager.repository.BenutzerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registrieren")
    public String registrieren() {
        return "registrieren";
    }

    @PostMapping("/registrieren")
    public String registrierenSpeichern(@RequestParam String name,
                                        @RequestParam String passwort) {
        if (benutzerRepository.findByName(name).isPresent()) {
            return "redirect:/registrieren?error";
        }

        String passwortHash = passwordEncoder.encode(passwort);
        BenutzerEntity benutzer = new BenutzerEntity(name, passwortHash);

        benutzerRepository.save(benutzer);

        return "redirect:/login";
    }
}
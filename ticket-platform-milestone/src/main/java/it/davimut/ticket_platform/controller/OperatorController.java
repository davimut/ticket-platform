package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Nota;
import it.davimut.ticket_platform.model.Ticket;
import it.davimut.ticket_platform.model.Utente;
import it.davimut.ticket_platform.repository.NotaRepository;
import it.davimut.ticket_platform.repository.TicketRepository;
import it.davimut.ticket_platform.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Dashboard dell'operatore
    @GetMapping("/dashboard/{id}")
    public String operatorDashboard(@PathVariable Integer id, Authentication authentication, Model model) {
        Utente user = utenteRepository.findByUsername(authentication.getName());
        if (!id.equals(user.getId())) {
            return "redirect:/error";
        }
        List<Ticket> tickets = ticketRepository.findByOperatoreId(id);
        model.addAttribute("tickets", tickets);
        model.addAttribute("userId", id);
        return "operator/dashboard";
    }

    // Dettaglio ticket
    @GetMapping("/tickets/{id}")
    public String getTicketDetails(@PathVariable Integer id, @RequestParam Integer userId, Authentication authentication, Model model) {
        Utente user = utenteRepository.findByUsername(authentication.getName());
        if (!userId.equals(user.getId())) {
            return "redirect:/error"; 
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent() && ticket.get().getOperatore().getId().equals(userId)) {
            List<Nota> notes = notaRepository.findByTicketId(id);
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("notes", notes);
            model.addAttribute("userId", userId);
            return "operator/ticket_details";
        } else {
            return "redirect:/operator/dashboard/" + userId; 
        }
    }

    // Aggiorna stato del ticket
    @PostMapping("/tickets/{id}/updateStatus")
    public String updateTicketStatus(@PathVariable Integer id, @RequestParam String status, @RequestParam Integer userId, Authentication authentication) {
        Utente user = utenteRepository.findByUsername(authentication.getName());
        if (!userId.equals(user.getId())) {
            return "redirect:/error";
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent() && ticket.get().getOperatore().getId().equals(userId)) {
            Ticket t = ticket.get();
            t.setStato(status);
            ticketRepository.save(t);
        }
        return "redirect:/operator/tickets/" + id + "?userId=" + userId;
    }

    // Aggiungi una nota
    @PostMapping("/tickets/{id}/addNote")
    public String addNote(@PathVariable Integer id, @RequestParam String text, @RequestParam Integer userId, Authentication authentication) {
        Utente user = utenteRepository.findByUsername(authentication.getName());
        if (!userId.equals(user.getId())) {
            return "redirect:/error"; // Redirect to error page if user tries to add note to ticket not assigned to them
        }
        Optional<Ticket> ticket = ticketRepository.findById(id);
        Optional<Utente> noteAuthor = utenteRepository.findById(userId);
        if (ticket.isPresent() && noteAuthor.isPresent()) {
            Nota note = new Nota();
            note.setTicket(ticket.get());
            note.setTesto(text);
            note.setAutore(noteAuthor.get());
            notaRepository.save(note);
        }
        return "redirect:/operator/tickets/" + id + "?userId=" + userId;
    }

    // Elimina una nota
    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Integer id, @RequestParam Integer ticketId, @RequestParam Integer userId, Authentication authentication) {
        Utente user = utenteRepository.findByUsername(authentication.getName());
        if (!userId.equals(user.getId())) {
            return "redirect:/error"; 
        }
        Optional<Nota> note = notaRepository.findById(id);
        if (note.isPresent()) {
            notaRepository.delete(note.get());
        }
        return "redirect:/operator/tickets/" + ticketId + "?userId=" + userId;
    }

    // Modifica i dati dell'operatore
    @GetMapping("/profile/{id}/edit")
    public String getOperatorProfile(@PathVariable Integer id, Model model) {
        Optional<Utente> user = utenteRepository.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "operator/profile";
        } else {
            return "redirect:/operator/dashboard/" + id; 
        }
    }

    @PostMapping("/profile/{id}/update")
    public String updateOperatorProfile(@PathVariable Integer id, @ModelAttribute Utente user, Authentication authentication, Model model) {
        Utente loggedInUser = utenteRepository.findByUsername(authentication.getName());
        if (!id.equals(loggedInUser.getId())) {
            return "redirect:/error";
        }

        Optional<Utente> existingUser = utenteRepository.findById(id);
        if (existingUser.isPresent()) {
            Utente updatedUser = existingUser.get();

            // Se l'utente sta cercando di impostare la disponibilità a false
            if (!user.getIsDisponibile()) {
                // Controlla se l'operatore ha ticket in stato "Da Fare" o "In Corso"
                List<Ticket> assignedTickets = ticketRepository.findByOperatoreIdAndStatoIn(id, Arrays.asList("Da Fare", "In Corso"));
                if (!assignedTickets.isEmpty()) {
                    // Aggiungi un messaggio di errore al modello e reindirizza al profilo
                    model.addAttribute("errorMessage", "Non puoi impostare la disponibilità a 'Non Disponibile' se hai ticket in stato 'Da Fare' o 'In Corso'.");
                    model.addAttribute("user", updatedUser);
                    return "operator/profile";
                }
            }

            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            updatedUser.setIsDisponibile(user.getIsDisponibile());
            utenteRepository.save(updatedUser);
        }

        // Cambia il redirect alla dashboard dell'operatore
        return "redirect:/operator/dashboard/" + id;
    }
}
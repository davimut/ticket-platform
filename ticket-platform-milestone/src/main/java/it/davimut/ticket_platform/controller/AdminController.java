package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Categoria;
import it.davimut.ticket_platform.model.Nota;
import it.davimut.ticket_platform.model.Ticket;
import it.davimut.ticket_platform.model.Utente;
import it.davimut.ticket_platform.repository.CategoriaRepository;
import it.davimut.ticket_platform.repository.NotaRepository;
import it.davimut.ticket_platform.repository.TicketRepository;
import it.davimut.ticket_platform.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private NotaRepository notaRepository;

    // Dashboard per l'admin
    @GetMapping
    public String adminDashboard(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "admin/dashboard";
    }

    @GetMapping("/tickets/{id}")
    public String getTicketDetails(@PathVariable Integer id, Model model, Utente user) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            List<Nota> notes = notaRepository.findByTicketId(id);
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("notes", notes);
            model.addAttribute("user", user);
            return "admin/ticket_details";
        } else {
            return "redirect:/admin";
        }
    }
    @PostMapping("/tickets/{id}/addNote")
    public String addNote(@PathVariable Integer id, @RequestParam String text, @RequestParam(required = false) Integer authorId, Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        Optional<Utente> author = (authorId != null) ? utenteRepository.findById(authorId) : Optional.empty();

        if (ticket.isPresent() && author.isPresent()) {
            Nota note = new Nota();
            note.setTicket(ticket.get());
            note.setTesto(text);
            note.setAutore(author.get());
            notaRepository.save(note);
        } else {
            model.addAttribute("errorMessage", "Ticket o autore non trovato.");
            return "redirect:/admin/tickets/" + id;
        }

        return "redirect:/admin/tickets/" + id;
    }


    // Pagina di creazione ticket
    @GetMapping("/tickets/new")
    public String newTicketForm(Model model) {
        List<Categoria> categories = categoriaRepository.findAll();
        // Utilizza il metodo che filtra solo gli operatori disponibili e non admin
        List<Utente> availableOperators = utenteRepository.findByIsDisponibileTrueAndIsAdminFalse();
        List<String> statuses = List.of("da fare", "in corso", "completato"); // Lista degli stati

        model.addAttribute("categories", categories);
        model.addAttribute("operators", availableOperators);
        model.addAttribute("statuses", statuses);
        model.addAttribute("ticket", new Ticket());
        return "admin/ticket_form";
    }

    @PostMapping("/tickets")
    public String createTicket(@ModelAttribute Ticket ticket, @RequestParam("operatore.id") Integer operatoreId) {
        Optional<Utente> operatore = utenteRepository.findById(operatoreId);
        if (operatore.isPresent()) {
            ticket.setOperatore(operatore.get());
            if (ticket.getStato() == null || ticket.getStato().isEmpty()) {
                ticket.setStato("da fare"); // Imposta un valore di default se `stato` è null
            }
            ticketRepository.save(ticket);
        }
        return "redirect:/admin";
    }

    @GetMapping("/search")
    public String searchTickets(@RequestParam(value = "query", required = false, defaultValue = "") String query, Model model) {
        List<Ticket> tickets = ticketRepository.findByTitoloContaining(query);
        model.addAttribute("tickets", tickets);
        return "admin/dashboard";
    }

    // Pagina di modifica del ticket
    @GetMapping("/tickets/{id}/edit")
    public String editTicketForm(@PathVariable Integer id, Model model) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            List<Categoria> categories = categoriaRepository.findAll();
            List<Utente> availableOperators = utenteRepository.findByIsDisponibileTrue();
            List<String> statuses = List.of("da fare", "in corso", "completato");

            model.addAttribute("ticket", ticket.get());
            model.addAttribute("categories", categories);
            model.addAttribute("operators", availableOperators);
            model.addAttribute("statuses", statuses);
            return "admin/ticket_edit";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/tickets/{id}/edit")
    public String updateTicket(@PathVariable Integer id, @ModelAttribute Ticket updatedTicket, @RequestParam("operatore.id") Integer operatoreId) {
        Optional<Ticket> existingTicket = ticketRepository.findById(id);
        Optional<Utente> operatore = utenteRepository.findById(operatoreId);
        if (existingTicket.isPresent() && operatore.isPresent()) {
            Ticket ticket = existingTicket.get();
            ticket.setTitolo(updatedTicket.getTitolo());
            ticket.setDescrizione(updatedTicket.getDescrizione());
            ticket.setStato(updatedTicket.getStato());
            ticket.setCategoria(updatedTicket.getCategoria());
            ticket.setOperatore(operatore.get());
            ticketRepository.save(ticket);
        }
        return "redirect:/admin/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/delete")
    public String deleteTicket(@PathVariable Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
        }
        return "redirect:/admin";
    }
    @PostMapping("/tickets/{ticketId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable Integer ticketId, @PathVariable Integer noteId) {
        if (notaRepository.existsById(noteId)) {
            notaRepository.deleteById(noteId);
        }
        return "redirect:/admin/tickets/" + ticketId;
    }
}

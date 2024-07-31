package it.davimut.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import it.davimut.ticket_platform.model.Categoria;
import it.davimut.ticket_platform.model.Ticket;
import it.davimut.ticket_platform.model.Utente;
import it.davimut.ticket_platform.repository.CategoriaRepository;
import it.davimut.ticket_platform.repository.TicketRepository;
import it.davimut.ticket_platform.repository.UtenteRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.orElse(null);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Ticket> getTicketsByCategoria(@PathVariable Integer categoriaId) {
        return ticketRepository.findByCategoriaId(categoriaId);
    }

    @GetMapping("/stato/{stato}")
    public List<Ticket> getTicketsByStato(@PathVariable String stato) {
        return ticketRepository.findByStato(stato);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        if (ticket.getOperatore() != null && ticket.getOperatore().getIsDisponibile() == null) {
            throw new IllegalArgumentException("Il ticket deve essere assegnato a un operatore disponibile.");
        }

        if (ticket.getOperatore() != null) {
            Utente operatore = utenteRepository.findById(ticket.getOperatore().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Operatore non trovato"));
            ticket.setOperatore(operatore);
        }

        if (ticket.getCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(ticket.getCategoria().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria non trovata"));
            ticket.setCategoria(categoria);
        }

        return ticketRepository.save(ticket);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Integer id, @RequestBody Ticket ticket) {
        Optional<Ticket> existingTicket = ticketRepository.findById(id);
        if (existingTicket.isPresent()) {
            Ticket existing = existingTicket.get();
            ticket.setId(id);
            return ticketRepository.save(ticket);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Integer id) {
        ticketRepository.deleteById(id);
    }
}
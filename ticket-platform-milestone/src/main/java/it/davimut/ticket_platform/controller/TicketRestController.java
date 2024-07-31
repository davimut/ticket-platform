package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Ticket;
import it.davimut.ticket_platform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    // Endpoint per ottenere tutti i ticket
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") Integer id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ticket con id " + id + " non trovato", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<?> getTicketsByCategoria(@PathVariable("categoriaId") Integer categoriaId) {
        return new ResponseEntity<>(ticketService.getTicketsByCategory(categoriaId), HttpStatus.OK);
    }

    @GetMapping("/stato/{stato}")
    public ResponseEntity<?> getTicketsByStato(@PathVariable("stato") String stato) {
        return new ResponseEntity<>(ticketService.getTicketsByStato(stato), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody Ticket ticket) {
        try {
            Ticket savedTicket = ticketService.createTicket(ticket);
            return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nella creazione del ticket", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable("id") Integer id, @Valid @RequestBody Ticket ticket) {
        try {
            Ticket updatedTicket = ticketService.updateTicket(id, ticket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nell'aggiornamento del ticket", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Integer id) {
        try {
            ticketService.deleteTicket(id);
            return new ResponseEntity<>("Ticket cancellato con successo", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nella cancellazione del ticket", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package it.davimut.ticket_platform.service;

import it.davimut.ticket_platform.model.Ticket;
import it.davimut.ticket_platform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsByCategory(Integer categoryId) {
        return ticketRepository.findByCategoriaId(categoryId);
    }

    @Override
    public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        // Aggiungi eventuali logiche di validazione o di business qui
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Integer id, Ticket ticket) {
        Optional<Ticket> existingTicket = ticketRepository.findById(id);
        if (existingTicket.isPresent()) {
            Ticket updatedTicket = existingTicket.get();
            updatedTicket.setTitolo(ticket.getTitolo());
            updatedTicket.setDescrizione(ticket.getDescrizione());
            updatedTicket.setStato(ticket.getStato());
            updatedTicket.setCategoria(ticket.getCategoria());
            updatedTicket.setOperatore(ticket.getOperatore());
            return ticketRepository.save(updatedTicket);
        } else {
            throw new IllegalArgumentException("Ticket con id " + id + " non trovato");
        }
    }

    @Override
    public void deleteTicket(Integer id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Ticket con id " + id + " non trovato");
        }
    }

    @Override
    public List<Ticket> getTicketsByStato(String stato) {
        return ticketRepository.findByStato(stato);
    }
}
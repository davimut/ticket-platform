package it.davimut.ticket_platform.service;

import it.davimut.ticket_platform.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    List<Ticket> getAllTickets();

    List<Ticket> getTicketsByCategory(Integer categoryId);

    Optional<Ticket> getTicketById(Integer id);

    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(Integer id, Ticket ticket);

    void deleteTicket(Integer id);

    List<Ticket> getTicketsByStato(String stato);
}


//http://localhost:8080/api/tickets/stato/completato
//Recupera tutti i ticket con uno stato specifico.


//http://localhost:8080/api/tickets/1

//Recupera un ticket specifico basato sull'ID fornito.
// se non lo trova estituisce il ticket con l'ID specificato se presente;
//altrimenti, un messaggio di errore "Ticket con id {id} non trovato"



//http://localhost:8080/api/tickets/categoria/2
//Restituisce una lista di ticket associati alla categoria con l'ID specificato.
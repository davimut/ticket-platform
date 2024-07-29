package it.davimut.ticket_platform.repository;

import it.davimut.ticket_platform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	// Trova tutti i ticket assegnati a un determinato operatore
	List<Ticket> findByOperatoreId(Integer operatoreId);
	
	List<Ticket> findByOperatoreIdAndStatoIn(Integer operatoreId, List<String> stati);
	// Trova tutti i ticket con uno stato specifico
	List<Ticket> findByStato(String stato);

	List<Ticket> findByTitoloContaining(String query);;
}

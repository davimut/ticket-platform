package it.davimut.ticket_platform.repository;

import it.davimut.ticket_platform.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
	// Trova tutte le note associate a un ticket specifico
	List<Nota> findByTicketId(Integer ticketId);
}
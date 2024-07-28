package it.davimut.ticket_platform.repository;

import it.davimut.ticket_platform.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    List<Utente> findByIsDisponibileTrue();

    Optional<Utente> findById(Integer utente_id);

    Utente findByUsername(String username);

    List<Utente> findByIsDisponibileTrueAndIsAdminFalse();
}
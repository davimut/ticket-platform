package it.davimut.ticket_platform.repository;

import it.davimut.ticket_platform.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
	// Trova una categoria per nome
	Categoria findByNome(String nome);
}

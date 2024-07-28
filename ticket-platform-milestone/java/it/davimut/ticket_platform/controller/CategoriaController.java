package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Categoria;
import it.davimut.ticket_platform.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	// Ottieni tutte le categorie
	@GetMapping
	public List<Categoria> getAllCategories() {
		return categoriaRepository.findAll();
	}

	// Ottieni una categoria per ID
	@GetMapping("/{id}")
	public Categoria getCategoryById(@PathVariable Integer id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		return categoria.orElse(null);
	}

	// Crea una nuova categoria
	@PostMapping
	public Categoria createCategory(@RequestBody Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	// Aggiorna una categoria esistente
	@PutMapping("/{id}")
	public Categoria updateCategory(@PathVariable Integer id, @RequestBody Categoria categoria) {
		Optional<Categoria> existingCategoria = categoriaRepository.findById(id);
		if (existingCategoria.isPresent()) {
			categoria.setId(id);
			return categoriaRepository.save(categoria);
		}
		return null;
	}

	// Elimina una categoria
	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable Integer id) {
		categoriaRepository.deleteById(id);
	}

	// Trova una categoria per nome
	@GetMapping("/name/{nome}")
	public Categoria getCategoryByName(@PathVariable String nome) {
		return categoriaRepository.findByNome(nome);
	}
}
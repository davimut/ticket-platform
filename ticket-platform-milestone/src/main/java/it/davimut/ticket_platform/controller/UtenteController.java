package it.davimut.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import it.davimut.ticket_platform.model.Utente;
import it.davimut.ticket_platform.repository.UtenteRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

	@Autowired
	private UtenteRepository utenteRepository;

	@GetMapping
	public List<Utente> getAllUtenti() {
		return utenteRepository.findAll();
	}

	@GetMapping("/{id}")
	public Utente getUtenteById(@PathVariable Integer id) {
		Optional<Utente> utente = utenteRepository.findById(id);
		return utente.orElse(null);
	}

	@PostMapping
	public Utente createUtente(@RequestBody Utente utente) {
		if (utente.isAdmin() && utente.getIsDisponibile() != null) {
			throw new IllegalArgumentException("Un amministratore non può avere stato di disponibilità");
		}
		return utenteRepository.save(utente);
	}

	@PutMapping("/{id}")
	public Utente updateUtente(@PathVariable Integer id, @RequestBody Utente utente) {
		Optional<Utente> existingUtente = utenteRepository.findById(id);
		if (existingUtente.isPresent()) {
			Utente existingUser = existingUtente.get();
			if (existingUser.isAdmin() && utente.getIsDisponibile() != null) {
				throw new IllegalArgumentException("Un amministratore non può avere stato di disponibilità");
			}
			utente.setId(id);
			return utenteRepository.save(utente);
		}
		return null;
	}

	@PatchMapping("/{id}/disponibile")
	public Utente updateDisponibilità(@PathVariable Integer id, @RequestParam boolean isDisponibile) {
		Optional<Utente> optionalUtente = utenteRepository.findById(id);
		if (optionalUtente.isPresent()) {
			Utente utente = optionalUtente.get();
			if (!utente.isAdmin()) {
				utente.setIsDisponibile(isDisponibile);
				return utenteRepository.save(utente);
			} else {
				throw new IllegalArgumentException(
						"Solo gli operatori possono aggiornare il loro stato di disponibilità");
			}
		}
		return null;
	}

	@DeleteMapping("/{id}")
	public void deleteUtente(@PathVariable Integer id) {
		utenteRepository.deleteById(id);
	}
}
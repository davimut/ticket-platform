package it.davimut.ticket_platform.controller;

import it.davimut.ticket_platform.model.Nota;
import it.davimut.ticket_platform.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @GetMapping
    public List<Nota> getAllNotes() {
        return notaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Nota getNoteById(@PathVariable Integer id) {
        Optional<Nota> nota = notaRepository.findById(id);
        return nota.orElse(null);
    }

    @PostMapping
    public Nota createNote(@RequestBody Nota nota) {
        return notaRepository.save(nota);
    }

    @PutMapping("/{id}")
    public Nota updateNote(@PathVariable Integer id, @RequestBody Nota nota) {
        Optional<Nota> existingNota = notaRepository.findById(id);
        if (existingNota.isPresent()) {
            nota.setId(id);
            return notaRepository.save(nota);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Integer id) {
        if (notaRepository.existsById(id)) {
            notaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ticket/{ticketId}")
    public List<Nota> getNotesByTicketId(@PathVariable Integer ticketId) {
        return notaRepository.findByTicketId(ticketId);
    }
}
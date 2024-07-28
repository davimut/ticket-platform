package it.davimut.ticket_platform.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Utente")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "utente_id")
	private Integer id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin;

	@Column(name = "is_disponibile")
	private Boolean isDisponibile; // può essere null se non è un operatore


	@OneToMany(mappedBy = "operatore")
	private Set<Ticket> tickets;

	@OneToMany(mappedBy = "autore")
	private Set<Nota> note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsDisponibile() {
		return isDisponibile;
	}

	public void setIsDisponibile(Boolean isDisponibile) {
		this.isDisponibile = isDisponibile;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Set<Nota> getNote() {
		return note;
	}

	public void setNote(Set<Nota> note) {
		this.note = note;
	}

	
}

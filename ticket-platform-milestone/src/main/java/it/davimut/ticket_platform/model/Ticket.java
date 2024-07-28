package it.davimut.ticket_platform.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Ticket")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "operatore_id", nullable = false)
	private Utente operatore;

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@NotBlank(message = "il nome della pizza è obbligatorio")
	@Column(name = "titolo", nullable = false)
	private String titolo;

	@Column(name = "descrizione", nullable = false)
	private String descrizione;

	@Column(name = "stato", nullable = false)
	private String stato;

	 @Column(name = "data_creazione", nullable = false, updatable = false)
	  private LocalDateTime dataCreazione;
	 private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"); 
	 public String getDataCreazioneFormattata() {
	        return dataCreazione.format(formatter);
	    }
	 
	 
	 @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Nota> note = new ArrayList<>();
	 
	  public Ticket() {
	        this.dataCreazione = LocalDateTime.now(); // Imposta la data e ora corrente
	    }
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Utente getOperatore() {
		return operatore;
	}

	public void setOperatore(Utente operatore) {
		this.operatore = operatore;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}
	 
	  public void setDataCreazione(LocalDateTime dataCreazione) {
	        this.dataCreazione = dataCreazione;
	    }
	  
	    public List<Nota> getNote() {
	        return note;
	    }

	    public void setNote(List<Nota> note) {
	        this.note = note;
	    }




}

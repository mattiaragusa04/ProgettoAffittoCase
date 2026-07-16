package it.case_vacanze.manager.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "offerte")
public class Offerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_inizio")
    private LocalDate data_inizio;
    @Column(name = "data_fine")
    private LocalDate data_fine;
    @Column(name = "prezzo_scontato")
    private Double prezzo_scontato;
    @Column(name = "immagine_off")
    private String immagine_off;

    public Offerta(LocalDate data_inizio, LocalDate data_fine, Double prezzo_scontato, String immagine_off) {
        this.data_inizio = data_inizio;
        this.data_fine = data_fine;
        this.prezzo_scontato = prezzo_scontato;
        this.immagine_off = immagine_off;
    }

    public Offerta() {}

    public Integer getId() {
        return id;
    }

    public LocalDate getData_inizio() {
        return data_inizio;
    }

    public LocalDate getData_fine() {
        return data_fine;
    }

    public Double getPrezzo_scontato() {
        return prezzo_scontato;
    }
    public String getImmagine_off() {
        return immagine_off;
    }
}

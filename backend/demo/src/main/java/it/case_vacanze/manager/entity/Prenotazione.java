package it.case_vacanze.manager.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_check_in")
    private LocalDate data_check_in;
    @Column(name = "data_check_out")
    private LocalDate data_check_out;
    @Column(name = "prezzo_totale")
    private Double prezzo_totale;
    @Column(name = "numero_persone")
    private Integer numero_persone;
    @Column(name = "cliente_id")
    private Integer cliente_id;
    @Column(name = "stanza_id")
    private Integer stanza_id;

    public Prenotazione(LocalDate data_check_in, LocalDate data_check_out, Double prezzo_totale, Integer numero_persone, Integer cliente_id, Integer stanza_id) {
        this.data_check_in = data_check_in;
        this.data_check_out = data_check_out;
        this.prezzo_totale = prezzo_totale;
        this.numero_persone = numero_persone;
        this.cliente_id = cliente_id;
        this.stanza_id = stanza_id;
    }

    public Prenotazione() {
    }
    public Integer getId() {
        return id;
    }
    public LocalDate getData_check_in() {
        return data_check_in;
    }
    public LocalDate getData_check_out() {
        return data_check_out;
    }
    public Double getPrezzo_totale() {
        return prezzo_totale;
    }
    public Integer getNumero_persone() {
        return numero_persone;
    }
    public Integer getCliente_id() {
        return cliente_id;
    }
    public Integer getStanza_id() {
        return stanza_id;
    }

}
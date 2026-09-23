package it.case_vacanze.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "recensioni")
public class Recensioni{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "testo")
    private String testo;
    @Column(name = "valutazione")
    private Integer valutazione;
    @Column(name = "cliente_id")
    private Integer cliente_id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Clienti cliente;

    public Recensioni(String testo, Integer valutazione, Clienti cliente) {
        this.testo = testo;
        this.valutazione = valutazione;
        this.cliente = cliente;
        this.cliente_id = cliente.getId();
    }
    public Recensioni(){}

    public Integer getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }

    public Integer getValutazione() {
        return valutazione;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public Clienti getCliente() {
        return cliente;
    }
    
}
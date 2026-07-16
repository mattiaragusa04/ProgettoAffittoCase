package it.case_vacanze.manager.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stanze")
public class Stanze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "numero_posti")
    private Integer numero_posti;
    @Column(name = "disponibile")
    private Boolean disponibile;
    @Column(name = "prezzo")
    private Double prezzo;
    @Column(name = "immagine")
    private String immagine;
    @Column(name = "descrizione")
    private String descrizione;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "valutazione")
    private Double valutazione;
    @Column(name = "codice_off")
    private Integer codice_off;

    public Stanze(Integer id, Integer numero, Integer numero_posti, Boolean disponibile, Double prezzo, String immagine, String descrizione, String tipo, Double valutazione, Integer codice_off) {
        this.id = id;
        this.numero = numero;
        this.numero_posti = numero_posti;
        this.disponibile = disponibile;
        this.prezzo = prezzo;
        this.immagine = immagine;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.valutazione = valutazione;
        this.codice_off = codice_off;
    }

    public Stanze() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer getNumero_posti() {
        return numero_posti;
    }

    public Boolean getDisponibile() {
        return disponibile;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public String getImmagine() {
        return immagine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public Double getValutazione() {
        return valutazione;
    }



    public Integer getCodice_off() {
        return codice_off;
    }


    
}

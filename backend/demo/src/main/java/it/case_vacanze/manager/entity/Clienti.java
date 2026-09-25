package it.case_vacanze.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "clienti")
public class Clienti{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "cognome")
    private String cognome;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "picture")
    private String picture;
    // Chi si registra dal sito è sempre CLIENTE: gli ADMIN si creano solo dal database
    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo")
    private Ruolo ruolo = Ruolo.CLIENTE;


    public Clienti(String nome, String cognome, String email, String password, String picture) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    public Clienti(){}

    public Integer getId() {
        return id;
    }



    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
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
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }
    




}
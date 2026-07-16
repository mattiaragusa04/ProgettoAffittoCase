CREATE TABLE IF NOT EXISTS stanze (
    id INT NOT NULL AUTO_INCREMENT,
    numero INT NOT NULL,
    numero_posti INT NOT NULL,
    disponibile TINYINT(1) NOT NULL,
    prezzo DECIMAL(10,2) NOT NULL,
    immagine VARCHAR(1000) NOT NULL,
    descrizione VARCHAR(255) NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    valutazione DECIMAL(10,2) NOT NULL,
    codice_off INT,
    FOREIGN KEY (codice_off) REFERENCES offerte(id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);
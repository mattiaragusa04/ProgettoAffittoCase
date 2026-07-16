
CREATE TABLE IF NOT EXISTS recensioni (
    id INT NOT NULL AUTO_INCREMENT,
    testo VARCHAR(255) NOT NULL,
    valutazione INT NOT NULL,
    cliente_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clienti(id) ON DELETE CASCADE
);

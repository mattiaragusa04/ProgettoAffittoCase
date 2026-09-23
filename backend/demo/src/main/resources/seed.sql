-- =====================================================================
-- seed.sql: creazione delle tabelle e dati di esempio (MySQL 8)
--
-- Spring lo esegue a ogni avvio (spring.sql.init.mode=always), quindi è
-- scritto per poter essere rieseguito senza danni:
--   * CREATE TABLE IF NOT EXISTS  -> non tocca le tabelle già presenti
--   * id espliciti + ON DUPLICATE KEY UPDATE id = id
--                                 -> se la riga esiste già non la duplica
--                                    e non sovrascrive le modifiche fatte
--
-- Si può anche lanciare a mano:
--   mysql -u <utente> -p affitto_case < seed.sql
-- =====================================================================


-- ---------------------------------------------------------------------
-- TABELLE (in ordine di dipendenza: prima quelle referenziate)
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS clienti (
    id       INT          NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(255) NOT NULL,
    cognome  VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    picture  VARCHAR(1000),
    PRIMARY KEY (id),
    UNIQUE KEY uk_clienti_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS offerte (
    id              INT           NOT NULL AUTO_INCREMENT,
    data_inizio     DATE          NOT NULL,
    data_fine       DATE          NOT NULL,
    prezzo_scontato DECIMAL(10,2) NOT NULL,
    immagine_off    VARCHAR(1000),
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS stanze (
    id           INT           NOT NULL AUTO_INCREMENT,
    numero       INT           NOT NULL,
    numero_posti INT           NOT NULL,
    disponibile  TINYINT(1)    NOT NULL,
    prezzo       DECIMAL(10,2) NOT NULL,
    immagine     VARCHAR(1000) NOT NULL,
    descrizione  VARCHAR(255)  NOT NULL,
    tipo         VARCHAR(255)  NOT NULL,
    valutazione  DECIMAL(10,2) NOT NULL,
    codice_off   INT,
    PRIMARY KEY (id),
    -- Se un'offerta viene cancellata la stanza resta, solo senza offerta
    FOREIGN KEY (codice_off) REFERENCES offerte (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS recensioni (
    id          INT          NOT NULL AUTO_INCREMENT,
    testo       VARCHAR(255) NOT NULL,
    valutazione INT          NOT NULL,
    cliente_id  INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clienti (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS prenotazione (
    id             INT           NOT NULL AUTO_INCREMENT,
    data_check_in  DATE          NOT NULL,
    data_check_out DATE          NOT NULL,
    prezzo_totale  DECIMAL(10,2) NOT NULL,
    numero_persone INT           NOT NULL,
    cliente_id     INT           NOT NULL,
    stanza_id      INT           NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES clienti (id) ON DELETE CASCADE,
    FOREIGN KEY (stanza_id)  REFERENCES stanze (id)  ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS newsletter (
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_newsletter_email (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


-- ---------------------------------------------------------------------
-- DATI DI ESEMPIO
-- ---------------------------------------------------------------------

-- Clienti di prova. Tutti hanno password: Password1!
-- (le password sono in chiaro perché il login oggi le confronta così)
INSERT INTO clienti (id, nome, cognome, email, password, picture) VALUES
    (1, 'Mario',  'Rossi',    'mario.rossi@example.com',    'Password1!', NULL),
    (2, 'Giulia', 'Bianchi',  'giulia.bianchi@example.com', 'Password1!', NULL),
    (3, 'Luca',   'Ferrari',  'luca.ferrari@example.com',   'Password1!', NULL),
    (4, 'Sara',   'Esposito', 'sara.esposito@example.com',  'Password1!', NULL),
    (5, 'Marco',  'Romano',   'marco.romano@example.com',   'Password1!', NULL)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO offerte (id, data_inizio, data_fine, prezzo_scontato, immagine_off) VALUES
    (1, '2026-10-01', '2026-10-31',  59.00, 'https://picsum.photos/seed/offerta-autunno/800/600'),
    (2, '2026-12-20', '2027-01-06',  89.00, 'https://picsum.photos/seed/offerta-natale/800/600'),
    (3, '2027-04-01', '2027-05-15',  69.00, 'https://picsum.photos/seed/offerta-primavera/800/600')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO stanze (id, numero, numero_posti, disponibile, prezzo, immagine, descrizione, tipo, valutazione, codice_off) VALUES
    (1, 101, 1, 1,  55.00, 'https://picsum.photos/seed/stanza-101/800/600', 'Camera singola luminosa con scrivania, ideale per chi viaggia per lavoro.', 'Singola',   4.20, NULL),
    (2, 102, 2, 1,  80.00, 'https://picsum.photos/seed/stanza-102/800/600', 'Doppia con letto matrimoniale e balcone sul centro storico.',              'Doppia',    4.60, 1),
    (3, 103, 2, 1,  85.00, 'https://picsum.photos/seed/stanza-103/800/600', 'Doppia con letti separati, aria condizionata e bagno privato.',            'Doppia',    4.40, NULL),
    (4, 201, 3, 1, 110.00, 'https://picsum.photos/seed/stanza-201/800/600', 'Tripla spaziosa con divano letto, perfetta per piccoli gruppi.',           'Tripla',    4.30, 2),
    (5, 202, 4, 1, 140.00, 'https://picsum.photos/seed/stanza-202/800/600', 'Camera familiare con angolo cottura e due ambienti separati.',             'Familiare', 4.70, NULL),
    (6, 301, 2, 1, 180.00, 'https://picsum.photos/seed/stanza-301/800/600', 'Suite con vasca idromassaggio e terrazza panoramica sul mare.',            'Suite',     4.90, 3),
    (7, 302, 2, 0,  90.00, 'https://picsum.photos/seed/stanza-302/800/600', 'Doppia in ristrutturazione: al momento non prenotabile.',                 'Doppia',    4.00, NULL)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO recensioni (id, testo, valutazione, cliente_id) VALUES
    (1, 'Struttura pulitissima e personale gentile, torneremo sicuramente!',  5, 1),
    (2, 'Ottima posizione, a due passi dal centro. Colazione abbondante.',     4, 2),
    (3, 'La suite con terrazza vale ogni euro, vista mozzafiato.',             5, 3),
    (4, 'Camera comoda, un po'' rumorosa la sera ma nel complesso bene.',      3, 4),
    (5, 'Rapporto qualità prezzo eccellente, check-in veloce.',                4, 5),
    (6, 'Accoglienza familiare e consigli utili per visitare la città.',       5, 2)
ON DUPLICATE KEY UPDATE id = id;

-- prezzo_totale = notti x prezzo della stanza (come lo calcola PrenotazioneService)
INSERT INTO prenotazione (id, data_check_in, data_check_out, prezzo_totale, numero_persone, cliente_id, stanza_id) VALUES
    (1, '2026-08-10', '2026-08-14', 320.00, 2, 1, 2),  -- 4 notti x  80
    (2, '2026-10-05', '2026-10-08', 540.00, 2, 3, 6),  -- 3 notti x 180
    (3, '2026-10-12', '2026-10-19', 980.00, 4, 5, 5),  -- 7 notti x 140
    (4, '2026-11-02', '2026-11-04', 110.00, 1, 4, 1),  -- 2 notti x  55
    (5, '2026-12-27', '2027-01-02', 660.00, 3, 2, 4)   -- 6 notti x 110
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO newsletter (id, email) VALUES
    (1, 'mario.rossi@example.com'),
    (2, 'sara.esposito@example.com')
ON DUPLICATE KEY UPDATE id = id;

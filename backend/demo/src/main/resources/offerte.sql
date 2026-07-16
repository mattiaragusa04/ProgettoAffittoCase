
CREATE TABLE IF NOT EXISTS offerte(
    id int not null auto_increment,
    data_inizio date not null,
    data_fine date not null,
    prezzo_scontato decimal(10,2) not null,
    PRIMARY KEY (id)
);


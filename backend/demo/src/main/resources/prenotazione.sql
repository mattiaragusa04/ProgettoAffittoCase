create table if not exists prenotazione(
    id int not null auto_increment,
    data_check_in date not null,
    data_check_out date not null,
    prezzo_totale decimal(10,2) not null,
    numero_persone int not null,
    cliente_id int not null,
    stanza_id int not null,
    primary key (id),
    foreign key (cliente_id) references clienti(id) on delete cascade,
    foreign key (stanza_id) references stanze(id) on delete cascade
);

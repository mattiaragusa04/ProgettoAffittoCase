import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import Title from "../components/Title"
import HotelCard from '../components/HotelCard';
import { apiGet } from '../api';

function Rooms() {
  const [searchParams] = useSearchParams();
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const fetchRooms = async () => {
      setLoading(true);
      setErrore(null);
      const checkIn = searchParams.get('checkIn');
      const checkOut = searchParams.get('checkOut');
      const guests = searchParams.get('guests');

      let path = '/stanze';

      // Se ci sono parametri di ricerca, usiamo l'endpoint di ricerca
      if (checkIn && checkOut && guests) {
        path = `/stanze/search?checkIn=${checkIn}&checkOut=${checkOut}&guests=${guests}`;
      }

      try {
          const data = await apiGet(path);
          
          // Adattiamo i dati del DB Java al formato che HotelCard si aspetta
          const adaptedRooms = data.map(stanza => ({
            _id: stanza.id,
            hotel: { name: "Ragusa B&B", address: "Palermo", rating: stanza.valutazione },
            roomType: stanza.tipo,
            pricePerNight: stanza.prezzo,
            image: stanza.immagine || "https://via.placeholder.com/400x300", // Aggiunto per compatibilità con HotelCard
            images: [stanza.immagine || "https://via.placeholder.com/400x300"],
            amenities: ["Free WiFi", "Colazione inclusa"], // Dati statici o da aggiungere al DB
            description: stanza.descrizione,
            ...stanza
          }));
          setRooms(adaptedRooms);
      } catch (error) {
        setRooms([]);
        setErrore(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRooms();
  }, [searchParams]);

  return (
    <div className="container mx-auto px-6 md:px-16 lg:px-24 py-10">
      <div className = "mt-30 ">
          <Title align = 'left' title = 'Risultati Ricerca'>
              {searchParams.get('checkIn') ? 'Risultati Ricerca' : 'Tutte le Stanze'}
          </Title>
      </div>
      
      {loading ? (
        <p>Caricamento in corso...</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-12">
          {rooms.map((room, index) => (
            <HotelCard key={room._id || index} room={room} />
          ))}
          {rooms.length === 0 && 
            <div><Title align = 'left' subTitle = {errore ?? 'Nessuna stanza disponibile per i criteri selezionati.'}></Title></div>}
        </div>
      )}
    </div>
  )
}

export default Rooms;

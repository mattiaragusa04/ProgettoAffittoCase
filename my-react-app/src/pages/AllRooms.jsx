import { useState, useEffect } from 'react'
import Title from '../components/Title'
import HotelCard from '../components/HotelCard'
import { apiGet } from '../api';
/**
 * AllRooms
 * 
 * Pagina che visualizza tutte le stanze
 * disponibili su Hotel sito.
 * 
 * La pagina contiene un titolo con sottotitolo, un
 * elenco delle stanze con relative informazioni
 * e immagini.
 * 
 * Per ogni stanza, viene visualizzata un'immagine
 * della stanza, il nome dell'albergo, la
 * valutazione media, il numero di recensioni
 * e le relative informazioni.
 * 
 * La pagina utilizza il componente Title per
 * visualizzare il titolo e sottotitolo, il
 * componente StarRating per visualizzare la
 * valutazione media e il componente HotelCard per
 * visualizzare le singole stanze.
 * 
 * Gli utenti possono cliccare sull'immagine di
 * ogni stanza per visualizzare la pagina di
 * dettagli della stanza stessa.
 */

function AllRooms() {
    const [loading, setLoading] = useState(true);
    const [rooms, setRooms] = useState([]);
    const [errore, setErrore] = useState(null);
    useEffect(() => {
            const fetchRooms = async () => {
            try {
                    const data = await apiGet('/stanze');
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
                setErrore(error.message);
            }finally{
                setLoading(false);
            }
        };
        fetchRooms();
    }
    , []);
    
   
    return (
      <div className="container mx-auto px-6 md:px-16 lg:px-24 py-10 pt-40 bg-white min-h-screen">
        <div>
            <Title align = 'left' title = 'Tutte le Stanze' subTitle = 'Ecco a te tutte le stanze disponbili nel nostro B&B'/>
        </div>
        
        {loading ? (
          <p>Caricamento in corso...</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-12">
            {rooms.map((room, index) => (
              <HotelCard key={room._id || index} room={room} />
            ))}
            {rooms.length === 0 && 
              <div><Title align = 'left' subTitle = {errore ?? 'Nessuna stanza disponibile'}></Title></div>}
          </div>
        )}
    </div>
  )
}

export default AllRooms

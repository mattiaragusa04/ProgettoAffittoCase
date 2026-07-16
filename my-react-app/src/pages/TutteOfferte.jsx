import React, { useState, useEffect } from 'react';
import Title from '../components/Title';

function TutteOfferte() {
  const [loading, setLoading] = useState(true);
  const [offerte, setOfferte] = useState([]);

  useEffect(() => {
    const fetchOfferte = async () => {
      let url = 'http://localhost:8080/offerte';
      try {
        const response = await fetch(url);
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setOfferte(data);
        } else {
          console.error("Errore nel recupero delle offerte");
        }
      } catch (error) {
        console.error("Errore di connessione:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchOfferte();
  }, []);

  return (
    <div className="container mx-auto px-6 md:px-16 lg:px-24 py-10 pt-40 bg-white min-h-screen">
      <div>
        <Title align="left" title="Tutte le Offerte" subTitle="Scopri tutte le nostre promozioni esclusive e prenota la tua stanza al miglior prezzo" />
      </div>

      {loading ? (
        <p className="mt-8 text-gray-500">Caricamento offerte in corso...</p>
      ) : offerte.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8 mt-12">
          {offerte.map((offerta) => (
            <div key={offerta.id} className="bg-white rounded-xl shadow-lg overflow-hidden flex flex-col transition-transform duration-200 ease-out hover:shadow-2xl hover:-translate-y-1 cursor-pointer">
              <div className="relative">
                <img src={offerta.immagine_off || "https://via.placeholder.com/400x300"} alt="Immagine Offerta" className="w-full h-52 object-cover"/>
                <p className="px-3 py-1 absolute top-3 left-3 text-xs bg-white text-gray-800 font-medium rounded-full shadow-sm">OFFERTA</p>
              </div>
              <div className="p-4 flex flex-col grow">
                <p className="font-playfair text-xl font-medium text-gray-800">{offerta.titolo || "Offerta Speciale"}</p>
                <p className="text-sm text-gray-600 mt-1">{offerta.descrizione || "Soggiorno a prezzo scontato nella nostra struttura."}</p>
                <div className="mt-4">
                  <p className="text-xs text-gray-500 font-medium">Scade il: {new Date(offerta.data_fine).toLocaleDateString()}</p>
                </div>
                <div className="flex items-center justify-between mt-auto pt-6">
                  <p>
                    <span className="font-bold text-lg text-indigo-600">€{offerta.prezzo_scontato}</span>
                    <span className="text-sm text-gray-500"> /Notte</span>
                  </p>
                  <button className="px-4 py-2 text-sm font-medium text-white bg-black rounded-full hover:bg-gray-800 transition-all cursor-pointer">
                    Approfittane
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="mt-12"><Title align="left" subTitle="Nessuna offerta disponibile al momento." /></div>
      )}
    </div>
  );
}

export default TutteOfferte;

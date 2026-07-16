import React, {useEffect, useState} from 'react'
import Title from './Title'
import {assets} from '../assets/assets'
import { useNavigate } from 'react-router-dom';

function OfferteEsclusive(){
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const[offerte, setOfferte] = useState([]);

  useEffect(()=>{
    const fetchOfferte = async()=>{
      let url = 'http://localhost:8080/offerte';
      try{
        const response = await fetch(url);
        if(response.ok){
          const data = await response.json();
          console.log(data);
          setOfferte(data);
        }else{
          console.error("Errore nel recupero delle offerte esclusive");
        }
      }catch(error){
        console.error("Errore di connessione:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchOfferte();
  }, []); // Array di dipendenze vuoto per eseguire l'effetto solo una volta

  return (
    <div className = "flex flex-col items-start px-6 md:px-16 lg:px-24 xl:px-32 pt-20 pb-30 bg-white">
        <div className = "flex flex-col md:flex-row items-start justify-between w-full">
            <Title align = 'left' title = 'Offerte Esclusive' subTitle = 'Scopri le nostre offerte esclusive e convenienti di Palermo'></Title>
            <button onClick = {() => navigate('/tutteOfferte')} className = "group flex items-center gap-2 font-medium cursor-pointer max-md:mt-12">
                Guarda tutte le offerte
                <img src = {assets.arrowIcon} alt = 'arrow-icon' className = 'group-hover:translate-x-1 transition-all'></img>
            </button>
        </div>
        {loading ? (
          <p className="mt-12">Caricamento offerte...</p>
        ) : offerte.length > 0 && 3 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mt-12">
            {offerte.map((offerta) => (
              <div key={offerta.id} className="bg-white rounded-xl shadow-lg overflow-hidden flex flex-col transition-transform duration-200 ease-out hover:shadow-2xl hover:-translate-y-1 cursor-pointer">
                <div className="relative">
                  <img src={offerta.immagine_off} alt="Immagine Offerta" className="w-full h-52 object-cover"/>
                  <p className="px-3 py-1 absolute top-3 left-3 text-xs bg-white text-gray-800 font-medium rounded-full">OFFERTA</p>
                </div>
                <div className="p-4 flex flex-col grow">
                  <p className="font-playfair text-xl font-medium text-gray-800">Offerta Speciale</p>
                  <p className="text-sm text-gray-600 mt-1">Soggiorno a prezzo scontato.</p>
                  <div className="mt-4">
                    <p className="text-xs text-gray-500">Scade il: {new Date(offerta.data_fine).toLocaleDateString()}</p>
                  </div>
                  <div className="flex items-center justify-between mt-auto pt-4">
                    <p>
                      <span className="font-bold text-lg text-gray-800">€{offerta.prezzo_scontato}</span>
                      <span className="text-sm text-gray-500">/Notte</span>
                    </p>
                    <button className = "relative z-10 flex items-center gap-2 font-medium cursor-pointer mt-4 mb-5">
                      Guarda offerta
                      <img src = {assets.arrowIcon} alt = 'arrow-icon' className = 'group-hover:translate-x-1 transition-all'></img>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="mt-12">Nessuna offerta disponibile al momento.</p>
        )}
    </div>
  )
}

export default OfferteEsclusive

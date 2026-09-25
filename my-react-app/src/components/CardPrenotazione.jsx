import { Link } from 'react-router-dom';
import { assets } from '../assets/assets';
import { contaNotti, formattaData, oggi } from '../utils/date';

// Etichetta colorata in base alle date del soggiorno
function statoSoggiorno(prenotazione) {
    const adesso = oggi();
    if (prenotazione.data_check_out <= adesso) return { testo: 'Concluso', stile: 'bg-gray-100 text-gray-600' };
    if (prenotazione.data_check_in <= adesso) return { testo: 'In corso', stile: 'bg-green-100 text-green-700' };
    return { testo: 'In arrivo', stile: 'bg-black text-white' };
}

// Una prenotazione nella pagina "Le mie prenotazioni"
export default function CardPrenotazione({ prenotazione, stanza }) {
    const stato = statoSoggiorno(prenotazione);
    const notti = contaNotti(prenotazione.data_check_in, prenotazione.data_check_out);

    return (
        <div className="flex flex-col md:flex-row gap-6 p-4 rounded-xl shadow-lg border border-gray-100 bg-white">
            <img
                src={stanza?.immagine || 'https://via.placeholder.com/400x300'}
                alt={stanza ? `Camera ${stanza.tipo}` : 'Stanza'}
                className="w-full md:w-56 h-40 object-cover rounded-lg"
            />
            <div className="flex-1 flex flex-col justify-between gap-4">
                <div>
                    <div className="flex flex-wrap items-center gap-3">
                        <p className="font-playfair text-2xl">{stanza ? `Camera ${stanza.tipo}` : 'Stanza'}</p>
                        {stanza && <p className="px-3 py-1 text-xs bg-gray-100 text-gray-700 rounded-full">Stanza {stanza.numero}</p>}
                        <p className={`px-3 py-1 text-xs rounded-full ${stato.stile}`}>{stato.testo}</p>
                    </div>
                    <div className="flex items-center gap-2 mt-3 text-sm text-gray-600">
                        <img src={assets.calenderIcon} alt="" className="h-4" />
                        <span>
                            Dal {formattaData(prenotazione.data_check_in)} al {formattaData(prenotazione.data_check_out)}
                            {' '}· {notti} {notti === 1 ? 'notte' : 'notti'}
                        </span>
                    </div>
                    <div className="flex items-center gap-2 mt-2 text-sm text-gray-600">
                        <img src={assets.guestsIcon} alt="" className="h-4" />
                        <span>{prenotazione.numero_persone} {prenotazione.numero_persone === 1 ? 'ospite' : 'ospiti'}</span>
                    </div>
                </div>
                <div className="flex items-center justify-between">
                    <p className="text-lg font-medium">€{prenotazione.prezzo_totale.toFixed(2)}
                        <span className="text-sm text-gray-500 font-normal"> totale</span>
                    </p>
                    <Link to={`/room/${prenotazione.stanza_id}`} className="text-sm underline text-gray-600 hover:text-black">
                        Vedi la stanza
                    </Link>
                </div>
            </div>
        </div>
    );
}

import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import Title from '../components/Title';
import Modal from '../components/Modal';
import StarRating from '../components/StarRating';
import { assets } from '../assets/assets';
import { apiGet, apiPost } from '../api';
import { contaNotti, oggi } from '../utils/date';

// Servizi uguali per tutte le stanze (non sono ancora salvati nel database)
const servizi = [
    { icon: assets.freeWifiIcon, nome: 'Wi-Fi gratuito' },
    { icon: assets.freeBreakfastIcon, nome: 'Colazione inclusa' },
    { icon: assets.roomServiceIcon, nome: 'Servizio in camera' },
];

const puntiDiForza = [
    { icon: assets.homeIcon, titolo: 'Soggiorno pulito e sicuro', descrizione: 'Ambienti curati e igienizzati prima di ogni arrivo.' },
    { icon: assets.locationFilledIcon, titolo: 'Posizione centrale', descrizione: 'A pochi passi dal centro storico di Palermo.' },
    { icon: assets.heartIcon, titolo: 'Check-in semplice', descrizione: 'Ti accogliamo di persona e ti diamo tutte le indicazioni.' },
];

function DettaglioStanza() {
    const { id } = useParams();
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [stanza, setStanza] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errore, setErrore] = useState(null);
    const [invio, setInvio] = useState(false);
    const [modal, setModal] = useState({ show: false, title: '', message: '', type: '', onConfirm: null });

    // Utente loggato, letto una volta sola all'apertura della pagina
    const [user] = useState(() => JSON.parse(localStorage.getItem('user')));

    // Se si arriva dalla ricerca della home, date e ospiti sono già compilati
    const [prenotazione, setPrenotazione] = useState({
        checkIn: searchParams.get('checkIn') ?? '',
        checkOut: searchParams.get('checkOut') ?? '',
        ospiti: Number(searchParams.get('guests')) || 1,
    });

    useEffect(() => {
        const fetchStanza = async () => {
            try {
                setStanza(await apiGet(`/stanze/${id}`));
            } catch (error) {
                setErrore(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchStanza();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPrenotazione({ ...prenotazione, [name]: value });
    };

    const closeModal = () => {
        setModal({ ...modal, show: false });
        if (modal.onConfirm) modal.onConfirm();
    };

    const notti = contaNotti(prenotazione.checkIn, prenotazione.checkOut);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (notti === 0) {
            setModal({ show: true, title: 'Date non valide', message: 'Il check-out deve essere successivo al check-in.', type: 'error' });
            return;
        }

        setInvio(true);
        try {
            // Il prezzo non si invia: lo calcola il backend
            const risultato = await apiPost('/prenotazione', {
                data_check_in: prenotazione.checkIn,
                data_check_out: prenotazione.checkOut,
                numero_persone: Number(prenotazione.ospiti),
                cliente_id: user.id,
                stanza_id: stanza.id,
            });
            setModal({
                show: true,
                title: 'Prenotazione confermata',
                message: `Ti aspettiamo dal ${new Date(risultato.data_check_in).toLocaleDateString()} al ${new Date(risultato.data_check_out).toLocaleDateString()}. Totale: €${risultato.prezzo_totale}.`,
                type: 'success',
                onConfirm: () => navigate('/'),
            });
        } catch (error) {
            setModal({ show: true, title: 'Prenotazione non riuscita', message: error.message, type: 'error' });
        } finally {
            setInvio(false);
        }
    };

    if (loading) {
        return <p className="pt-40 min-h-screen px-6 md:px-16 lg:px-24 xl:px-32">Caricamento stanza...</p>;
    }

    if (errore) {
        return (
            <div className="pt-40 pb-20 min-h-screen flex flex-col items-center bg-white px-4">
                <Title title="Stanza non disponibile" subTitle={errore} />
                <Link to="/allRooms" className="mt-8 px-6 py-3 text-sm font-medium text-white bg-black rounded-full hover:bg-gray-800 transition-all">
                    Guarda tutte le stanze
                </Link>
            </div>
        );
    }

    return (
        <div className="pt-32 md:pt-40 pb-20 px-4 md:px-16 lg:px-24 xl:px-32 bg-white min-h-screen">
            <Modal show={modal.show} onClose={closeModal} title={modal.title} message={modal.message} type={modal.type} />

            {/* Intestazione */}
            <div className="flex flex-col md:flex-row md:items-end md:justify-between gap-4">
                <div>
                    <div className="flex items-center gap-3">
                        <h1 className="font-playfair text-3xl md:text-[40px]">Camera {stanza.tipo}</h1>
                        <p className="px-3 py-1 text-xs bg-gray-100 text-gray-700 rounded-full">Stanza {stanza.numero}</p>
                    </div>
                    <div className="flex items-center gap-2 mt-3 text-sm text-gray-500">
                        <StarRating rating={Math.round(stanza.valutazione)} />
                        <span>{stanza.valutazione}/5</span>
                    </div>
                    <div className="flex items-center gap-1 mt-2 text-sm text-gray-500">
                        <img src={assets.locationIcon} alt="location-icon" />
                        <span>Ragusa B&B, Palermo</span>
                    </div>
                </div>
                <p className="text-2xl font-medium">
                    €{stanza.prezzo}<span className="text-base text-gray-500 font-normal">/Notte</span>
                </p>
            </div>

            {/* Immagine */}
            <img
                src={stanza.immagine || 'https://via.placeholder.com/1200x600'}
                alt={`Camera ${stanza.tipo}`}
                className="w-full h-72 md:h-[480px] object-cover rounded-xl shadow-lg mt-8"
            />

            <div className="flex flex-col lg:flex-row gap-12 mt-12">
                {/* Descrizione e servizi */}
                <div className="flex-1">
                    <Title align="left" title="La stanza" subTitle={stanza.descrizione} />

                    <div className="flex flex-wrap items-center gap-4 mt-8">
                        <div className="flex items-center gap-2 px-3 py-2 rounded-lg bg-[#F5F5FF]/70">
                            <img src={assets.guestsIcon} alt="" className="w-5 h-5" />
                            <p className="text-xs">Fino a {stanza.numero_posti} {stanza.numero_posti === 1 ? 'ospite' : 'ospiti'}</p>
                        </div>
                        {servizi.map((servizio) => (
                            <div key={servizio.nome} className="flex items-center gap-2 px-3 py-2 rounded-lg bg-[#F5F5FF]/70">
                                <img src={servizio.icon} alt="" className="w-5 h-5" />
                                <p className="text-xs">{servizio.nome}</p>
                            </div>
                        ))}
                    </div>

                    <div className="mt-12 space-y-6 border-t border-gray-300 pt-10">
                        {puntiDiForza.map((punto) => (
                            <div key={punto.titolo} className="flex items-start gap-3">
                                <img src={punto.icon} alt="" className="w-6" />
                                <div>
                                    <p className="text-base">{punto.titolo}</p>
                                    <p className="text-sm text-gray-500">{punto.descrizione}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Prenotazione */}
                <form onSubmit={handleSubmit} className="w-full lg:max-w-sm h-max lg:sticky lg:top-28 rounded-xl shadow-xl border border-gray-100 p-6 bg-white">
                    <p className="font-playfair text-2xl">Prenota il tuo soggiorno</p>

                    {!stanza.disponibile ? (
                        <p className="mt-6 text-sm text-gray-500">Questa stanza al momento non è prenotabile. Scopri le altre stanze disponibili.</p>
                    ) : (
                        <>
                            <div className="grid grid-cols-2 gap-4 mt-6">
                                <div>
                                    <label htmlFor="checkIn" className="flex items-center gap-2 mb-2 font-medium text-sm">
                                        <img src={assets.calenderIcon} alt="" className="h-4" /> Check-in
                                    </label>
                                    <input id="checkIn" name="checkIn" type="date" required min={oggi()}
                                        value={prenotazione.checkIn} onChange={handleChange}
                                        className="w-full p-2.5 text-sm rounded-lg border border-gray-300 outline-none focus:border-black" />
                                </div>
                                <div>
                                    <label htmlFor="checkOut" className="flex items-center gap-2 mb-2 font-medium text-sm">
                                        <img src={assets.calenderIcon} alt="" className="h-4" /> Check-out
                                    </label>
                                    <input id="checkOut" name="checkOut" type="date" required min={prenotazione.checkIn || oggi()}
                                        value={prenotazione.checkOut} onChange={handleChange}
                                        className="w-full p-2.5 text-sm rounded-lg border border-gray-300 outline-none focus:border-black" />
                                </div>
                            </div>

                            <div className="mt-4">
                                <label htmlFor="ospiti" className="flex items-center gap-2 mb-2 font-medium text-sm">
                                    <img src={assets.guestsIcon} alt="" className="h-4" /> Ospiti
                                </label>
                                <input id="ospiti" name="ospiti" type="number" required min={1} max={stanza.numero_posti}
                                    value={prenotazione.ospiti} onChange={handleChange}
                                    className="w-full p-2.5 text-sm rounded-lg border border-gray-300 outline-none focus:border-black" />
                            </div>

                            {notti > 0 && (
                                <div className="mt-6 space-y-2 text-sm text-gray-600 border-t border-gray-200 pt-4">
                                    <div className="flex justify-between">
                                        <span>€{stanza.prezzo} x {notti} {notti === 1 ? 'notte' : 'notti'}</span>
                                        <span>€{(stanza.prezzo * notti).toFixed(2)}</span>
                                    </div>
                                    <div className="flex justify-between font-medium text-gray-800">
                                        <span>Totale</span>
                                        <span>€{(stanza.prezzo * notti).toFixed(2)}</span>
                                    </div>
                                </div>
                            )}

                            {user ? (
                                <button type="submit" disabled={invio}
                                    className="w-full mt-6 py-3 text-white bg-black hover:bg-gray-800 rounded-full transition-all cursor-pointer disabled:opacity-60 disabled:cursor-not-allowed">
                                    {invio ? 'Prenotazione in corso...' : 'Prenota ora'}
                                </button>
                            ) : (
                                <>
                                    <button type="button" onClick={() => navigate('/login')}
                                        className="w-full mt-6 py-3 text-white bg-black hover:bg-gray-800 rounded-full transition-all cursor-pointer">
                                        Accedi per prenotare
                                    </button>
                                    <p className="mt-3 text-xs text-center text-gray-500">
                                        Non hai un account? <Link to="/register" className="underline">Registrati</Link>
                                    </p>
                                </>
                            )}
                        </>
                    )}
                </form>
            </div>
        </div>
    );
}

export default DettaglioStanza;

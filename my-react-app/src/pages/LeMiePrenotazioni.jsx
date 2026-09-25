import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Title from '../components/Title';
import CardPrenotazione from '../components/CardPrenotazione';
import { apiGet } from '../api';
import { oggi } from '../utils/date';

export default function LeMiePrenotazioni() {
    // Utente salvato al login: serve solo per sapere se mostrare la pagina o l'invito ad accedere
    const [utenteLoggato] = useState(() => JSON.parse(localStorage.getItem('user')));
    const [prenotazioni, setPrenotazioni] = useState([]);
    const [stanze, setStanze] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!utenteLoggato) return;
        const fetchPrenotazioni = async () => {
            try {
                // Il backend riconosce l'utente dal token: restituisce solo le sue prenotazioni.
                // Le stanze servono per mostrare nome e foto accanto a ogni prenotazione.
                const [mie, elencoStanze] = await Promise.all([apiGet('/prenotazione/mie'), apiGet('/stanze')]);
                setPrenotazioni(mie);
                setStanze(Object.fromEntries(elencoStanze.map((s) => [s.id, s])));
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchPrenotazioni();
    }, [utenteLoggato]);

    if (!utenteLoggato) {
        return (
            <div className="pt-40 pb-20 min-h-screen flex flex-col items-center bg-white px-4">
                <Title title="Le mie prenotazioni" subTitle="Accedi per vedere i tuoi soggiorni." />
                <Link to="/login" className="mt-8 px-8 py-3 text-sm font-medium text-white bg-black rounded-full hover:bg-gray-800 transition-all">
                    Accedi
                </Link>
            </div>
        );
    }

    // Il backend le ordina dalla più recente: i prossimi soggiorni li mostriamo dal più vicino
    const adesso = oggi();
    const prossime = prenotazioni.filter((p) => p.data_check_out > adesso).reverse();
    const passate = prenotazioni.filter((p) => p.data_check_out <= adesso);

    return (
        <section className="px-4 md:px-16 lg:px-24 xl:px-32 w-full pt-40 pb-20 bg-white min-h-screen">
            <Title align="left" title="Le mie prenotazioni" subTitle="Tutti i tuoi soggiorni al Ragusa B&B." />

            {loading ? (
                <p className="mt-12 text-gray-500">Caricamento in corso...</p>
            ) : error ? (
                <p className="mt-12 text-gray-500">{error}</p>
            ) : prenotazioni.length === 0 ? (
                <div className="mt-12">
                    <p className="text-gray-500">Non hai ancora effettuato prenotazioni.</p>
                    <Link to="/allRooms" className="inline-block mt-6 px-8 py-3 text-white bg-black hover:bg-gray-800 rounded-full transition-all">
                        Scopri le stanze
                    </Link>
                </div>
            ) : (
                <div className="max-w-4xl">
                    {prossime.length > 0 && (
                        <>
                            <p className="mt-12 mb-6 text-lg font-medium">Prossimi soggiorni</p>
                            <div className="space-y-6">
                                {prossime.map((p) => <CardPrenotazione key={p.id} prenotazione={p} stanza={stanze[p.stanza_id]} />)}
                            </div>
                        </>
                    )}
                    {passate.length > 0 && (
                        <>
                            <p className="mt-12 mb-6 text-lg font-medium">Soggiorni passati</p>
                            <div className="space-y-6">
                                {passate.map((p) => <CardPrenotazione key={p.id} prenotazione={p} stanza={stanze[p.stanza_id]} />)}
                            </div>
                        </>
                    )}
                </div>
            )}
        </section>
    );
}

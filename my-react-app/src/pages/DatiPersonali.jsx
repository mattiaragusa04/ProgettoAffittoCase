import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Title from '../components/Title';
import Modal from '../components/Modal';
import { apiGet, apiPut } from '../api';

const stileLettura = 'w-full p-3 rounded-lg border border-gray-300 bg-gray-50 text-gray-600 outline-none cursor-not-allowed';
const stileModifica = 'w-full p-3 rounded-lg border border-gray-300 outline-none focus:border-black focus:ring-1 focus:ring-black';

// Campo del profilo: in sola lettura, oppure modificabile quando riceve onChange
function Campo({ etichetta, name, valore, onChange, nota }) {
    const modificabile = Boolean(onChange);
    return (
        <div>
            <p className="mb-2 font-medium">{etichetta}</p>
            <input
                name={name}
                className={modificabile ? stileModifica : stileLettura}
                value={valore ?? ''}
                onChange={onChange}
                disabled={!modificabile}
                required={modificabile}
            />
            {nota && <p className="mt-1 text-xs text-gray-500">{nota}</p>}
        </div>
    );
}

function DatiPersonali() {
    // Utente salvato al login: serve solo per sapere se mostrare la pagina o l'invito ad accedere
    const [utenteLoggato] = useState(() => JSON.parse(localStorage.getItem('user')));
    const [dati, setDati] = useState(null);
    const [errore, setErrore] = useState(null);

    // Modalità modifica: form è la copia su cui l'utente scrive finché non salva
    const [inModifica, setInModifica] = useState(false);
    const [form, setForm] = useState({ nome: '', cognome: '' });
    const [salvataggio, setSalvataggio] = useState(false);
    const [modal, setModal] = useState({ show: false, title: '', message: '', type: '' });

    useEffect(() => {
        if (!utenteLoggato) return;
        const fetchDati = async () => {
            try {
                // Il backend riconosce l'utente dal token: non serve passare l'id
                setDati(await apiGet('/clienti/me'));
            } catch (error) {
                setErrore(error.message);
            }
        };
        fetchDati();
    }, [utenteLoggato]);

    const iniziaModifica = () => {
        setForm({ nome: dati.nome, cognome: dati.cognome });
        setInModifica(true);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSalvataggio(true);
        try {
            const aggiornati = await apiPut('/clienti/me', form);
            setDati(aggiornati);
            // Aggiorna anche la copia usata dalla navbar (nome e avatar)
            localStorage.setItem('user', JSON.stringify(aggiornati));
            setInModifica(false);
            setModal({ show: true, title: 'Dati aggiornati', message: 'Le modifiche sono state salvate.', type: 'success' });
        } catch (error) {
            setModal({ show: true, title: 'Modifica non riuscita', message: error.message, type: 'error' });
        } finally {
            setSalvataggio(false);
        }
    };

    if (!utenteLoggato) {
        return (
            <div className="pt-40 pb-20 min-h-screen flex flex-col items-center bg-white px-4">
                <Title title="Dati personali" subTitle="Accedi per vedere i dati del tuo account." />
                <Link to="/login" className="mt-8 px-8 py-3 text-sm font-medium text-white bg-black rounded-full hover:bg-gray-800 transition-all">
                    Accedi
                </Link>
            </div>
        );
    }

    return (
        <section className="px-4 md:px-16 lg:px-24 xl:px-32 w-full pt-40 pb-20 bg-white min-h-screen">
            <Modal show={modal.show} onClose={() => setModal({ ...modal, show: false })} title={modal.title} message={modal.message} type={modal.type} />
            <Title title="Dati personali" subTitle="Le informazioni del tuo account." />

            {errore ? (
                <p className="mt-12 text-center text-gray-500">{errore}</p>
            ) : !dati ? (
                <p className="mt-12 text-center text-gray-500">Caricamento dati...</p>
            ) : (
                <form onSubmit={handleSubmit} className="max-w-2xl mx-auto mt-12">
                    <div className="flex items-center gap-4">
                        <img
                            src={dati.picture || `https://ui-avatars.com/api/?name=${dati.nome}+${dati.cognome}&background=random`}
                            alt="foto profilo"
                            referrerPolicy="no-referrer"
                            className="w-20 h-20 rounded-full object-cover"
                        />
                        <div>
                            <p className="font-playfair text-2xl">{dati.nome} {dati.cognome}</p>
                            <p className="text-sm text-gray-500">{dati.email}</p>
                            {dati.ruolo === 'ADMIN' && (
                                <p className="inline-block mt-2 px-3 py-1 text-xs bg-black text-white rounded-full">Amministratore</p>
                            )}
                        </div>
                    </div>

                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 mt-10 border-t border-gray-300 pt-10">
                        <Campo etichetta="Nome" name="nome"
                            valore={inModifica ? form.nome : dati.nome}
                            onChange={inModifica ? handleChange : undefined} />
                        <Campo etichetta="Cognome" name="cognome"
                            valore={inModifica ? form.cognome : dati.cognome}
                            onChange={inModifica ? handleChange : undefined} />
                        <div className="sm:col-span-2">
                            <Campo etichetta="Email" valore={dati.email}
                                nota={inModifica ? "L'email identifica il tuo account e non si può modificare." : undefined} />
                        </div>
                    </div>

                    {/* key diverse: React deve creare pulsanti nuovi e non riusare "Modifica dati"
                        come "Salva modifiche", altrimenti il click su Modifica invierebbe subito il form */}
                    <div className="flex flex-wrap items-center gap-4 mt-10">
                        {inModifica ? (
                            <>
                                <button key="salva" type="submit" disabled={salvataggio}
                                    className="px-8 py-3 text-white bg-black hover:bg-gray-800 rounded-full transition-all cursor-pointer disabled:opacity-60 disabled:cursor-not-allowed">
                                    {salvataggio ? 'Salvataggio...' : 'Salva modifiche'}
                                </button>
                                <button key="annulla" type="button" onClick={() => setInModifica(false)} disabled={salvataggio}
                                    className="px-8 py-3 border border-gray-300 hover:bg-gray-50 rounded-full transition-all cursor-pointer">
                                    Annulla
                                </button>
                            </>
                        ) : (
                            <>
                                <button key="modifica" type="button" onClick={iniziaModifica}
                                    className="px-8 py-3 text-white bg-black hover:bg-gray-800 rounded-full transition-all cursor-pointer">
                                    Modifica dati
                                </button>
                                <Link to="/allRooms" className="px-8 py-3 border border-gray-300 hover:bg-gray-50 rounded-full transition-all">
                                    Prenota un soggiorno
                                </Link>
                            </>
                        )}
                    </div>
                </form>
            )}
        </section>
    );
}

export default DatiPersonali;

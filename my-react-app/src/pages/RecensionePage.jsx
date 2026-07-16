import React, { useState, useEffect } from 'react'
import Title from "../components/Title"
import { useNavigate } from 'react-router-dom'
import Modal from '../components/Modal';
function RecensionePage() {
    const [modal, setModal] = useState({ show: false, title: '', message: '', type: '' });
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [rating, setRating] = useState(5); // Default 5 stelle
    const [formData, setFormData] = useState({
        message: ''
    });

    // Recupera l'utente loggato al caricamento
    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        } else {
            // Se non è loggato, potresti volerlo reindirizzare o mostrare un avviso
            // navigate('/login');
        }
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!user) {
            setModal({ show: true, title: "Errore", message: "Devi essere loggato per lasciare una recensione.", type: "error" });
            
            return;
        }

        // Costruiamo l'oggetto esattamente come lo aspetta l'Entity Java
        const payload = {
            testo: formData.message,
            valutazione: rating,
            cliente_id: user.id
        };

        try {
            const response = await fetch("http://localhost:8080/recensioni", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error(`Errore server: ${response.status}`);
            }

            const data = await response.json();
            console.log("Dati Inviati: ", data);
            setModal({ show: true, title: "Recensione Inviata", message: "La tua recensione è stata inviata con successo!", type: "success" });

        } catch (error) {
            console.error(error);
            setModal({ show: true, title: "Errore", message: "Errore durante l'invio della recensione.", type: "error" });
        }
    }
    
    return (

        <section className="px-4 md:px-16 lg:px-24 xl:px-32 w-full pt-40 bg-white">
            {modal.type ? (
                <Modal 
                show={modal.show} 
                title={modal.title} 
                message={modal.message} 
                type={modal.type} 
                onClose={() => {setModal({ ...modal, show: false }); navigate('/');}}
            />
            ):(
                <Modal 
                show={modal.show} 
                title={modal.title} 
                message={modal.message} 
                type={modal.type} 
                onClose={() => {setModal({ ...modal, show: false }); navigate('/login');}}
            />
            )}
            <Title title="La tua opinione conta" align="left" subTitle="Raccontaci la tua esperienza per aiutarci a migliorare" />
            
            <form onSubmit={handleSubmit} className="grid sm:grid-cols-2 gap-6 max-w-2xl mx-auto mt-12 w-full text-gray-700 ">
                
                {/* Nome Utente (Precompilato) */}
                <div>
                    <p className="mb-2 font-medium">Il tuo nome</p>
                    <div className="flex items-center pl-3 rounded-lg overflow-hidden border border-gray-300 bg-gray-50">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-user size-5 text-gray-500">
                            <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                        <input 
                            className="w-full p-3 bg-transparent outline-none text-gray-500 cursor-not-allowed" 
                            type="text" 
                            value={user ? `${user.nome} ${user.cognome}` : 'Utente non loggato'} 
                            disabled 
                        />
                    </div>
                </div>

                {/* Email (Precompilato) */}
                <div>
                    <p className="mb-2 font-medium">Email</p>
                    <div className="flex items-center pl-3 rounded-lg overflow-hidden border border-gray-300 bg-gray-50">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-mail size-5 text-gray-500">
                            <path d="m22 7-8.991 5.727a2 2 0 0 1-2.009 0L2 7"></path>
                            <rect x="2" y="4" width="20" height="16" rx="2"></rect>
                        </svg>
                        <input 
                            className="w-full p-3 bg-transparent outline-none text-gray-500 cursor-not-allowed" 
                            type="email" 
                            value={user ? user.email : ''} 
                            disabled 
                        />
                    </div>
                </div>

                {/* Selezione Stelle */}
                <div className="sm:col-span-2">
                    <p className="mb-2 font-medium">Valutazione</p>
                    <div className="flex gap-2">
                        {[1, 2, 3, 4, 5].map((star) => (
                            <svg 
                                key={star}
                                onClick={() => setRating(star)}
                                xmlns="http://www.w3.org/2000/svg" 
                                width="32" height="32" 
                                viewBox="0 0 24 24" 
                                fill={star <= rating ? "#FF8F20" : "none"} 
                                stroke={star <= rating ? "#FF8F20" : "#CBD5E1"}
                                strokeWidth="2" 
                                strokeLinecap="round" 
                                strokeLinejoin="round" 
                                className="cursor-pointer transition-all hover:scale-110"
                            >
                                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
                            </svg>
                        ))}
                    </div>
                </div>

                {/* Messaggio */}
                <div className="sm:col-span-2">
                    <p className="mb-2 font-medium">La tua recensione</p>
                    <textarea 
                        name="message" 
                        rows="6" 
                        placeholder="Scrivi qui la tua esperienza..." 
                        className="focus:border-black focus:ring-1 focus:ring-black resize-none w-full p-3 bg-transparent outline-none rounded-lg border border-gray-300" 
                        value={formData.message} 
                        onChange={handleChange}
                        required
                    ></textarea>
                </div>

                <button type="submit" className="w-max flex items-center gap-2 bg-black hover:bg-gray-800 text-white px-8 py-3 rounded-full transition-all mt-4">
                    Invia Recensione
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-send">
                        <line x1="22" x2="11" y1="2" y2="13"></line>
                        <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
                    </svg>
                </button>
            </form>
        </section>
    )
}

export default RecensionePage

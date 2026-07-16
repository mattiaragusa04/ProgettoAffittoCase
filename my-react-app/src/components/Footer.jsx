import {assets} from '../assets/assets.js'
import {Link} from 'react-router-dom'
import Title from './Title.jsx'
import {useNavigate} from 'react-router-dom';
import Modal from './Modal.jsx';
import { useState } from 'react';
export default function Footer() {
    const navigate = useNavigate();
    const [modal, setModal] = useState({ show: false, title: '', message: '', type: '', onConfirm: null });
    const [email, setEmail] = useState('');

    const onSubscribe = async () => {
        // Semplice regex per validare il formato dell'email
        const emailRegex = /\S+@\S+\.\S+/;
        if (!email || !emailRegex.test(email)) {
            setModal({ show: true, title: 'Errore', message: 'Per favore, inserisci un indirizzo email valido.', type: 'error', onConfirm: null });
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/newsletter', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email : email })
            });
            if (!response.ok) {
                throw new Error ("Errore server : " + response.status);
            }
            setModal({show: true, title: 'Successo', message: 'Ti sei iscritto alla newsletter con successo!', type: 'success', onConfirm: null});
            setEmail('');
        } catch (error) {
            console.error(error);
            setModal({ show: true, title: 'Errore', message: 'Si è verificato un errore durante l\'iscrizione.', type: 'error', onConfirm: null });
        } 
    };
    const closeModal = () => {     
        setModal({ ...modal, show: false });
    };
    return (
        
        <footer className="px-6 md:px-16 lg:px-24 xl:px-32 pt-8 w-full text-gray-500 bg-white">
            <Modal show={modal.show} onClose={closeModal} title={modal.title} message={modal.message} type={modal.type} />
            <div className="flex flex-col md:flex-row justify-between w-full gap-10 border-b border-gray-500/30 pb-6">
                <div className="md:max-w-96 mt-2 text-center md:text-left">
                    <Link to={'/'}>
                        <img onClick = {() => window.scrollTo(0,0)} src = {assets.logo} alt = "logo" className = "w-64 opacity-80 invert mx-auto md:mx-0" ></img>
                    </Link>
                    <p className=" text-sm">
                        Seguici anche sui nostri social, rimani aggiornato sulle nostre offerte e vienici a trovare, ti aspettiamo
                    </p>
                    <div className = "mt-4 flex items-center justify-center md:justify-start gap-2">
                        <img src = {assets.facebookIcon} alt = "facebook" className = "w-6 h-6"></img>
                        <img src = {assets.instagramIcon} alt = "instagram" className = "w-6 h-6"></img>
                        <img src = {assets.twitterIcon} alt = "twitter" className = "w-6 h-6"></img>
                    </div>
                    <p className="mt-6 text-sm">
                    </p>
                </div>
                <div>
                    <Title title="Compagnia" align = "left"/>
                    <ul className="text-sm space-y-2">
                        <li><a href="#" onClick = {() => window.scrollTo(0,0)}>Home</a></li>
                        <li><a href="#" onClick = {()=> navigate("/About")}>Chi Siamo</a></li>
                        <li><a href="#">Contattaci</a></li>
                        <li><a href="#">Privacy policy</a></li>
                    </ul>
                </div>
                    <div>
                        <Title title="Newsletter" align = "left"/>
                        <div className="text-sm space-y-2">
                            <p>Ti terremo aggiornato sulle nuove offerte e pacchetti.</p>
                            <div className="flex items-center gap-2 pt-4">
                            <input 
                                className="border border-gray-500/30 placeholder-gray-500 focus:ring-2 ring-indigo-600 outline-none w-full max-w-64 h-9 rounded px-2" 
                                type="email" 
                                placeholder="Inserisci la tua email" 
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                                <button className="bg-blue-600 w-24 h-9 text-white rounded" onClick={onSubscribe}>Iscriviti</button>
                            </div>
                        </div>
                    </div>
            </div>
        </footer>
    );
};
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {useState} from "react";
import Modal from "../components/Modal";
import Title from '../components/Title';
import { apiPost, salvaSessione } from '../api';
export default function RegisterPage() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nome: '',
        cognome : '',
        email : '',
        password : '',
        confirmPassword : ''
    });
    const [modal, setModal] = useState({ show: false, title: '', message: '', type: '', onConfirm: null });
    const [showPassword, setShowPassword] = useState(false);

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };
    const closeModal = () => {
        setModal({ ...modal, show: false });
        if (modal.onConfirm) modal.onConfirm();
    };

    const caratteriSpeciali = /[!@#$%^&*(),.?":{}|<>]/;
    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name] : value
        })
    }


    const handleSubmit = async (e) => {
        e.preventDefault();
        if(formData.password !== formData.confirmPassword){
            setModal({
                show: true,
                title: "Errore Password",
                message: "Le password non corrispondono!",
                type: "error"
            });
            return;
        }
        if(!caratteriSpeciali.test(formData.password)){
            setModal({
                show: true,
                title: "Password Debole",
                message: "La password deve contenere almeno un carattere speciale!",
                type: "error"
            });
            return;
        }
        const dataToSend = { ...formData };
        delete dataToSend.confirmPassword;

        try{
            salvaSessione(await apiPost('/clienti', dataToSend)); // Inviamo solo i dati necessari al backend
            setModal({
                show: true,
                title: "Benvenuto!",
                message: "Registrazione avvenuta con successo!",
                type: "success",
                onConfirm: () => navigate("/")
            });
        }catch(error){
            console.error(error);
            setModal({
                show: true,
                title: "Errore",
                message: error.message,
                type: "error"
            });
        }
    }

    




    return (
        <div className="flex min-h-screen items-center justify-center pt-44 px-4 mb-20 bg-white">
            <Modal show={modal.show} onClose={closeModal} title={modal.title} message={modal.message} type={modal.type} />
            <form onSubmit={handleSubmit} className="flex w-full flex-col items-center justify-center max-w-96">
                <Title title = "Registrati" subTitle = "Crea il tuo account per iniziare a prenotare"></Title>
                <div className="mt-10 flex h-12 w-full items-center gap-2 overflow-hidden rounded-full border border-gray-200 bg-transparent pl-5 focus-within:border-gray-300">
                    <input
                        name="nome"
                        value={formData.nome}
                        onChange={handleChange}
                        placeholder="Nome"
                        className="h-full w-full bg-transparent text-sm placeholder-gray-400 outline-none"
                        required=""
                        type="text"
                    />
                </div>
                <div className="flex h-12 w-full items-center gap-2 overflow-hidden rounded-full border border-gray-200 bg-transparent pl-5 focus-within:border-gray-300 mt-6">
                    <input
                        name="cognome"
                        value={formData.cognome}
                        onChange={handleChange}
                        placeholder="Cognome"
                        className="h-full w-full bg-transparent text-sm placeholder-gray-400 outline-none"
                        required=""
                        type="text"

                    />
                </div>
                <div className="flex h-12 w-full items-center gap-2 overflow-hidden rounded-full border border-gray-200 bg-transparent pl-5 focus-within:border-gray-300 mt-6">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-mail text-gray-400" aria-hidden="true" >
                        <path d="m22 7-8.991 5.727a2 2 0 0 1-2.009 0L2 7"></path>
                        <rect x="2" y="4" width="20" height="16" rx="2"></rect>
                    </svg>
                    <input
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="Email id"
                        className="h-full w-full bg-transparent text-sm placeholder-gray-400 outline-none"
                        required=""
                        type="email"
                    />
                </div>
                <div className="mt-6 flex h-12 w-full items-center gap-2 overflow-hidden rounded-full border border-gray-200 bg-transparent pl-5 focus-within:border-gray-300">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-lock text-gray-400" aria-hidden="true" >
                        <rect width="18" height="11" x="3" y="11" rx="2" ry="2"></rect>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                    </svg>
                    <input
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        type = {showPassword ? "text" : "password"}
                        placeholder="Password"
                        className="h-full w-full bg-transparent text-sm placeholder-gray-400 outline-none"
                        required
                    />
                    <button onClick = {togglePasswordVisibility} type = "button" className = "text-gray-400 -translate-x-4">
                            {showPassword ? (
                            <span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-fill" viewBox="0 0 16 16">
                                    <path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0"/>
                                    <path d="M0 8s3-5.5 8-5.5S16 8 16 8s-3 5.5-8 5.5S0 8 0 8m8 3.5a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7"/>
                            </svg>
                        </span>
                            ):
                            (
                            <span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-slash-fill" viewBox="0 0 16 16">
                                    <path d="m10.79 12.912-1.614-1.615a3.5 3.5 0 0 1-4.474-4.474l-2.06-2.06C.938 6.278 0 8 0 8s3 5.5 8 5.5a7 7 0 0 0 2.79-.588M5.21 3.088A7 7 0 0 1 8 2.5c5 0 8 5.5 8 5.5s-.939 1.721-2.641 3.238l-2.062-2.062a3.5 3.5 0 0 0-4.474-4.474z"/>
                                    <path d="M5.525 7.646a2.5 2.5 0 0 0 2.829 2.829zm4.95.708-2.829-2.83a2.5 2.5 0 0 1 2.829 2.829zm3.171 6-12-12 .708-.708 12 12z"/>
                                </svg>
                            </span>
                            )}
                    </button>
                </div>
                <a className="text-xs flex items-center justify-end mt-2 text-gray-500">
                    La password deve contenere almeno una lettera maiuscola, un numero e un carattere speciale
                </a>
                <div className="mt-6 flex h-12 w-full items-center gap-2 overflow-hidden rounded-full border border-gray-200 bg-transparent pl-5 focus-within:border-gray-300">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-lock text-gray-400" aria-hidden="true" >
                        <rect width="18" height="11" x="3" y="11" rx="2" ry="2"></rect>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                    </svg>
                    <input
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        placeholder="Conferma Password"
                        className="h-full w-full bg-transparent text-sm placeholder-gray-400 outline-none"
                        required
                        type={showPassword ? "text" : "password"}
                    />
                        <button onClick = {togglePasswordVisibility} type = "button" className = "text-gray-400 -translate-x-4">
                            {showPassword ? (
                            <span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-fill" viewBox="0 0 16 16">
                                    <path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0"/>
                                    <path d="M0 8s3-5.5 8-5.5S16 8 16 8s-3 5.5-8 5.5S0 8 0 8m8 3.5a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7"/>
                            </svg>
                        </span>
                            ):
                            (
                            <span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye-slash-fill" viewBox="0 0 16 16">
                                    <path d="m10.79 12.912-1.614-1.615a3.5 3.5 0 0 1-4.474-4.474l-2.06-2.06C.938 6.278 0 8 0 8s3 5.5 8 5.5a7 7 0 0 0 2.79-.588M5.21 3.088A7 7 0 0 1 8 2.5c5 0 8 5.5 8 5.5s-.939 1.721-2.641 3.238l-2.062-2.062a3.5 3.5 0 0 0-4.474-4.474z"/>
                                    <path d="M5.525 7.646a2.5 2.5 0 0 0 2.829 2.829zm4.95.708-2.829-2.83a2.5 2.5 0 0 1 2.829 2.829zm3.171 6-12-12 .708-.708 12 12z"/>
                                </svg>
                            </span>
                            )}
                    </button>
                </div>
                <div className="mt-8 flex w-full items-center justify-between">
                    <label className="flex cursor-pointer items-center gap-2">
                        <input className="peer hidden" type="checkbox" checked="" />
                        <span className="relative flex size-4.5 items-center justify-center rounded border border-slate-300 peer-checked:border-gray-800 peer-checked:bg-gray-800">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-check size-3 text-white" aria-hidden="true" >
                                <path d="M20 6 9 17l-5-5"></path>
                            </svg>
                        </span>
                        <span className="text-gray-500 select-none">Ricordami</span>
                    </label>
                    <a className="text-gray-800 underline" href="#">
                        Password dimenticata?
                    </a>
                </div>
                <button type="submit" className="mt-8 h-11 w-full cursor-pointer rounded-full bg-linear-to-b from-gray-600 to-gray-800 text-white transition hover:from-gray-700 hover:to-gray-900" >
                    Registrati
                </button>
                <p className="mt-4 text-gray-500/90">
                    Hai gia' un account?
                    <span>                        
                        <Link to = '/login' className="items-center text-gray-800 underline" href="#">
                           Accedi
                        </Link>
                    </span>               
                </p>
            </form>
        </div>
    );
};
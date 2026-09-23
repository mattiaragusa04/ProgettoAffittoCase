import React from 'react';
import Title from './Title';
import { useState, useEffect } from 'react';
import { apiGet } from '../api';

const Recensioni = () => {


    const [currentIndex, setCurrentIndex] = React.useState(0);
    const [isMobile, setIsMobile] = React.useState(window.innerWidth < 768);
    const [recensioni, setRecensioni ]= useState([]);
    const [errore, setErrore] = useState(null);

    React.useEffect(() => {
        const handleResize = () => {
            setIsMobile(window.innerWidth < 768);
        };
        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, []);

    const handleNext = () => {
        setCurrentIndex((prev) => prev + 3 >= recensioni.length ? 0 : prev + 3);
    };

    const handlePrev = () => {
        setCurrentIndex((prev) => prev - 3 < 0 ? Math.max(recensioni.length - 3, 0) : prev - 3);
    };

    React.useEffect(() => {
        if (!isMobile || recensioni.length === 0) return;

        const interval = setInterval(() => {
            setCurrentIndex((prev) =>
                prev + 1 >= recensioni.length ? 0 : prev + 1
            );
        }, 3000);

        return () => clearInterval(interval);
    }, [isMobile, recensioni]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchRecensioni = async () => {
            try{
                setRecensioni(await apiGet('/recensioni'));
            }catch(error){
                setErrore(error.message);
            } finally {
                setLoading(false);
            } 
        };
        fetchRecensioni();
    },[])
    return (
        <>
            <section className='py-20 px-4 sm:px-6 lg:px-8 bg-white'>
                <div className='w-full max-w-6xl mx-auto'>
                    <Title title="Ecco cosa dicono di noi i nostri Clienti" subTitle="Prima di prenotare scopri quello che dicono delle nostre strutture, potresti saperne qualcosa di piu`"></Title>

                    <div className='hidden md:flex justify-end gap-2 mt-4'>
                        <div onClick={handlePrev} className='h-10 w-10 rounded-lg bg-neutral-100 border border-neutral-200 flex items-center justify-center cursor-pointer hover:bg-neutral-200 transition-all text-neutral-500'>
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-arrow-left-icon lucide-arrow-left"><path d="m12 19-7-7 7-7" /><path d="M19 12H5" /></svg>
                        </div>
                        <div onClick={handleNext} className='h-10 w-10 rounded-lg bg-neutral-100 border border-neutral-200 flex items-center justify-center cursor-pointer hover:bg-neutral-200 transition-all text-neutral-500'>
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-arrow-right-icon lucide-arrow-right"><path d="M5 12h14" /><path d="m12 5 7 7-7 7" /></svg>
                        </div>
                    </div>

                    <div className='mx-auto grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 px-8 md:px-0 mt-12 md:mt-6'>
                        {loading ? <p className="col-span-3 text-center">Caricamento recensioni...</p> :
                         recensioni.length > 0 ? recensioni.slice(currentIndex, isMobile ? currentIndex + 1 : currentIndex + 3).map((recensione) => (
                                <div key={recensione.id} className='bg-zinc-50 hover:-translate-y-1 transition duration-300 border border-zinc-200 rounded-2xl p-6 space-y-6'>
                                    <div className='flex items-start justify-between'>
                                        <div className="flex">
                                            {Array(recensione.valutazione).fill(0).map((_, i) => (
                                                <svg key={i} xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
                                                    className="lucide lucide-star text-transparent fill-[#FF8F20]" aria-hidden="true">
                                                    <path d="M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"></path>
                                                </svg>
                                            ))}
                                        </div>
                                    </div>

                                    <p className='text-sm/6 text-neutral-600'>"{recensione.testo}"</p>

                                    <div className='flex items-center gap-4 mt-4'>
                                        <img src={`https://ui-avatars.com/api/?name=${recensione.cliente?.nome}+${recensione.cliente?.cognome}&background=random`} alt="User Avatar" className='w-12 h-12 rounded-full object-cover'/>
                                        <div>
                                            <p className='text-sm text-neutral-700'>{recensione.cliente ? `${recensione.cliente.nome} ${recensione.cliente.cognome}` : 'Utente'}</p>
                                            <p className='text-xs font-medium text-neutral-500'>Cliente Verificato</p>
                                        </div>
                                    </div>
                                </div>
                            )) : <p className="col-span-3 text-center">{errore ?? 'Nessuna recensione disponibile al momento.'}</p>
                        }
                    </div>
                </div>
                <div className="hidden max-[768px]:flex items-center justify-center mt-5 space-x-2">
                    {recensioni.map((_, index) => (
                        <span key={index} onClick={() => setCurrentIndex(index)}
                            className={`w-3 h-3 rounded-full transition-all ${index === currentIndex
                                    ? "bg-neutral-800"
                                    : "bg-neutral-300"
                                }`}
                        ></span>
                    ))}
                </div>
            </section>
        </>
    )
}
export default Recensioni

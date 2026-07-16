import Title from "../components/Title";


export default function AboutUs() {
    return (
        <>
            <div className = "pt-40 pb-10 min-h-screen bg-white mb-10">
                <Title title="Chi Siamo" subTitle = "Scopri chi siamo e cosa facciamo per rendere la vostra esperienza unica e indimenticabile." />

                <div className="max-w-4xl mx-auto flex flex-col md:flex-row items-center justify-center gap-8 px-4 md:px-0 py-10">
                    <div className="size-130 rounded-full absolute blur-[300px] -z-10 bg-[#FBFFE1]"></div>
                    <img className="max-w-sm w-full rounded-xl h-auto"
                        src="https://www.diotti.com/it/media/images/progetti/373708/progetto-appartamento-open-sapce-60-mq-diotti.jpeg"
                        alt="" />
                    <div>
                        <Title title="Il Nostro Progetto" align="left" subTitle = "La nostra iniziativa e` nata con l'obiettivo di rendere la vostra esperienza unica e indimenticabile. Con la nostra vasta gamma di paccehtti e offerte riuscirete a godervi dei giorni in pieno relax nella nostra struttura."></Title>
                
                        <div className="flex flex-col gap-10 mt-6">
                            <div className="flex items-center gap-4">
                                <div>
                                    <h3 className="text-base font-medium text-slate-600">Struttura con Professionisti di alto livello</h3>
                                    <p className="text-sm text-slate-500">Avrete la possibilita` di interfacciarvi con i nostri dipendenti all'interno della struttura, fornendo spiegazioni su i nostri servizi.</p>
                                </div>
                            </div>
                            <div className="flex items-center gap-4">
                                <div>
                                    <h3 className="text-base font-medium text-slate-600">Design Moderno e Accogliente</h3>
                                    <p className="text-sm text-slate-500">La nostra struttura è stata progettata con cura per offrire un ambiente confortevole e accogliente, dove ogni dettaglio è stato pensato per migliorare la vostra esperienza.</p>
                                </div>
                            </div>
                            <div className="flex items-center gap-4">

                                <div>
                                    <h3 className="text-base font-medium text-slate-600">Configurazione Facile e Rapida</h3>
                                    <p className="text-sm text-slate-500">La nostra struttura offre una configurazione facile e rapida, consentendo agli utenti di personalizzare la loro esperienza in modo semplice e pratico.</p>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                    
                </div>
                <div className="gap-4">
                    <Title title = "Contattaci" align = "center" ></Title>
                    <p className="text-center text-sm text-gray-500 mt-2">Hai domande o vuoi saperne di più sui nostri servizi? Contattaci, siamo qui per aiutarti!</p>
                    <div className="flex items-center justify-center gap-6 mt-6">
                        <a href="mailto:info@ragusabb.com" className="text-sm font-medium text-indigo-500 hover:text-indigo-600 flex items-center gap-1">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-mail">
                                <rect width="20" height="16" x="2" y="4" rx="2"/>
                                <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/>
                            </svg>
                            Invia Email
                        </a>
                        <a href="tel:+391234567890" className="text-sm font-medium text-indigo-500 hover:text-indigo-600 flex items-center gap-1">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-phone">
                                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
                            </svg>
                            Chiama Ora
                        </a>
                    </div>
                </div>
            </div>
        </>
    );
};
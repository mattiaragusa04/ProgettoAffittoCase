// Pannello del proprietario: da costruire (gestione stanze, prenotazioni, clienti)
function OwnerPage() {
    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-4">Pannello del Proprietario</h1>
            <p className="text-gray-600 mb-8">Qui puoi gestire le stanze, le prenotazioni e i clienti.</p>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Gestione Stanze</h2>
                    <p className="text-gray-600">Aggiungi, modifica o rimuovi stanze disponibili per la prenotazione.</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Gestione Prenotazioni</h2>
                    <p className="text-gray-600">Visualizza e gestisci tutte le prenotazioni effettuate dai clienti.</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Gestione Clienti</h2>
                    <p className="text-gray-600">Visualizza e gestisci le informazioni dei clienti registrati.</p>
                </div>
            </div>
        </div>
    );
}

export default OwnerPage;

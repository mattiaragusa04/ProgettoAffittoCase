// Tutte le chiamate al backend passano da qui.
// L'indirizzo si può cambiare con la variabile VITE_API_URL (file .env), senza toccare il codice.
export const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

// Errore con un messaggio già pronto da mostrare all'utente
export class ApiError extends Error {
    constructor(message, status) {
        super(message);
        this.status = status;
    }
}

export const apiGet = (path) => richiesta(path, { method: 'GET' });

export const apiPost = (path, body) => richiesta(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
});

async function richiesta(path, options) {
    let response;
    try {
        response = await fetch(API_URL + path, options);
    } catch {
        // fetch fallisce senza risposta: backend spento, rete assente o richiesta bloccata dal CORS
        throw new ApiError('Impossibile contattare il server. Controlla che il backend sia avviato e riprova.', 0);
    }

    const data = await response.json().catch(() => null);
    if (!response.ok) {
        throw new ApiError(messaggioErrore(response.status, data), response.status);
    }
    return data;
}

// Il backend risponde con { status, messaggio, dettagli? } (vedi GlobalExceptionHandler)
function messaggioErrore(status, data) {
    if (data?.dettagli) return Object.values(data.dettagli).join('. ');
    if (data?.messaggio) return data.messaggio;
    if (status >= 500) return 'Errore interno del server. Riprova più tardi.';
    return `Richiesta non riuscita (errore ${status}).`;
}

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

// Sessione: dopo login o registrazione il backend restituisce { token, utente }
export function salvaSessione({ token, utente }) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(utente));
}

export function chiudiSessione() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
}

export const apiGet = (path) => richiesta(path, { method: 'GET' });

export const apiPost = (path, body) => richiesta(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
});

export const apiPut = (path, body) => richiesta(path, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
});

async function richiesta(path, options) {
    // Se l'utente è loggato, ogni richiesta porta con sé il suo token
    const token = localStorage.getItem('token');
    if (token) {
        options.headers = { ...options.headers, Authorization: `Bearer ${token}` };
    }

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
    if (status === 401) return 'Devi effettuare il login.';
    if (status === 403) return 'Non hai i permessi per questa operazione.';
    if (status >= 500) return 'Errore interno del server. Riprova più tardi.';
    return `Richiesta non riuscita (errore ${status}).`;
}

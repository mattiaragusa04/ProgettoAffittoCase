// Funzioni sulle date usate in più pagine.
// Le date arrivano dal backend (e dagli input type="date") come testo YYYY-MM-DD.

// Data di oggi come YYYY-MM-DD, nel fuso orario locale
export const oggi = () => new Date().toLocaleDateString('sv-SE');

// Notti tra check-in e check-out; 0 se una data manca o se il check-out non è dopo il check-in
export const contaNotti = (checkIn, checkOut) => {
    if (!checkIn || !checkOut) return 0;
    const notti = (new Date(checkOut) - new Date(checkIn)) / 86400000;
    return notti > 0 ? notti : 0;
};

// Es. "5 ottobre 2026"
export const formattaData = (data) =>
    new Date(data).toLocaleDateString('it-IT', { day: 'numeric', month: 'long', year: 'numeric' });

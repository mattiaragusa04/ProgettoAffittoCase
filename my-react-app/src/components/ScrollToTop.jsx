import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

// A ogni cambio pagina riporta la vista in cima.
// Se il link ha un'ancora (es. /About#contatti) scorre invece fino a quella sezione.
function ScrollToTop() {
    const { pathname, hash, key } = useLocation();

    useEffect(() => {
        const sezione = hash && document.getElementById(hash.slice(1));
        if (sezione) {
            sezione.scrollIntoView({ behavior: 'smooth' });
        } else {
            window.scrollTo(0, 0);
        }
    }, [pathname, hash, key]); // key cambia anche cliccando un link alla pagina in cui sei già

    return null;
}

export default ScrollToTop;

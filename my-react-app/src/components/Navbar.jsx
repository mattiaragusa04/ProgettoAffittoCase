import React, { useState, useEffect, useRef } from "react";
import {Link} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useNavigate, useLocation} from "react-router-dom";
import { chiudiSessione } from "../api";
const Navbar = () => {
    
    const navLinks = [
        { name: 'Home', path: '/' },
        { name: 'Stanze', path: '/allRooms' },
        { name: 'Tutte le recensioni', path: '/recensioni' },
        { name: 'Chi Siamo', path: '/About' },
    ];


    const [isScrolled, setIsScrolled] = useState(false);
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [user, setUser] = useState(null);
    const [isProfiloOpen, setIsProfiloOpen] = useState(false);
    const profiloRef = useRef(null);
    const navigate = useNavigate();

    const location = useLocation();

    const handleLogout = () => {
        setIsProfiloOpen(false);
        chiudiSessione();
        setUser(null);
        navigate('/');
    };

    useEffect(() => {
      // Controllo utente eseguito solo al cambio pagina, non allo scroll
      const checkUser = () => {
          const storedUser = localStorage.getItem('user');
          if (storedUser) {
              try {
                  setUser(JSON.parse(storedUser));
              } catch (error) {
                  console.error("Errore dati utente", error);
                  chiudiSessione();
              }
          }
      };
      checkUser();

      const handleScroll = () => {
          if (location.pathname === '/') {
              setIsScrolled(window.scrollY > 10);
          } else {
              setIsScrolled(true);
          }
      };

      handleScroll();

      window.addEventListener("scroll", handleScroll);
      return () => window.removeEventListener("scroll", handleScroll);
    }, [location.pathname]); // L'effetto si resetta solo quando cambi pagina

    // Menu del profilo: si chiude cliccando fuori o premendo Esc
    useEffect(() => {
        if (!isProfiloOpen) return;
        const chiudi = (e) => {
            const fuori = e.type === 'mousedown' && !profiloRef.current?.contains(e.target);
            if (fuori || e.key === 'Escape') setIsProfiloOpen(false);
        };
        document.addEventListener('mousedown', chiudi);
        document.addEventListener('keydown', chiudi);
        return () => {
            document.removeEventListener('mousedown', chiudi);
            document.removeEventListener('keydown', chiudi);
        };
    }, [isProfiloOpen]);

    return (
            <nav className={`fixed top-0 left-0 w-full flex items-center justify-between px-4 md:px-8 lg:px-12 xl:px-16 transition-all duration-500 z-50 ${isScrolled ? "bg-white/80 shadow-md text-gray-700 backdrop-blur-lg py-3 md:py-4" : "py-4 md:py-6"}`}>

            {/* Logo */}
            <Link to="/">
                <img src={assets.logo} alt="logo" className={`h-25 ${isScrolled && "invert opacity-80"}`} />
            </Link>

            {/* Desktop Nav */}
            <div className="hidden md:flex items-center gap-4 lg:gap-8">
                {navLinks.map((link, i) => (
                    <Link key={i} to={link.path} className={`group flex flex-col gap-0.5 ${isScrolled ? "text-gray-700" : "text-white"}`}>
                        {link.name}
                        <div className={`${isScrolled ? "bg-gray-700" : "bg-white"} h-0.5 w-0 group-hover:w-full transition-all duration-300`} />
                    </Link>
                ))}
                {/* Solo l'amministratore vede la Dashboard */}
                {user?.ruolo === 'ADMIN' && (
                    <button className={`border px-4 py-1 text-sm font-light rounded-full cursor-pointer ${isScrolled ? 'text-black' : 'text-white'} transition-all`} onClick={() => navigate("/owner")}>
                        Dashboard
                    </button>
                )}
            </div>

            {/* Desktop Right */}
            <div className="hidden md:flex items-center gap-4">
                

                {user ? (
                    <div ref={profiloRef} className="relative ml-4">
                        <button type="button" onClick={() => setIsProfiloOpen(!isProfiloOpen)}
                            aria-haspopup="menu" aria-expanded={isProfiloOpen}
                            title={`Loggato come ${user.nome}`}
                            className="flex items-center gap-2 cursor-pointer">
                            <img src = {user.picture|| `https://ui-avatars.com/api/?name=${user.nome}+${user.cognome}&background=random`} 
                                alt = "user_picture" 
                                referrerPolicy="no-referrer"
                                className = "w-10 h-10 rounded-full object-cover" />
                            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
                                className={`${isScrolled ? "text-gray-700" : "text-white"} transition-transform duration-200 ${isProfiloOpen ? "rotate-180" : ""}`}>
                                <path d="m6 9 6 6 6-6" />
                            </svg>
                        </button>

                        {isProfiloOpen && (
                            <div role="menu" className="absolute right-0 top-full mt-3 w-64 bg-white text-gray-700 rounded-xl shadow-xl border border-gray-100 py-2 text-sm">
                                <div className="px-4 py-3 border-b border-gray-100">
                                    <p className="font-medium text-gray-800">{user.nome} {user.cognome}</p>
                                    <p className="text-xs text-gray-500 truncate">{user.email}</p>
                                </div>
                                <Link to="/datiPersonali" role="menuitem" onClick={() => setIsProfiloOpen(false)}
                                    className="flex items-center gap-3 px-4 py-2.5 hover:bg-gray-50 transition-colors">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-500">
                                        <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" /><circle cx="12" cy="7" r="4" />
                                    </svg>
                                    Dati personali
                                </Link>
                                <Link to="/leMiePrenotazioni" role="menuitem" onClick={() => setIsProfiloOpen(false)}
                                    className="flex items-center gap-3 px-4 py-2.5 hover:bg-gray-50 transition-colors">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-500">
                                        <rect width="18" height="18" x="3" y="4" rx="2" /><path d="M16 2v4" /><path d="M8 2v4" /><path d="M3 10h18" />
                                    </svg>
                                    Le mie prenotazioni
                                </Link>
                                <button type="button" role="menuitem" onClick={handleLogout}
                                    className="w-full flex items-center gap-3 px-4 py-2.5 mt-1 border-t border-gray-100 hover:bg-gray-50 transition-colors cursor-pointer">
                                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-500">
                                        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" /><polyline points="16 17 21 12 16 7" /><line x1="21" x2="9" y1="12" y2="12" />
                                    </svg>
                                    Logout
                                </button>
                            </div>
                        )}
                    </div>

                ) : (
                    location.pathname === "/login" || location.pathname === "/register" ? (
                        <div className = "flex items-center gap-4 ml-50"></div>
                    ) : (
                        <>
                            <button onClick={() => navigate("/login")} className={`px-8 py-2.5 rounded-full ml-4 transition-all duration-500 ${isScrolled ? "text-white bg-black" : "bg-white text-black"}`}>
                                Sign up/Sign In
                            </button>
                        </>
                    )
                )}
            </div>

            {/* Mobile Menu Button */}
            <div className="flex items-center gap-3 md:hidden">
                <img onClick={() => setIsMenuOpen(!isMenuOpen)} src={assets.menuIcon} alt="menu" className={`h-4 cursor-pointer ${isScrolled && "invert"}`} />
            </div>

            {/* Mobile Menu */}
            <div className={`fixed top-0 left-0 w-full h-screen bg-white text-base flex flex-col md:hidden items-center justify-center gap-6 font-medium text-gray-800 transition-all duration-500 ${isMenuOpen ? "translate-x-0" : "-translate-x-full"}`}>
                <button className="absolute top-4 right-4" onClick={() => setIsMenuOpen(false)}>
                    <img src={assets.closeIcon} alt="close" className="h-6.5" />
                </button>

                {navLinks.map((link, i) => (
                    <Link key={i} to={link.path} onClick={() => setIsMenuOpen(false)}>
                        {link.name}
                    </Link>
                ))}

                {user ? (
                    <>
                        <Link to="/datiPersonali" onClick={() => setIsMenuOpen(false)}>Dati personali</Link>
                        <Link to="/leMiePrenotazioni" onClick={() => setIsMenuOpen(false)}>Le mie prenotazioni</Link>
                        <button onClick={handleLogout} className="bg-black text-white px-8 py-2.5 rounded-full transition-all duration-500">
                            Logout
                        </button>
                    </>
                ) : (location.pathname === "/login" || location.pathname === "/register" ? (
                    <div className = "flex items-center gap-4 ml-50"></div>
                ) : (
                    <>
                        <button onClick={() => {setIsMenuOpen(false); navigate("/login")}} className="bg-black text-white px-8 py-2.5 rounded-full transition-all duration-500">
                            Sign up/Sign In
                        </button>
                    </>
                ))}
            </div>
        </nav>
    );
}

export default Navbar;
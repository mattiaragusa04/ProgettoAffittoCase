import React, { useState, useEffect} from "react";
import {Link} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useNavigate, useLocation} from "react-router-dom";
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
    const navigate = useNavigate();

    const location = useLocation();

    const handleLogout = () => {
        localStorage.removeItem('user');
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
                  localStorage.removeItem('user');
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
                <button className={`border px-4 py-1 text-sm font-light rounded-full cursor-pointer ${isScrolled ? 'text-black' : 'text-white'} transition-all`} onClick={() => navigate("/owner")}>
                    Dashboard
                </button>
            </div>

            {/* Desktop Right */}
            <div className="hidden md:flex items-center gap-4">
                

                {user ? (
                    <div className="flex items-center gap-4 ml-4">
                        <img src = {user.picture|| `https://ui-avatars.com/api/?name=${user.nome}+${user.cognome}&background=random`} 
                            alt = "user_picture" 
                            referrerPolicy="no-referrer"
                            className = "w-10 h-10 rounded-full object-cover cursor-pointer" 
                            title = {`Loggato come ${user.nome}`}/>
                        <button onClick={handleLogout} className={`px-8 py-2.5 rounded-full ml-4 transition-all duration-500 ${isScrolled ? "text-white bg-black" : "bg-white text-black"}`}>
                            Logout
                        </button>
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
                    <button onClick={handleLogout} className="bg-black text-white px-8 py-2.5 rounded-full transition-all duration-500">
                        Logout
                    </button>
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
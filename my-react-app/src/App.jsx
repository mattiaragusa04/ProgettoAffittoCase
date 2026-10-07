import Navbar from './components/Navbar';
import ScrollToTop from './components/ScrollToTop';
import {useLocation} from "react-router-dom";
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home";
import AllRooms from "./pages/AllRooms";
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Footer from './components/Footer';
import Rooms from './pages/Rooms';
import RecensionePage from "./pages/RecensionePage"
import AboutUs from "./pages/AboutUs";
import TutteOfferte from "./pages/TutteOfferte";
import DettaglioStanza from "./pages/DettaglioStanza";
import DatiPersonali from "./pages/DatiPersonali";
import LeMiePrenotazioni from "./pages/LeMiePrenotazioni";
import OwnerPage from './pages/Owner';
function App(){
  const isOwnerPath = useLocation().pathname.includes("owner"); //nascosta: qui dentro c'è il proprietario, che sarei io!
  return (
      <div>
        <ScrollToTop />
        {!isOwnerPath && <Navbar/>}
        <div className = "min-h-[70vh]">
          <Routes>
            <Route path="/allRooms" element={<AllRooms />} />
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<LoginPage/>} />
            <Route path="/register" element={<RegisterPage/>} />
            <Route path = "/rooms" element = {<Rooms/>}/>
            <Route path = "/recensioni" element = {<RecensionePage/>}/>
            <Route path = "/About" element = {<AboutUs/>}/>
            <Route path = "/tutteOfferte" element = {<TutteOfferte/>}/>
            <Route path = "/room/:id" element = {<DettaglioStanza/>}/>
            <Route path = "/datiPersonali" element = {<DatiPersonali/>}/>
            <Route path = "/leMiePrenotazioni" element = {<LeMiePrenotazioni/>}/>
            <Route path = "/owner" element = {<OwnerPage/>}/>
          </Routes>
         </div>
        {!isOwnerPath && <Footer/>}
      </div>
  );
}

export default App;
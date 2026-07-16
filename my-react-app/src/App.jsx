import Navbar from './components/navbar';
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
function App(){
  const isOwnerPath = useLocation().pathname.includes("owner"); //nascosta: qui dentro c'è il proprietario, che sarei io!
  return (
      <div>
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
          </Routes>
         </div>
        {!isOwnerPath && <Footer/>}
      </div>
  );
}

export default App;
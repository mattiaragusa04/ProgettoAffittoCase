import HotelCard from './HotelCard';
import { roomsDummyData } from '../assets/assets';
import Title from './Title';
import {useNavigate} from 'react-router-dom';

function FeaturedDestionation() {
    const navigation = useNavigate();
    return (
        <div>
        <div className = "flex flex-col items-center px-6 md:px-16 lg:px-24 bg-slate-50">
            <Title title = 'Le Stanze piu` Vendute' subTitle = 'Scopri la vasta gamma delle stanze piu` premium di Palermo'></Title>
            <div className = "flex flex-wrap items-center justify-between gap-6 mb-10 mt-10">
                {roomsDummyData.slice(0,4).map((room,index)=>(<HotelCard key = {room._id} room = {room} index = {index}></HotelCard>))}
            </div>
                <button onClick = {()=>{navigation('/allRooms'); scrollTo(0,0)}} 
                        className = 
                            'my-16 mt-8 px-4 py-2 text-sm font-semibold border border-gray-300 rounded bg-white hover:bg-gray-50 transition-all cursor-pointer'>
                            Guarda tutte le stanze
                </button>
        </div>
        </div>
    )
}

export default FeaturedDestionation;

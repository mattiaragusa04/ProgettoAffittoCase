import React from 'react';
import {Link} from 'react-router-dom';
import {assets} from '../assets/assets.js';
const HotelCard = ({room, index}) => {
    const [tilt, setTilt] = React.useState({ x: 0, y: 0 });

    // Adjust the threshold value to control the tilt effect
    const threshold = 12;

    const handleMove = (e) => {
        const { left, top, width, height } = e.currentTarget.getBoundingClientRect();
        const x = (e.clientX - left) / width - 0.5;
        const y = (e.clientY - top) / height - 0.5;
        setTilt({ x: y * -threshold, y: x * threshold });
    };

    return (
        <Link to = {'/room/' + room._id} onClick = {() => window.scrollTo(0, 0)} key={room._id}>
            <div className="rounded-xl shadow-xl overflow-hidden transition-transform duration-200 ease-out cursor-pointer max-w-80 bg-white"
                onMouseMove={handleMove}
                onMouseLeave={() => setTilt({ x: 0, y: 0 })}
                style={{ transform: `perspective(1000px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg)` }}
            >
                <img src= {room.images[0]} alt="" className="w-full h-52 object-cover"/>
                {index % 2 ===0 && <p className = "px-3 py-1 absolute top-3 left-3 text-xs bg-white text-gray-800 fond-medium rounded-full">Best Seller</p>}
                <div className = "p-4 pt-5">
                    <div className = "flex items-center justify-between">
                        <p className = "font-playfair text-xl font-medium text-gray-800">{room.hotel.name}</p>
                        <div className = "flex items-center gap-1">
                            <img src = {assets.starIconFilled} alt = "star-icon" />4.5
                        </div>
                    </div>
                    <div className = "flex items-center gap-1 text-sm">
                        <img src = {assets.locationIcon} alt = "location-icon" />
                        <span>{room.hotel.address}</span>
                    </div>
                    <div className = "flex items-center justify-between mt-4">
                        <p>
                            <span>€{room.pricePerNight}/Notte</span>
                        </p>
                        <button className = "px-4 py-2 text-sm font-medium border border-gray-300 rounded hover:bg-gray-50 transition-all cursor-pointer">Prenota adesso</button>
                    </div>
                </div>
            </div>
        </Link>
    );
};
export default HotelCard;
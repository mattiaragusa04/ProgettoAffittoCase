import { useState } from 'react'
import {assets} from '../assets/assets.js'
import { useNavigate } from 'react-router-dom';

function Hero() {
  const navigate = useNavigate();
  const [searchData, setSearchData] = useState({
    checkIn: '',
    checkOut: '',
    guests: 1
  });

  const handleChange = (e) => {
    setSearchData({ ...searchData, [e.target.id]: e.target.value });
  };

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/rooms?checkIn=${searchData.checkIn}&checkOut=${searchData.checkOut}&guests=${searchData.guests}`);
  };

  return (
    
        <div className="flex flex-col items-start justify-center px-6 md:px-16 lg:px-24 xl:px-32 text-white bg-[url('/src/assets/heroImage.png')] bg-no-repeat bg-cover bg-center h-screen">
          <p className="bg-[#49b9ff]/50 px-3.5 py-y rounded-full mt-20 md:text-[30px] md:leading-14 font-bold md:font-extrabold">Il B&B dei tuoi sogni!</p>
          <h1 className="font-playfair text-5xl md:text-5xl md:text-[56px] md:leading-14 font-bold md:font-extrabold max-w-xl mt-4">Scopri il B&B perfetto per le tue esigenze. <br/>Tutto quello che stai cercando è qui!</h1>
          <p className="font-playfair text-2xl md:text-2xl md:text-[28px] md:leading-14 font-bold md:font-extrabold max-w-xl ">Il B&B luxury di Palermo</p>
            <form onSubmit={handleSearch} className='bg-white text-gray-500 rounded-lg px-6 py-4 mt-8 flex flex-col md:flex-row max-md:items-start gap-4 max-md:mx-auto'>

              <div>
                  <div className='flex items-center gap-2'>
                      <img src = {assets.calenderIcon} alt = "" className="h-4" />
                      <label htmlFor="checkIn">Check in</label>
                  </div>
                  <input id="checkIn" required type="date" value={searchData.checkIn} onChange={handleChange} className=" rounded border border-gray-200 px-3 py-1.5 mt-1.5 text-sm outline-none" />
              </div>

              <div>
                  <div className='flex items-center gap-2'>
                    <img src = {assets.calenderIcon} alt = "" className="h-4" />
                    <label htmlFor="checkOut">Check out</label>
                  </div>
                  <input id="checkOut" required type="date" value={searchData.checkOut} onChange={handleChange} className=" rounded border border-gray-200 px-3 py-1.5 mt-1.5 text-sm outline-none" />
              </div>

              <div className='flex md:flex-col max-md:gap-2 max-md:items-center'>
                  <label htmlFor="guests">Guests</label>
                  <input min={1} max={4} id="guests" type="number" value={searchData.guests} onChange={handleChange} className=" rounded border border-gray-200 px-3 py-1.5 mt-1.5 text-sm outline-none max-w-16 " placeholder="1" />
              </div>

              <button type="submit" className='flex items-center justify-center gap-1 rounded-md bg-black py-3 px-4 text-white my-auto cursor-pointer max-md:w-full max-md:py-1'>
                  <img src = {assets.searchIcon} alt = "" className="h-7" />
                  <span>Search</span>
              </button>
          </form>
          
        </div>
      
  )
}

export default Hero

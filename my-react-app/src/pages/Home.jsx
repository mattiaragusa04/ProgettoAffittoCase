import React from 'react'
import Hero from '../components/Hero'
import FeaturedDestionation from '../components/FeaturedDestionation'
import Cookies from '../components/Cookies'
import OfferteEsclusive from '../components/OfferteEsclusive'
import Recensioni from '../components/Recensioni'
function Home() {
  return (
    <div>
      <Hero />
      <FeaturedDestionation />
      <Cookies />
      <OfferteEsclusive />
      <Recensioni />
    </div>
  )
}

export default Home

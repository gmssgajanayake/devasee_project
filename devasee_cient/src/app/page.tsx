import {AdvertisementSlides} from "@/app/_components/AdvertisementSlides";
import TopCategories from "@/app/_components/TopCategories";
import NewRelease from "@/app/_components/NewRelease";
import BooksAdvertisementSlides from "@/app/_components/BooksAdvertisementSlides";
import OffersSlides from "@/app/_components/OffersSlides";
import NewsSubscribe from "@/app/_components/NewsSubscribe";
import Article from "@/app/_components/Article";
import Head from "@/app/head";
// import Maintenance from "@/components/Maintenance";

export default function Home() {
  return (
      <div className={"bg-white"}>
          {/*<Maintenance/>*/}
          <Head/>
          <AdvertisementSlides/>
          <TopCategories/>
          <NewRelease/>
          <BooksAdvertisementSlides/>
          <OffersSlides/>
          <NewsSubscribe/>
          <Article/>

      </div>
  );
}

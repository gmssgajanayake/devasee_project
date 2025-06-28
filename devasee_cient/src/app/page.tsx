import {AdvertisementSlides} from "@/app/_components/AdvertisementSlides";
import TopCategories from "@/app/_components/TopCategories";
import NewRelease from "@/app/_components/NewRelease";
// import Maintenance from "@/components/Maintenance";

export default function Home() {
  return (
      <div className={"bg-white"}>
          {/*<Maintenance/>*/}
          <AdvertisementSlides/>
          <TopCategories/>
          <NewRelease/>
          {/*Want to implement other sub-sections*/}
      </div>
  );
}

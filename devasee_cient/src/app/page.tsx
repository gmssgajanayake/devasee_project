import {AdvertisementSlides} from "@/app/_components/AdvertisementSlides";
// import Maintenance from "@/components/Maintenance";

export default function Home() {
  return (
      <div className={"bg-white"}>
          {/*<Maintenance/>*/}
          <AdvertisementSlides/>
          <div className={"h-screen bg-blue-300"}></div>
          {/*Want to implement other sub-sections*/}
      </div>
  );
}

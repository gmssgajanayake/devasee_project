import Image from "next/image";
import logo from "@/assets/devasee logo.png";
import Link from "next/link";
// import ContactBar from "@/app/_components/ContactBar";

// import MainNavBar from "@/app/_components/MainNavBar";

export default function Home() {
  return (
      <div className={"w-full h-screen flex flex-col justify-center"}>
          {/*<ContactBar/>*/}
          <div className={"bg-neutral-50 h-full  flex flex-col items-center gap-5 justify-center"}>
              <Image
                  src={logo}
                  alt="Devasee logo"
                  className="w-36 h-auto bg-white rounded-full"
                  priority
              />
              <div className={"flex flex-col items-center justify-center"}>
                  <h1 className={"text-xl md:text-3xl text-gray-800 font-bold"}>Welcome to Devasee Bookshop</h1>
                  <p className={"text-sm md:text-lg text-gray-800"} ><Link className={"underline cursor-pointer"} href={"#"}>www.devasee.lk</Link><span className={""}> under maintenance</span></p>
                  <p className={"text-gray-800                                                text-xs md:text-sm"}>This website will be available soon ...</p>
              </div>
          </div>
      </div>

      /*<div>
          <MainNavBar/>
          Home page
      </div>*/
  );
}

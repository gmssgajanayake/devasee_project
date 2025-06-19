import Image from "next/image";
import logo from "@/assets/devasee logo.png";

export default function Home() {
  return (
      <div className={"w-full h-screen flex flex-col items-center gap-5 justify-center"}>
          <Image
              src={logo}
              alt="Devasee logo"
              className="w-36 h-auto"
              priority
          />
          <div className={"flex flex-col items-center justify-center"}>
              <h1 className={"text-xl md:text-3xl"}>Welcome to Devasee Bookshop</h1>
              <p className={"text-sm md:text-lg"} ><a>www.devasee.lk</a> <span className={"text-red-500"}> under maintenance</span></p>
          </div>
      </div>
  );
}

import Image from "next/image";
import logo from "@/assets/devasee logo.png";
import Link from "next/link";

export default function Home() {
  return (
      <div className={"bg-neutral-50 w-full h-screen flex flex-col items-center gap-5 justify-center"}>
          <Image
              src={logo}
              alt="Devasee logo"
              className="w-36 h-auto bg-white rounded-full"
              priority
          />
          <div className={"flex flex-col items-center justify-center"}>
              <h1 className={"text-xl md:text-3xl text-gray-800 font-bold"}>Welcome to Devasee Bookshop</h1>
              <p className={"text-sm md:text-lg text-gray-800"} ><Link className={"underline cursor-pointer"} href={"#"}>www.devasee.lk</Link><span className={""}> under maintenance</span></p>
              <p className={"text-gray-800 text-sm"}>This website will be available soon ...</p>
          </div>
      </div>
  );
}

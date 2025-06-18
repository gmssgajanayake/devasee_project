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
            <h1 className={"text-4xl"}>Wellcome to Devasee Bookshop</h1>
            <p> <a>www.devasee.com</a> <span className={"text-red-500"}> under maintenance</span> </p>
        </div>


    </div>
  );
}

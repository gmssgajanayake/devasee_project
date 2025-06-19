import Image from "next/image";
import logo from "@/assets/devasee logo.png";

export default function NotFoundPage(){
    return (
        <div className={"w-full h-screen flex flex-col items-center gap-5 justify-center"}>
            <Image
                src={logo}
                alt="Devasee logo"
                className="w-36 h-auto  bg-white rounded-full"
                priority
            />
            <div className={"flex flex-col items-center justify-center"}>
                <h3 className={"text-2xl font-medium"}>404 | Page Not Found </h3>
            </div>


        </div>
    );
}
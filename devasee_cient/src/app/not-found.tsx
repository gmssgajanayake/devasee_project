import Image from "next/image";
import logo from "@/assets/devasee.png";
import ContactBar from "@/app/_components/ContactBar";

export default function NotFoundPage() {
    return (
        <div className={"w-full h-screen flex flex-col justify-center"}>
            <ContactBar/>
            <div className={"w-full bg-blue-50 h-full flex flex-col items-center gap-5 justify-center"}>
                <Image
                    src={logo}
                    alt="Devasee logo"
                    className="w-36 h-auto rounded-full"
                    priority
                />
                <div className={"flex flex-col items-center justify-center"}>
                    <h3 className={"text-2xl font-medium dark:text-gray-800"}>404 | Page Not Found </h3>
                </div>
            </div>
        </div>

    );
}
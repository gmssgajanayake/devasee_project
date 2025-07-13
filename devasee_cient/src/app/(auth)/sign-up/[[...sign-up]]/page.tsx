import { SignUp} from "@clerk/nextjs";
import Image from "next/image";
import loginImage from "@/assets/devasee login page.png";

export default function Page() {
    return (
        <div className={"w-full mt-32   mb-10 h-auto flex justify-center items-center"}>
            <div className="w-1/2 px-8 h-full py-24  bg-white  gap-3  flex flex-col justify-center items-center">
                <h2 className="text-4xl text-gray-700 text-center font-semibold">Create Account | Devasee Bookshop & Printing</h2>
                <div className="flex justify-center items-start ">
                    <p className={"pl-2 mt-24 text-gray-600 text-center italic"}>
                        &quot;Join Devasee Bookshop to discover and order books, manage your printing needs, and enjoy a personalized shopping experience. Sign up in seconds and get started today.&quot;
                    </p>

                    <Image src={loginImage} className={"w-96 h-auto"} alt={""}/>
                </div>
            </div>

            <div className="w-1/2 py-12 bg-[#0000ff]  flex justify-center items-center">
                <SignUp />
            </div>
        </div>
    );
}
import {SignIn} from '@clerk/nextjs'
import loginImage from "@/assets/devasee login page.png";
import Image from "next/image";

export default function Page() {
    return (
        <div className={"w-full mt-32  mb-10 h-auto flex justify-center items-center"}>
            <div className="w-1/2 p-8 bg-white  gap-3  flex flex-col justify-center items-center">
                <h2 className="text-4xl text-gray-700 text-center font-semibold">Login | Devasee Bookshop & Printing</h2>
                <div className="flex justify-center items-start ">
                    <p className={"pl-2 mt-24 text-gray-600 text-center italic"}>
                        &quot;Access your Devasee Bookshop account to track orders, manage purchases, and explore a wide range of books and printing services. Fast, secure login for book lovers and students.&quot;
                    </p>

                    <Image src={loginImage} className={"w-96 h-auto"} alt={""}/>
                </div>
            </div>

            <div className="w-1/2  bg-[#0000ff] py-12  flex justify-center items-center">
                <SignIn />
            </div>
        </div>
    )
}
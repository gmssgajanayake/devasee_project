import c1 from "@/assets/categoryies image/img.png";
import c2 from "@/assets/categoryies image/img_3.png";
import c3 from "@/assets/categoryies image/img_2.png";
import Image from "next/image";
import MainButton from "@/components/MainButton";
import Link from "next/link";

export default function TopCategories(){
    return (
        <div className="flex flex-col items-start justify-start w-screen h-auto  bg-white">
            <div className={"flex justify-start px-4 md:px-8 items-center gap-2 mb-5 mt-5 md:mt-10  w-full"}>
                <div className={"h-0.5 border border-[#0000ff] rounded-full w-10 bg-[#0000ff]"}></div>
                <p className={"font-bold text-[#0000ff] text-sm tracking-widest"}>Categories</p>
            </div>
            <div className={" w-full px-4 md:px-8  flex items-center justify-start"}>
                <h2 className={"font-bold text-[#2b216d] text-lg md:text-3xl  w-full"}>Explore our Top Categories</h2>
            </div>
            {/*Books are displayed area*/}
            <div className={"w-full px-4 gap-8 md:gap-4 md:px-8   flex-col md:flex-row flex items-center justify-start  my-8"}>
                <div className={"md:w-1/3 h-auto flex items-center justify-center flex-col gap-8"}>
                    <Image src={c1} alt={"Category Image"} className={"w-full h-64"}/>
                    <h2 className={"text-xl text-center font-bold text-[#2b216d]"}>Higher Education</h2>
                </div>
                <div className={"md:w-1/3 h-auto flex items-center justify-center flex-col gap-8"}>
                    <Image src={c2} alt={"Category Image"} className={"w-full h-64"}/>
                    <h2 className={"text-xl text-center font-bold text-[#2b216d]"}>Management Books</h2>
                </div>
                <div className={"md:w-1/3 h-auto flex items-center justify-center flex-col gap-8"}>
                    <Image src={c3} alt={"Category Image"} className={"w-full h-64"}/>
                    <h2 className={"text-xl text-center font-bold text-[#2b216d]"}>Engineering Books</h2>
                </div>
            </div>
            <Link href={"products"} className={"w-full flex text-center mb-12  justify-center items-center  "}>
                <MainButton name={"VIEW MORE"}/>
            </Link>
        </div>
    );
}
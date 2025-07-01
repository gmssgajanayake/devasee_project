import Link from "next/link";
import Image from "next/image";
import logo from "@/assets/devasee logo.png";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFacebook, faLinkedinIn, faXTwitter, faYoutube} from "@fortawesome/free-brands-svg-icons";

export default function Footer() {
    return (
        <footer className="bg-white overflow-hidden h-auto w-screen flex justify-between items-center flex-col">
            <div className="w-full hidden md:flex p-10  justify-start items-start">
                <div className={" w-1/3 p-14  gap-8 flex flex-col justify-center items-start"}>
                    <Link href="/">
                        <Image
                            src={logo}
                            alt={"logo"}
                            priority
                            className="h-10 w-10 lg:w-16 lg:h-16"
                        />
                    </Link>

                    <p className={"text-[#2b216d]  text-sm md:text-lg"}>Devasee&nbsp;—&nbsp;Trusted bookshop and printing services for every need.</p>

                    <div className="w-full flex items-center justify-between">
                        <FontAwesomeIcon icon={faFacebook} className={"text-xl lg:text-3xl text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faLinkedinIn} className={"text-xl lg:text-3xl text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faXTwitter} className={"text-xl lg:text-3xl text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faYoutube} className={"text-xl lg:text-3xl text-[#0000ff]"}/>

                    </div>

                </div>
                <div className={"w-1/3 lg:w-1/4 p-14  gap-8 flex flex-col justify-center items-start"}>
                    <p className="font-bold text-lg md:text-xl text-[#2b216d]">Pages</p>
                    <div className="w-full flex flex-col gap-2 justify-center items-start">
                        <Link href="/about" className="text-sm md:text-lg text-[#2b216d]  hover:underline">Home</Link>
                        <Link href="/about" className=" text-sm md:text-lg text-[#2b216d]  hover:underline">About&nbsp;Us</Link>
                        <Link href="/products" className="text-sm md:text-lg text-[#2b216d]  hover:underline">Products</Link>
                        <Link href="/services" className= "text-sm md:text-lg  text-[#2b216d]  hover:underline">Printing&nbsp;Services</Link>
                        <Link href="/contact" className="text-sm md:text-lg text-[#2b216d]  hover:underline">Contact&nbsp;Us</Link>
                    </div>
                </div>
                <div className={"w-1/3 p-12 gap-8 flex flex-col justify-center items-start"}>
                    <p className="font-bold text-lg md:text-xl ml-8 text-[#2b216d]">Importent&nbsp;Links</p>
                    <div className="w-full flex ml-8 flex-col gap-2 justify-center items-start">
                        <Link href="/about" className="text-sm md:text-lg text-[#2b216d]  hover:underline">Privacy&nbsp;Policy</Link>
                        <Link href="/about" className= "text-sm md:text-lg text-[#2b216d]  hover:underline">FAQs</Link>
                        <Link href="/products" className=" text-sm md:text-lg text-[#2b216d]  hover:underline">Terms&nbsp;of&nbsp;Service</Link>
                    </div>
                    <div className={"gap-4 flex flex-col"}>
                        <div className={"flex gap-20"}>
                            <div className="w-3 h-3 bg-[#f8bfb8]"></div>
                            <div className="w-3 h-3 bg-[#fae0b1]"></div>
                        </div>
                        <div className={"ml-10 flex gap-20"}>
                            <div className="w-3 h-3 bg-[#fae0b1]"></div>
                            <div className="w-3 h-3 bg-[#f8bfb8]"></div>
                        </div>
                    </div>

                </div>
                <div className={"gap-4 hidden  pt-48 lg:flex flex-col pr-10"}>
                    <div className={"flex gap-20"}>
                        <div className="w-3 h-3 bg-[#a2a2a2]"></div>
                        <div className="w-3 h-3 bg-[#cfc9ff]"></div>
                    </div>
                    <div className={"ml-10 flex gap-20"}>
                        <div className="w-3 h-3 bg-[#cfc9ff]"></div>
                        <div className="w-3 h-3 bg-[#a2a2a2]"></div>
                    </div>
                </div>
            </div>
            <div className={"md:hidden flex justify-center w-full items-start p-8 "}>
                <div className="w-1/3 flex flex-col p-2 gap-4 ">
                    <Link href="/">
                        <Image
                            src={logo}
                            alt={"logo"}
                            priority
                            className="h-10 w-10 lg:w-16 lg:h-16"
                        />
                    </Link>
                    <p className={"text-[#2b216d]  text-[12px] md:text-lg"}>Devasee&nbsp;—&nbsp;Trusted bookshop and printing services for every need.</p>
                    <div className="w-full flex items-center justify-between">
                        <FontAwesomeIcon icon={faFacebook} className={"  text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faLinkedinIn} className={" text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faXTwitter} className={"  text-[#0000ff]"}/>
                        <FontAwesomeIcon icon={faYoutube} className={" text-[#0000ff]"}/>
                    </div>
                </div>
                <div className="w-1/3 p-2 pl-8 flex flex-col gap-2 ">
                    <p className="font-bold text-sm md:text-xl text-[#2b216d]">Pages</p>
                    <div className="w-full flex flex-col gap-2 justify-center items-start">
                        <Link href="/about" className="text-xs md:text-lg text-[#2b216d]  hover:underline">Home</Link>
                        <Link href="/about" className=" text-xs md:text-lg text-[#2b216d]  hover:underline">About&nbsp;Us</Link>
                        <Link href="/products" className="text-xs md:text-lg text-[#2b216d]  hover:underline">Products</Link>
                        <Link href="/services" className= "text-xs md:text-lg  text-[#2b216d]  hover:underline">Printing&nbsp;Services</Link>
                        <Link href="/contact" className="text-xs md:text-lg text-[#2b216d]  hover:underline">Contact&nbsp;Us</Link>
                    </div>
                </div>
                <div className="w-1/3 p-2 flex flex-col h-full gap-2 ">
                    <p className="font-bold text-sm md:text-xl ml-8 text-[#2b216d]">Importent Links</p>
                    <div className="w-full flex ml-8 flex-col gap-2 justify-center items-start">
                        <Link href="/about" className="text-xs md:text-lg text-[#2b216d]  hover:underline">Privacy&nbsp;Policy</Link>
                        <Link href="/about" className= "text-xs md:text-lg text-[#2b216d]  hover:underline">FAQs</Link>
                        <Link href="/products" className=" text-xs md:text-lg text-[#2b216d]  hover:underline">Terms&nbsp;of Service</Link>
                    </div>
                </div>
            </div>
            <div className="bg-[#0000ff] w-screen flex items-center justify-between px-4  md:px-20 py-3">
                <p className="text-white text-xs md:text-sm tracking-wide">&copy; 2025 DEVASEE. All Rights Reserved.</p>
                <p className="font-bold text-white text-xs md:text-sm ">Privacy | Terms of Service</p>
            </div>
        </footer>
    );
}
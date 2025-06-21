import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPhone} from "@fortawesome/free-solid-svg-icons/faPhone";
import { faFacebookF ,faLinkedinIn,faXTwitter,faThreads,faInstagram} from "@fortawesome/free-brands-svg-icons";


export default function ContactBar(){
    return (
        <nav className="w-full h-auto flex justify-center md:justify-between bg-[#0000FF] pl-8 pr-8 pt-1.5 pb-1.5">
            <div className={" flex justify-center items-center"}>
                <FontAwesomeIcon icon={faPhone} className="mr-2 size-3.5 text-gray-50 " />
                <a href="tel:+94342244909" className="text-gray-50 font-bold">
                    +94 34 224 4909
                </a>
            </div>
            <div className={"hidden md:flex justify-between w-[150] items-center"}>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <FontAwesomeIcon icon={faFacebookF} className="size-2" />
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <FontAwesomeIcon icon={faInstagram} className="size-3"/>
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <FontAwesomeIcon icon={faLinkedinIn} className="size-3"/>
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <FontAwesomeIcon icon={faXTwitter} className="size-3" />
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <FontAwesomeIcon icon={faThreads} className="size-3"/>
                </Link>

            </div>
        </nav>
    );
}
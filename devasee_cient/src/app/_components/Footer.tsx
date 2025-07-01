import Link from "next/link";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faFacebookF,
    faLinkedinIn,
    faXTwitter,
    faYoutube,
} from "@fortawesome/free-brands-svg-icons";

export default function Footer() {
    return (
        <footer className="bg-white border-t text-blue-950 w-full">
            <div className="max-w-7xl mx-auto px-6 py-10 flex flex-col md:flex-row gap-10 md:gap-0 justify-between items-start">
                {/* Left Section */}
                <div className="flex-1 min-w-[250px] flex flex-col items-start gap-4">
                    {/* Logo */}
                    <div>
                        {/* <img
                            src="/logo.png"
                            alt="Devasee Logo"
                            className="w-36 h-auto mb-3"
                        />*/}
                    </div>
                    {/* Description */}
                    <p className="text-sm text-gray-700 mb-2">
                        Devasee is a leading bookshop and professional printing service based in Sri Lanka, delivering high-quality products and innovative print solutions.
                    </p>
                    {/* Social Media Icons */}
                    <div className="flex gap-5 mt-1">
                        <Link
                            href="https://www.facebook.com/devaseepvtltd/"
                            target="_blank"
                            rel="noopener noreferrer"
                            aria-label="Facebook"
                            className="text-blue-600 hover:text-blue-800"
                        >
                            <FontAwesomeIcon icon={faFacebookF} className="w-5 h-5" />
                        </Link>
                        <Link
                            href="https://www.linkedin.com/company/devasee"
                            target="_blank"
                            rel="noopener noreferrer"
                            aria-label="LinkedIn"
                            className="text-blue-800 hover:text-blue-600"
                        >
                            <FontAwesomeIcon icon={faLinkedinIn} className="w-5 h-5" />
                        </Link>
                        <Link
                            href="https://x.com/Devaseelk"
                            target="_blank"
                            rel="noopener noreferrer"
                            aria-label="X (Twitter)"
                            className="text-black hover:text-blue-500"
                        >
                            <FontAwesomeIcon icon={faXTwitter} className="w-5 h-5" />
                        </Link>
                        <Link
                            href="https://www.youtube.com/@devasee"
                            target="_blank"
                            rel="noopener noreferrer"
                            aria-label="YouTube"
                            className="text-red-600 hover:text-red-700"
                        >
                            <FontAwesomeIcon icon={faYoutube} className="w-5 h-5" />
                        </Link>
                    </div>
                </div>
                {/* Right Section */}
                <div className="flex-1 min-w-[180px] flex flex-col items-start mt-8 md:mt-0">
                    <h3 className="text-lg font-semibold mb-4">Company</h3>
                    <ul className="flex flex-col gap-3 text-sm">
                        <li>
                            <Link href="/" className="hover:text-blue-700 transition">Home</Link>
                        </li>
                        <li>
                            <Link href="/about" className="hover:text-blue-700 transition">About Us</Link>
                        </li>
                        <li>
                            <Link href="/books" className="hover:text-blue-700 transition">Books</Link>
                        </li>
                        <li>
                            <Link href="/printing" className="hover:text-blue-700 transition">Printing</Link>
                        </li>
                        <li>
                            <Link href="/contact" className="hover:text-blue-700 transition">Contact Us</Link>
                        </li>
                    </ul>
                </div>
            </div>
        </footer>
    );
}
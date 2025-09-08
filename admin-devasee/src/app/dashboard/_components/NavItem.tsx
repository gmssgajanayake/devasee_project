"use client";

import Link from "next/link";

interface NavItemProps {
    href: string;
    label: string;
    active?: boolean;
}

export default function NavItem({ href, label, active }: NavItemProps) {
    return (
        <Link
            href={href}
            className={`block rounded-lg px-4 py-3 font-medium transition ${
                active
                    ? "bg-blue-500 text-white shadow-md"
                    : "bg-gray-100 hover:bg-gray-200"
            }`}
        >
            {label}
        </Link>
    );
}

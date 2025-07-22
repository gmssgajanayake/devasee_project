"use client";

import { usePathname } from "next/navigation";
import Head from "next/head";

export default function Canonical() {
    const pathname = usePathname();
    const canonicalUrl = `https://www.devasee.lk${pathname}`;

    return (
        <Head>
            <link rel="canonical" href={canonicalUrl} />
        </Head>
    );
}

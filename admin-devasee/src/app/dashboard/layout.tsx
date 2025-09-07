'use client';

import {useEffect} from "react";
import {UserButton, useUser} from "@clerk/nextjs";
import {useRouter} from "next/navigation";
import {PropsWithChildren} from "react";

export default function DashboardLayout({children}: PropsWithChildren) {
    const {isSignedIn, isLoaded} = useUser();
    const router = useRouter();

    useEffect(() => {
        if (isLoaded && !isSignedIn) {
            router.replace("/sign-in");
        }
    }, [isLoaded, isSignedIn, router]);

    if (!isLoaded) {
        return <div className="flex items-center justify-center h-screen">Loading...</div>;
    }

    return (
        <div className="">
            {children}
            <UserButton
                afterSignOutUrl="/sign-in"
            />
        </div>
    );
}

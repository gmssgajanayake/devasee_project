import React from "react";
import Footer from "@/app/_components/Footer";

export default function RouterLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            <div className={"w-screen"}>{children}</div>
            <Footer/>
        </div>
    );
}

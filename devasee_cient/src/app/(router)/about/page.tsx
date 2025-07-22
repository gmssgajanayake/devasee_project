import Maintenance from "@/components/Maintenance";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import React from "react";
import Head from "@/app/(router)/about/head";

export default function Page(){
    return (
        <>
            <Head/>
            <div>
                <SubNavBar path="ABOUT" />
                <Maintenance />
            </div>
        </>

    );
}
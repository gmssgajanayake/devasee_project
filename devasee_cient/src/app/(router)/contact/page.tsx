import React from "react";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Maintenance from "@/components/Maintenance";
import Head from "@/app/(router)/contact/head";

export default function Page() {
    return (
        <>
            <Head/>
            <div>
                <SubNavBar path="CONTACT" />
                <Maintenance />
            </div>
       </>

    );
}

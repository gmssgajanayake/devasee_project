import Maintenance from "@/components/Maintenance";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import React from "react";
import Head from "@/app/(router)/services/head";


export default function Page() {
    return (
        <>
            <Head/>
            <div>
                <SubNavBar path={"SERVICES"}/>
                <Maintenance/>
            </div>
        </>
    );
}
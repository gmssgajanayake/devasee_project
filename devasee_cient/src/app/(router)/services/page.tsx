import Maintenance from "@/components/Maintenance";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import React from "react";

export default function Page(){
    return (
        <div>
            <SubNavBar path={"SERVICES"}/>
            <Maintenance/>
        </div>
    );
}
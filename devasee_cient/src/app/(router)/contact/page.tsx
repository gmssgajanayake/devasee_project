import React from "react";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Maintenance from "@/components/Maintenance";

export default function Page() {
    return (
        <div>
            <SubNavBar path="CONTACT" />
            <Maintenance />
        </div>
    );
}

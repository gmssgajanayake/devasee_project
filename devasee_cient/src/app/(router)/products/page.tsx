/*import Maintenance from "@/components/Maintenance";*/


import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Maintenance from "@/components/Maintenance";
import React from "react";

export default function Page(){
    return (

        <div className={" w-full"}>
            <div>
                <SubNavBar path="PRODUCTS" />
                <Maintenance />
            </div>
        </div>
        /*<Maintenance/>*/
        /*<div>
            <p>Products page</p>
        </div>*/
    );
}
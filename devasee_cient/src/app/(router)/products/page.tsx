import SubNavBar from "@/app/(router)/_components/SubNavBar";
import React from "react";
import Container from "@/app/(router)/products/_components/Container";
import FilterBar from "@/app/(router)/products/_components/FilterBar";


export default function Page(){
    return (

        <div className={" w-full"}>
            <div>
                <SubNavBar path="PRODUCTS" />
                {/*<Maintenance />*/}
                <div className={"flex items-center justify-center  w-full  h-auto"}>
                    <FilterBar/>
                    <Container/>
                </div>
            </div>
        </div>
    );
}
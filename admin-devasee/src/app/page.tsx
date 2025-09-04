"use client";
import {useEffect} from "react";
import {redirect} from "next/navigation";

export default function LoginPage() {
    useEffect(() => {
        //redirect("http://localhost:3000/sign-in");
        redirect("https://admin.devasee.lk/sign-in");
    }, []);
}

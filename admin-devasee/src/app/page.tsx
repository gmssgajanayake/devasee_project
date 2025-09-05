"use client";
import {useEffect} from "react";
import {redirect} from "next/navigation";

export default function LoginPage() {
    useEffect(() => {
        redirect("/sign-in");
    }, []);
}

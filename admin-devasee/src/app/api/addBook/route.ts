// src/app/api/addBook/route.ts
import { NextResponse } from "next/server";
import { auth } from "@clerk/nextjs/server";

export async function POST(req: Request) {
    const { getToken } = await auth();
    const token = await getToken({ template: "devasee_user_token" });
    console.log(token)


    const formData = await req.formData();

    const res = await fetch("http://api.devasee.lk/api/v1/product/book/admin/addBook", {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: formData
    });

    const data = await res.json();
    return NextResponse.json(data, { status: res.status });
}

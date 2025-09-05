// src/lib/actions.ts
"use server";

import { auth } from "@clerk/nextjs/server";

export async function addBook(formData: FormData) {

    try {

       // const token = getJWTToken();
        const token ="eyJhbGciOiJSUzI1NiIsImNhdCI6ImNsX0I3ZDRQRDIyMkFBQSIsImtpZCI6Imluc18yemt5Q3ZzcllvdFV2RWRmTTdFTURKZm4zZjUiLCJ0eXAiOiJKV1QifQ.eyJhenAiOiJodHRwczovL3d3dy5kZXZhc2VlLmxrIiwiZW1haWwiOiIyMDIxc3AwMjZAdW5pdi5qZm4uYWMubGsiLCJlbWFpbFZlcmlmaWVkIjp0cnVlLCJleHAiOjE3NTcwOTM1ODEsImZpcnN0TmFtZSI6IjIwMjFzcDAyNiIsImlhdCI6MTc1NzA4OTk4MSwiaW1hZ2VVcmwiOiJodHRwczovL2ltZy5jbGVyay5jb20vZXlKMGVYQmxJam9pWkdWbVlYVnNkQ0lzSW1scFpDSTZJbWx1YzE4eWVtdDVRM1p6Y2xsdmRGVjJSV1JtVFRkRlRVUktabTR6WmpVaUxDSnlhV1FpT2lKMWMyVnlYek15U0dsdFpUSm1VakpqU0V4M1JtNVRWbXBFYkVkSFdHdGtheUlzSW1sdWFYUnBZV3h6SWpvaU1sTWlmUSIsImlzcyI6Imh0dHBzOi8vY2xlcmsuZGV2YXNlZS5sayIsImp0aSI6IjA2ZGJlMzVmMTc0NWM3YzFkNDdkIiwibGFzdE5hbWUiOiJTYWt1amEgU2hhbWFsIEdhamFuYXlha2UiLCJuYmYiOjE3NTcwODk5NzYsIm9yZ0lkIjpudWxsLCJvcmdOYW1lIjpudWxsLCJvcmdSb2xlIjpudWxsLCJwaG9uZU51bWJlclZlcmlmaWVkIjpmYWxzZSwicHJpbWFyeUVtYWlsQWRkcmVzcyI6IjIwMjFzcDAyNkB1bml2Lmpmbi5hYy5sayIsInByaW1hcnlQaG9uZU51bWJlciI6bnVsbCwicHJpbWFyeVdlYjNXYWxsZXQiOm51bGwsInN1YiI6InVzZXJfMzJIaW1lMmZSMmNITHdGblNWakRsR0dYa2RrIiwidXNlcklkIjoidXNlcl8zMkhpbWUyZlIyY0hMd0ZuU1ZqRGxHR1hrZGsiLCJ1c2VybmFtZSI6InNoYW1hbCJ9.XvEtms7GiHIz5FcW8_TEleMLLYtYlFwFrHUHh5nlaMuK83FniUUPbE_dOAxDxS4lfX1wuJWMUPJkAYQJsIYr1h66ApxENuXnLPW9hZJ4kU9BTdMzGGFIlyEqee8xHhjczwhluYfmadNlUSlzHBnVdkcQXmHf8sjU8BBGG0aPKC0aM5Is9bPQekRpKDsckwat-GS-tI8I8XS8GWeqOnZ7x-08Jmx0TGQWbFJ_PmupMiDEGe6_nnlPd4mVnWgItTjR7BS5x4xbG3iNzd119pQP3xgEIy8pOLMIqRp8waR142Wvf1NyyJXrfpaehFqlvNqJqGxzxq0c5usjIjpCAE1HSQ"


        if (!token) {
            throw new Error("Missing authentication token");
        }

        console.log(formData)

        const response = await fetch(
            "http://api.devasee.lk/api/v1/product/book/admin/addBook",
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                body: formData,
            }
        );

        console.log(response)

        if (!response.ok) {
            throw new Error(`Failed to add book: ${response.statusText}`);
        }

        return await response.json();
    } catch (error) {
        console.error("Error adding book:", error);
        throw error;
    }
}

export  async function getJWTToken(){
    const { getToken } = await auth();
    const token = await getToken({ template: "devasee_user_token" });
    return token;
}

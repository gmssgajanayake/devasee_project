// src/lib/actions.ts
"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {
    console.log()

    try {
        const token = JWTtoken;

        //testing token
        //const token = "eyJhbGciOiJSUzI1NiIsImNhdCI6ImNsX0I3ZDRQRDIyMkFBQSIsImtpZCI6Imluc18yemt5Q3ZzcllvdFV2RWRmTTdFTURKZm4zZjUiLCJ0eXAiOiJKV1QifQ.eyJhenAiOiJodHRwczovL2FkbWluLmRldmFzZWUubGsiLCJlbWFpbCI6IjIwMjFzcDAyNkB1bml2Lmpmbi5hYy5sayIsImVtYWlsVmVyaWZpZWQiOnRydWUsImV4cCI6MTc1NzM1MjEzMSwiZmlyc3ROYW1lIjoiMjAyMXNwMDI2IiwiaWF0IjoxNzU3MzE2MTMxLCJpbWFnZVVybCI6Imh0dHBzOi8vaW1nLmNsZXJrLmNvbS9leUowZVhCbElqb2laR1ZtWVhWc2RDSXNJbWxwWkNJNkltbHVjMTh5ZW10NVEzWnpjbGx2ZEZWMlJXUm1UVGRGVFVSS1ptNHpaalVpTENKeWFXUWlPaUoxYzJWeVh6TXlTR2x0WlRKbVVqSmpTRXgzUm01VFZtcEViRWRIV0d0a2F5SXNJbWx1YVhScFlXeHpJam9pTWxNaWZRIiwiaXNzIjoiaHR0cHM6Ly9jbGVyay5kZXZhc2VlLmxrIiwianRpIjoiNjY2ODJmMTdlNTg5N2RlNTU1ZTQiLCJsYXN0TmFtZSI6IlNha3VqYSBTaGFtYWwgR2FqYW5heWFrZSIsIm5iZiI6MTc1NzMxNjEyNiwib3JnSWQiOm51bGwsIm9yZ05hbWUiOm51bGwsIm9yZ1JvbGUiOm51bGwsInBob25lTnVtYmVyVmVyaWZpZWQiOmZhbHNlLCJwcmltYXJ5RW1haWxBZGRyZXNzIjoiMjAyMXNwMDI2QHVuaXYuamZuLmFjLmxrIiwicHJpbWFyeVBob25lTnVtYmVyIjpudWxsLCJwcmltYXJ5V2ViM1dhbGxldCI6bnVsbCwic3ViIjoidXNlcl8zMkhpbWUyZlIyY0hMd0ZuU1ZqRGxHR1hrZGsiLCJ1c2VySWQiOiJ1c2VyXzMySGltZTJmUjJjSEx3Rm5TVmpEbEdHWGtkayIsInVzZXJuYW1lIjoic2hhbWFsIn0.vm5inDN30IjY8lae-eSWxpE3uAmeKr7US6VUwO98S5-UKV9aw9ZyHwlfGvymYcE8cZRoGSv4I55B32yP7mFp3-Ib9FfIu3KLI9D6BJuPPUdw2MCzoPAwK5XUYzd_E_8hPP4B6I7ooFTKMSmx6Ht7gYl7WW8TSIKTBEaPcrWy-1WAi-cMtFtqjgjM1RPZ9L_zvajMuM2HRxz-hLnyLhiDwI1LLMFHaYP53GZy1Y_23Zgpo4SP0DnbXHxlPKOu1zeSbXdtYbbwR9n6jVfuzYMBpp6y5dqw89xRgOJ3sZQGAPaqJn-mIsGGtlWLttIqvFRsBkZBAiDxKgxK8nKm-OZmtg"
        if (!token) {
            throw new Error("Missing authentication token");
        }

        // Build FormData properly
        const body = new FormData();

        console.log(formData)

        body.append(
            "book",
            JSON.stringify({
                title: formData.get('title'),
                author:formData.get('author'),
                publisher: formData.get('publisher'),
                category: formData.get('category'),
                description:formData.get('description'),
                language: formData.get('language'),
                price: formData.get('price'),
                initialQuantity: formData.get('stockQuantity'),
                isbn: formData.get('isbn')
            })
        );

        const file = formData.get("file") as File | null;
        if (file) {
            body.append("file", file);
        }

        const response = await fetch(
            "http://api.devasee.lk/api/v1/product/book/admin/addBook",
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    // ⚠️ Don't set Content-Type manually; fetch will set correct boundary
                },
                body,
            }
        );

        if (!response.ok) {
            const errorBody = await response.text();
            console.error("API Error Response:", errorBody);
            throw new Error(
                `Failed to add book: ${response.status} ${response.statusText}`
            );
        }

        return await response.json();
    } catch (error) {
        console.error("Error adding book:", error);
        throw error;
    }
}

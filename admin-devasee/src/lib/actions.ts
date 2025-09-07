// src/lib/actions.ts
"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {
    console.log()

    try {
        const token = JWTtoken;

        //testing token
        //const token = "eyJhbGciOiJSUzI1NiIsImNhdCI6ImNsX0I3ZDRQRDIyMkFBQSIsImtpZCI6Imluc18yemt5Q3ZzcllvdFV2RWRmTTdFTURKZm4zZjUiLCJ0eXAiOiJKV1QifQ.eyJhenAiOiJodHRwczovL2FkbWluLmRldmFzZWUubGsiLCJlbWFpbCI6IjIwMjFzcDAyNkB1bml2Lmpmbi5hYy5sayIsImVtYWlsVmVyaWZpZWQiOnRydWUsImV4cCI6MTc1NzE4MDY4OSwiZmlyc3ROYW1lIjoiMjAyMXNwMDI2IiwiaWF0IjoxNzU3MTQ0Njg5LCJpbWFnZVVybCI6Imh0dHBzOi8vaW1nLmNsZXJrLmNvbS9leUowZVhCbElqb2laR1ZtWVhWc2RDSXNJbWxwWkNJNkltbHVjMTh5ZW10NVEzWnpjbGx2ZEZWMlJXUm1UVGRGVFVSS1ptNHpaalVpTENKeWFXUWlPaUoxYzJWeVh6TXlTR2x0WlRKbVVqSmpTRXgzUm01VFZtcEViRWRIV0d0a2F5SXNJbWx1YVhScFlXeHpJam9pTWxNaWZRIiwiaXNzIjoiaHR0cHM6Ly9jbGVyay5kZXZhc2VlLmxrIiwianRpIjoiZGQ2OWZlMzI3MzRiNTg1YWE1NjkiLCJsYXN0TmFtZSI6IlNha3VqYSBTaGFtYWwgR2FqYW5heWFrZSIsIm5iZiI6MTc1NzE0NDY4NCwib3JnSWQiOm51bGwsIm9yZ05hbWUiOm51bGwsIm9yZ1JvbGUiOm51bGwsInBob25lTnVtYmVyVmVyaWZpZWQiOmZhbHNlLCJwcmltYXJ5RW1haWxBZGRyZXNzIjoiMjAyMXNwMDI2QHVuaXYuamZuLmFjLmxrIiwicHJpbWFyeVBob25lTnVtYmVyIjpudWxsLCJwcmltYXJ5V2ViM1dhbGxldCI6bnVsbCwic3ViIjoidXNlcl8zMkhpbWUyZlIyY0hMd0ZuU1ZqRGxHR1hrZGsiLCJ1c2VySWQiOiJ1c2VyXzMySGltZTJmUjJjSEx3Rm5TVmpEbEdHWGtkayIsInVzZXJuYW1lIjoic2hhbWFsIn0.bn8AgYGO-DhIr5Rcj9nFUUiM-DKbqPKeGMUdLdR0fd59JKnC-GnUfSshXKqi-can_UdViERdH0plszrnINQil_0n969bx9_wpcMaT_LYMYGXwXRqHdGrYGVGMNGbSyeaeDaD_T6koyGTvk4JXv5v02hRoFjG_jdK4Xzyb-gfQgK3zjYQlBxyVAdxKY8V1LTW4ktuRo_3ZduB4HFk_Acv8gha_G7GfJV853pGKeg1YyvFGXx8fQpgbGOTfYFMDsEphS5QT8s2LBXOmqkTAmaAlAFKbXaDDULxCvWqL7gANMe9bRcctXfxfN3RxjuYj7vFiCms8SO-eAu5mFxovPvYlQ";

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

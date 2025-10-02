// src/lib/actions.ts
"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {

    try {
        const token = JWTtoken;

        //testing token
        //const token = "eyJhbGciOiJSUzI1NiIsImNhdCI6ImNsX0I3ZDRQRDIyMkFBQSIsImtpZCI6Imluc18yemt5Q3ZzcllvdFV2RWRmTTdFTURKZm4zZjUiLCJ0eXAiOiJKV1QifQ.eyJhenAiOiJodHRwczovL3d3dy5kZXZhc2VlLmxrIiwiZW1haWwiOiIyMDIxc3AwMjZAdW5pdi5qZm4uYWMubGsiLCJlbWFpbFZlcmlmaWVkIjp0cnVlLCJleHAiOjE3NTg1ODc5MjMsImZpcnN0TmFtZSI6IjIwMjFzcDAyNiIsImlhdCI6MTc1ODU1MTkyMywiaW1hZ2VVcmwiOiJodHRwczovL2ltZy5jbGVyay5jb20vZXlKMGVYQmxJam9pWkdWbVlYVnNkQ0lzSW1scFpDSTZJbWx1YzE4eWVtdDVRM1p6Y2xsdmRGVjJSV1JtVFRkRlRVUktabTR6WmpVaUxDSnlhV1FpT2lKMWMyVnlYek15U0dsdFpUSm1VakpqU0V4M1JtNVRWbXBFYkVkSFdHdGtheUlzSW1sdWFYUnBZV3h6SWpvaU1sTWlmUSIsImlzcyI6Imh0dHBzOi8vY2xlcmsuZGV2YXNlZS5sayIsImp0aSI6IjgyNWUzNWNmYWZmYjVmZGQyNmYxIiwibGFzdE5hbWUiOiJTYWt1amEgU2hhbWFsIEdhamFuYXlha2UiLCJuYmYiOjE3NTg1NTE5MTgsIm9yZ0lkIjpudWxsLCJvcmdOYW1lIjpudWxsLCJvcmdSb2xlIjpudWxsLCJwaG9uZU51bWJlclZlcmlmaWVkIjpmYWxzZSwicHJpbWFyeUVtYWlsQWRkcmVzcyI6IjIwMjFzcDAyNkB1bml2Lmpmbi5hYy5sayIsInByaW1hcnlQaG9uZU51bWJlciI6bnVsbCwicHJpbWFyeVdlYjNXYWxsZXQiOm51bGwsInN1YiI6InVzZXJfMzJIaW1lMmZSMmNITHdGblNWakRsR0dYa2RrIiwidXNlcklkIjoidXNlcl8zMkhpbWUyZlIyY0hMd0ZuU1ZqRGxHR1hrZGsiLCJ1c2VybmFtZSI6InNoYW1hbCJ9.DLI9_UIxv_YpbS4_wOpdVlwbyMg4C5WrHpefpjjvwZrVF8lu6_bHCYIW7r2hKFcW_N6i3GURQyhjSSRU_7pKMAGJyfxD6r6NK3uyZ70nq0fVV_XFJAcddvWkgH9zwCMr04O_hM8zkZwBT6xtmkmyqYvWAMc77tBbiXJfwp7H9XIJogvKA2dIb0vD1cdDpdtCa_4vqTG6O_RXO3oQ7SNEh5ESmqwTToio2FIj1uVLBbOSGbqIqW1JTPHmxahpcpEv_3UTpDt6c-wcBDVEQ-77PR1ZQFAA0lk4arXxkuITeXSg65-phgtdCZZdS5fmxcwP0hrunyaOmy1edoVGoEtqFw"

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
            "http://api.devasee.lk/api/v1/product/books",
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

// app/actions/auth.ts
export async function authenticateWithAPI(token: string) {
    try {
        const response = await fetch('http://api.devasee.lk/api/v1/users/auth', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('API authentication failed');
        }

        return { success: true };
    } catch (error) {
        console.error('API authentication error:', error);
        return { success: false, error: 'Failed to authenticate with API' };
    }
}
"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import Image from "next/image";
import Logo from "@/assets/logo.png";
import Link from "next/link";


export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = (e: React.FormEvent) => {
        e.preventDefault();
        // TODO: Add your login logic (API call or auth provider)
        console.log("Login attempt:", { email, password });
    };

    return (
        <div className="min-h-screen flex items-center  justify-center bg-gradient-to-br from-gray-100 via-white to-gray-200 p-4">
            <Card className="w-full max-w-md shadow-2xl rounded-2xl border border-gray-200">
                <CardHeader >
                    <CardTitle className="text-center gap-4 flex-col flex justify-center items-center  text-2xl font-bold text-gray-800">
                        <Image src={Logo} width={70}  height={70} alt={"Logo"}/>
                        <div className="">
                            <h3 className={"text-gray-600"}>Devasee Admin</h3>
                            <p className={"text-xs font-light text-gray-500"}>
                                Welcome back! Please sign in to continue
                            </p>
                        </div>

                    </CardTitle>
                </CardHeader>

                <CardContent>
                    <form onSubmit={handleLogin} className="space-y-6">
                        {/* Email Field */}
                        <div>
                            <Label htmlFor="email" className="text-gray-700">
                                Email
                            </Label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="admin@devasee.lk"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                className="mt-2 h-12 border-gray-300 focus:border-blue-500 focus:outline-none focus:ring-0"
                            />
                        </div>

                        {/* Password Field */}
                        <div>
                            <Label htmlFor="password" className="text-gray-700">
                                Password
                            </Label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="••••••••"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                className="mt-2 h-12 border-gray-300 focus:border-blue-500 focus:outline-none focus:ring-0"
                            />


                        </div>

                        <Button type="submit" className="w-full h-12 bg-blue-600 hover:bg-blue-600 text-white">
                            Sign In
                        </Button>
                    </form>
                </CardContent>

                <CardFooter className="flex justify-center">
                    <div className="flex flex-col items-center justify-center w-full text-white py-4 rounded-b-2xl">
                        <p className="text-sm text-gray-600">
                            © {new Date().getFullYear()} Devasee Admin Panel
                        </p>
                        <Link href="https://www.devasee.lk" className={"text-xs text-gray-400"} target="_blank" rel="noopener noreferrer">
                            www.devasee.lk
                        </Link>
                    </div>
                </CardFooter>
            </Card>
        </div>
    );
}

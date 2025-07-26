import SubNavBar from "@/app/(router)/_components/SubNavBar";
import React from "react";

export default function Page() {
    return (
        <div>
            {/* Page-wide SubNavBar at the top */}
            <SubNavBar path="ABOUT" />
            {/* Page content in centered container */}
            <div style={{ maxWidth: 900, margin: "0 auto", padding: "2rem 1rem" }}>
                <div style={{ textAlign: "center", marginTop: "2rem" }}>
                    {/* Small blue title */}
                    <div style={{ color: "#1976d2", fontSize: 16, letterSpacing: 2, fontWeight: 500 }}>
                        WHO WE ARE
                    </div>
                    {/* Large black, bold subtitle */}
                    <div style={{ fontSize: 32, fontWeight: 600, color: "#222", margin: "1rem 0 0.5rem" }}>
                        Explore Our Story, Growth, and Commitment to Quality
                    </div>
                    {/* Centered blue line */}
                    <div
                        style={{
                            width: 60,
                            height: 6,
                            background: "#1976d2",
                            borderRadius: 6,
                            margin: "1rem auto"
                        }}
                    />
                    {/* Blue tagline */}
                    <div style={{ color: "#1976d2", fontSize: 18, marginBottom: "1.5rem" }}>
                        Your One-Stop Hub for Books, Stationery, and Custom Printing in Sri Lanka.
                    </div>
                    {/* Centered paragraph */}
                    <p style={{ maxWidth: 700, margin: "0 auto 2rem", color: "#444", fontSize: 17 }}>
                        At Devasee, we are passionate about providing a comprehensive range of books, stationery, and custom printing solutions to meet the diverse needs of our customers. Our journey is rooted in a commitment to quality, innovation, and exceptional service.
                    </p>
                    {/* Wide photo */}
                    <img
                        src="https://images.unsplash.com/photo-1515378791036-0648a3ef77b2?auto=format&fit=crop&w=900&q=80"
                        alt="About us"
                        style={{
                            width: "100%",
                            maxWidth: 900,
                            borderRadius: 12,
                            margin: "2rem 0"
                        }}
                    />
                    {/* Two columns under the photo */}
                    <div
                        style={{
                            display: "flex",
                            gap: "2rem",
                            justifyContent: "center",
                            alignItems: "flex-start",
                            flexWrap: "wrap"
                        }}
                    >
                        <div style={{ flex: 1, minWidth: 260 }}>
                            <p style={{ color: "#444", fontSize: 16, textAlign: "justify" }}>
                                Since our inception, we have grown from a small local store to a trusted name in the industry. Our team works tirelessly to curate the best products and deliver personalized solutions for every customer, whether you are a student, professional, or business.
                            </p>
                        </div>
                        <div style={{ flex: 1, minWidth: 260 }}>
                            <p style={{ color: "#444", fontSize: 16, textAlign: "justify" }}>
                                We believe in building lasting relationships and continuously evolving to serve you better. Thank you for making us your preferred destination for all your educational and creative needs in Sri Lanka.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}



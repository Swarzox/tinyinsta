import { useEffect } from "react";
import logo from "./img/insta_logo.webp";

function Login() {

    return (
        <div className="w-screen h-screen relative bg-gray-200">
            <div className="w-96 py-8 px-4 bg-gray-100 left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 relative">
                <div className="flex flex-col items-center justify-center">
                    <img src={logo} alt="Instagram Logo" className="w-16 mb-4" />
                    <h1>TinyInsta - Connexion</h1>
                </div>
            </div>
        </div>
    );
}
export default Login;
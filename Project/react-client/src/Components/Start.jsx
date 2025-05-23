import LoginPage from "./LoginPage.jsx";
import { Login } from "../utils/RestCalls.js";
import App from "./App.jsx";
import { useState } from "react";

function Start() {
    const [token, setToken] = useState("");

    async function loginPersoanaOficiu(username, password) {
        try {
            const newToken = await Login(username, password);
            console.log("Token primit:", newToken);
            setToken(newToken);
        } catch (err) {
            console.error("Eroare la autentificare:", err);
            alert("Autentificare eșuată. Verifică datele introduse.");
        }
    }

    if (token) {
        return <App token={token} />;
    }

    return (
        <>
            <h1>Autentificare Persoana Oficiu - Concurs Înot</h1>
            <LoginPage loginFunction={loginPersoanaOficiu} />
        </>
    );
}

export default Start;

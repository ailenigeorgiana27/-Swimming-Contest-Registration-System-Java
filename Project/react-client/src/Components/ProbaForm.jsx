import { useState } from "react";
import "../CSS/ProbaForm.css";

export default function ProbaForm({ addFunc }) {
    const [distanta, setDistanta] = useState("");
    const [stil, setStil] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        const proba = {
            distanta: parseInt(distanta),
            stil: stil.trim()
        };
        addFunc(proba);
        setDistanta("");
        setStil("");
    }

    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/* Distanta */}
            <div className="input">
                <label htmlFor="distanta" className="input-label">
                    Distanța (metri)
                </label>
                <input
                    type="number"
                    className="input-field"
                    id="distanta"
                    value={distanta}
                    onChange={(e) => setDistanta(e.target.value)}
                    required
                    placeholder="ex. 100"
                />
            </div>

            {/* Stil */}
            <div className="input">
                <label htmlFor="stil" className="input-label">
                    Stil
                </label>
                <input
                    type="text"
                    className="input-field"
                    id="stil"
                    value={stil}
                    onChange={(e) => setStil(e.target.value)}
                    required
                    placeholder="ex. liber, fluture, mixt"
                />
            </div>

            <button type="submit" className="add-button">
                Adaugă Proba
            </button>
        </form>
    );
}

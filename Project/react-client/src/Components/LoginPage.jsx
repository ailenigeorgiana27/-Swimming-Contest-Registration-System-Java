import { useState } from "react";
import '../CSS/LoginPage.css';

function LoginPage({ loginFunction }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    function handleSubmit(event) {
        event.preventDefault();
        if (!username.trim() || !password.trim()) {
            alert("Te rugăm să completezi toate câmpurile.");
            return;
        }
        loginFunction(username, password);
    }

    return (
        <div className="card">
            <h2 style={{ textAlign: 'center', marginBottom: '1rem' }}>Autentificare Persoană Oficiu</h2>
            <form className="card-form" onSubmit={handleSubmit}>
                {/* Username */}
                <div className="input">
                    <label htmlFor="username" className="input-label">
                        Username
                    </label>
                    <input
                        type="text"
                        className="input-field"
                        placeholder=" "
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        id="username"
                        required
                    />
                </div>

                {/* Password */}
                <div className="input">
                    <label htmlFor="password" className="input-label">
                        Parolă
                    </label>
                    <input
                        type="password"
                        className="input-field"
                        placeholder=" "
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        id="password"
                        required
                    />
                </div>

                <button type="submit" className="add-button">
                    Autentificare
                </button>
            </form>
        </div>
    );
}

export default LoginPage;

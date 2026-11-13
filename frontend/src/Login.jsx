
import { useState } from "react";

function Login({ onLogin }) {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleLogin(e) {
        e.preventDefault();
        setError("");
        setLoading(true);

        const token = btoa(`${userName}:${password}`);
        const headers = {
            Authorization: `Basic ${token}`,
        };

        try {
            // First check: Admin
            let response = await fetch(
                "http://localhost:8080/admin/dashboard",
                { headers }
            );

            if (response.ok) {
                sessionStorage.setItem("authToken", token);
                onLogin("ADMIN");
                return;
            }

            // Second check: Employee/User
            response = await fetch("http://localhost:8080/user", {
                headers,
            });

            if (response.ok) {
                sessionStorage.setItem("authToken", token);
                onLogin("USER");
                return;
            }

            setError("Invalid username or password");
        } catch (err) {
            setError("Backend server is not responding");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="login-container">
            <form className="login-form" onSubmit={handleLogin}>
                <h2>Employee Management</h2>
                <p>Login to your account</p>

                <input
                    type="text"
                    placeholder="Username"
                    value={userName}
                    onChange={(e) => setUserName(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                {error && <p style={{ color: "red" }}>{error}</p>}

                <button type="submit" disabled={loading}>
                    {loading ? "Logging in..." : "Login"}
                </button>
            </form>
        </div>
    );
}

export default Login;

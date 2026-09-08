import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Input from "../../components/common/Input";
import Button from "../../components/common/Button";

export default function Login() {

    const { login } = useAuth();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const submit = async (e) => {

        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            await login(email, password);

            navigate("/dashboard");

        } catch (err) {

            setError(
                err.response?.data?.message ||
                "Invalid email or password."
            );

        } finally {

            setLoading(false);
        }
    };

    return (
        <div className="auth-page">

            <div className="auth-glow"></div>

            <div className="auth-container">

                <div className="auth-brand">

                    <div className="brand-logo">
                        F
                    </div>

                    ForgeFlow

                </div>

                <div className="auth-card">

                    <div className="auth-header">

                        <h1>
                            Welcome back
                        </h1>

                        <p>
                            Sign in to your workspace
                        </p>

                    </div>

                    {error && (
                        <div className="auth-error">
                            {error}
                        </div>
                    )}

                    <form onSubmit={submit}>

                        <Input
                            label="Email"
                            type="email"
                            placeholder="you@example.com"
                            value={email}
                            onChange={(e) =>
                                setEmail(e.target.value)
                            }
                            required
                        />

                        <Input
                            label="Password"
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
                            required
                        />

                        <Button
                            type="submit"
                            loading={loading}
                            className="full-width"
                        >
                            Sign in →
                        </Button>

                    </form>

                    <div className="auth-switch">

                        Don't have an account?

                        <Link to="/register">
                            Create one
                        </Link>

                    </div>

                </div>

                <p className="auth-footer">
                    ForgeFlow · Modern project management
                </p>

            </div>
        </div>
    );
}
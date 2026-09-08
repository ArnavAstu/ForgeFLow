import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import Input from "../../components/common/Input";
import Button from "../../components/common/Button";

export default function Register() {

    const { register } = useAuth();
    const navigate = useNavigate();

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const submit = async (e) => {

        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            await register(
                name,
                email,
                password
            );

            navigate("/dashboard");

        } catch (err) {

            setError(
                err.response?.data?.message ||
                "Unable to create account."
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
                            Create your account
                        </h1>

                        <p>
                            Start building your workspace
                        </p>

                    </div>

                    {error && (
                        <div className="auth-error">
                            {error}
                        </div>
                    )}

                    <form onSubmit={submit}>

                        <Input
                            label="Full name"
                            placeholder="Your name"
                            value={name}
                            onChange={(e) =>
                                setName(e.target.value)
                            }
                            required
                        />

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
                            placeholder="Create a password"
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
                            Create account →
                        </Button>

                    </form>

                    <div className="auth-switch">

                        Already have an account?

                        <Link to="/login">
                            Sign in
                        </Link>

                    </div>

                </div>

            </div>
        </div>
    );
}
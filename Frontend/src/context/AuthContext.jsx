import { createContext, useContext, useState } from "react";
import {
    loginUser,
    registerUser
} from "../services/authService";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {

    const [user, setUser] = useState(() => {
        const saved = localStorage.getItem("forgeflow_user");
        return saved ? JSON.parse(saved) : null;
    });

    const saveAuth = (data) => {

        localStorage.setItem(
            "forgeflow_token",
            data.token
        );

        const userData = {
            userId: data.userId,
            name: data.name,
            email: data.email,
            role: data.role,
        };

        localStorage.setItem(
            "forgeflow_user",
            JSON.stringify(userData)
        );

        setUser(userData);
    };

    const login = async (email, password) => {

        const data = await loginUser({
            email,
            password
        });

        saveAuth(data);

        return data;
    };

    const register = async (
        name,
        email,
        password
    ) => {

        const data = await registerUser({
            name,
            email,
            password
        });

        saveAuth(data);

        return data;
    };

    const logout = () => {

        localStorage.removeItem("forgeflow_token");
        localStorage.removeItem("forgeflow_user");

        setUser(null);
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                login,
                register,
                logout,
                isAuthenticated: !!user
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () =>
    useContext(AuthContext);
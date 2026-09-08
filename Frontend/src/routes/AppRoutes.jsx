import {
    Routes,
    Route,
    Navigate
} from "react-router-dom";

import ProtectedRoute from "./ProtectedRoute";
import AppLayout from "../components/layout/AppLayout";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import Dashboard from "../pages/Dashboard";
import Projects from "../pages/Projects";
import ProjectDetails from "../pages/ProjectDetails";
import Tasks from "../pages/Tasks";
import Profile from "../pages/Profile";

export default function AppRoutes() {

    return (
        <Routes>

            <Route
                path="/login"
                element={<Login />}
            />

            <Route
                path="/register"
                element={<Register />}
            />

            <Route
                element={
                    <ProtectedRoute>
                        <AppLayout />
                    </ProtectedRoute>
                }
            >

                <Route
                    path="/dashboard"
                    element={<Dashboard />}
                />

                <Route
                    path="/projects"
                    element={<Projects />}
                />

                <Route
                    path="/projects/:projectId"
                    element={<ProjectDetails />}
                />

                <Route
                    path="/tasks"
                    element={<Tasks />}
                />

                <Route
                    path="/profile"
                    element={<Profile />}
                />

            </Route>

            <Route
                path="*"
                element={
                    <Navigate
                        to="/dashboard"
                        replace
                    />
                }
            />

        </Routes>
    );
}
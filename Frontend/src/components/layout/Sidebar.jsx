import { NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Sidebar() {

    const { user } = useAuth();

    return (
        <aside className="sidebar">

            <div className="sidebar-brand">

                <div className="brand-logo">
                    F
                </div>

                <span>ForgeFlow</span>

            </div>

            <div className="sidebar-section">

                <span className="sidebar-label">
                    WORKSPACE
                </span>

                <NavLink
                    to="/dashboard"
                    className="nav-item"
                >
                    <span>⌂</span>
                    Dashboard
                </NavLink>

                <NavLink
                    to="/projects"
                    className="nav-item"
                >
                    <span>▦</span>
                    Projects
                </NavLink>

                <NavLink
                    to="/tasks"
                    className="nav-item"
                >
                    <span>✓</span>
                    My Tasks
                </NavLink>

            </div>

            <div className="sidebar-section">

                <span className="sidebar-label">
                    MANAGEMENT
                </span>

                <NavLink
                    to="/profile"
                    className="nav-item"
                >
                    <span>◎</span>
                    Profile
                </NavLink>

            </div>

            <div className="sidebar-bottom">

                <div className="sidebar-user">

                    <div className="avatar">
                        {user?.name?.charAt(0)?.toUpperCase()}
                    </div>

                    <div>
                        <strong>
                            {user?.name}
                        </strong>

                        <small>
                            {user?.role}
                        </small>
                    </div>

                </div>

            </div>

        </aside>
    );
}
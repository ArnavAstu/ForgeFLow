import { useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Topbar() {

    const location = useLocation();
    const { user, logout } = useAuth();

    const titles = {
        "/dashboard": "Dashboard",
        "/projects": "Projects",
        "/tasks": "My Tasks",
        "/profile": "Profile"
    };

    const title =
        titles[location.pathname] || "ForgeFlow";

    return (
        <header className="topbar">

            <div>
                <h1>{title}</h1>
                <span>
                    Workspace overview
                </span>
            </div>

            <div className="topbar-actions">

                <button className="icon-button">
                    ◔
                </button>

                <div className="topbar-user">

                    <div className="avatar small">
                        {user?.name?.charAt(0)?.toUpperCase()}
                    </div>

                    <div>
                        <strong>{user?.name}</strong>
                        <small>{user?.email}</small>
                    </div>

                </div>

                <button
                    className="logout-button"
                    onClick={logout}
                >
                    Logout
                </button>

            </div>

        </header>
    );
}
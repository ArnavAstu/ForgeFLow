import { useAuth } from "../context/AuthContext";

export default function Profile() {

    const { user } = useAuth();

    return (
        <div>

            <div className="page-heading">

                <div>
                    <span className="eyebrow">
                        ACCOUNT
                    </span>

                    <h2>Your profile</h2>

                    <p>
                        Manage your ForgeFlow account.
                    </p>
                </div>

            </div>

            <div className="profile-card">

                <div className="profile-avatar">
                    {user?.name
                        ?.charAt(0)
                        ?.toUpperCase()}
                </div>

                <div className="profile-info">

                    <h2>{user?.name}</h2>

                    <p>{user?.email}</p>

                    <span className="role-badge">
                        {user?.role}
                    </span>

                </div>

            </div>

        </div>
    );
}
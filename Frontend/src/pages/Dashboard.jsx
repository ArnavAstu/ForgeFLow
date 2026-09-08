import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import StatCard from "../components/dashboard/StatCard";
import ActivityFeed from "../components/dashboard/ActivityFeed";
import Loader from "../components/common/Loader";
import { getProjectActivity } from "../services/activityService";

export default function Dashboard() {

    const { user } = useAuth();

    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {

        /*
         * Activity requires a projectId.
         * We'll populate this after the project
         * dashboard is connected.
         */

    }, []);

    return (
        <div>

            <div className="welcome-section">

                <div>

                    <span className="eyebrow">
                        WORKSPACE
                    </span>

                    <h2>
                        Good morning, {user?.name?.split(" ")[0]}.
                    </h2>

                    <p>
                        Here's what's happening with your projects.
                    </p>

                </div>

                <div className="dashboard-date">
                    {new Date().toLocaleDateString(
                        undefined,
                        {
                            weekday: "long",
                            month: "long",
                            day: "numeric"
                        }
                    )}
                </div>

            </div>

            <div className="stats-grid">

                <StatCard
                    icon="▦"
                    title="Total projects"
                    value="—"
                    subtitle="Your workspace"
                />

                <StatCard
                    icon="✓"
                    title="Tasks"
                    value="—"
                    subtitle="Across projects"
                />

                <StatCard
                    icon="◷"
                    title="In progress"
                    value="—"
                    subtitle="Currently active"
                />

                <StatCard
                    icon="◆"
                    title="Completed"
                    value="—"
                    subtitle="Keep shipping"
                />

            </div>

            <div className="dashboard-grid">

                <ActivityFeed
                    activities={activities}
                />

                <div className="panel quick-panel">

                    <div className="panel-header">
                        <div>
                            <h2>Quick actions</h2>
                            <p>
                                Jump straight into your work
                            </p>
                        </div>
                    </div>

                    <div className="quick-actions">

                        <a href="/projects">
                            <span>＋</span>
                            New project
                        </a>

                        <a href="/tasks">
                            <span>✓</span>
                            View tasks
                        </a>

                    </div>

                </div>

            </div>

        </div>
    );
}
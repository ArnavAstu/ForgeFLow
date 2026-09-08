import { useEffect, useState } from "react";
import {
    useParams,
    Link
} from "react-router-dom";

import Loader from "../components/common/Loader";
import TaskCard from "../components/tasks/TaskCard";
import StatCard from "../components/dashboard/StatCard";

import { getProject } from "../services/projectService";
import { getProjectTasks } from "../services/taskService";
import { getDashboardStats } from "../services/dashboardService";

export default function ProjectDetails() {

    const { projectId } = useParams();

    const [project, setProject] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [stats, setStats] = useState(null);

    const [loading, setLoading] = useState(true);

    useEffect(() => {

        const load = async () => {

            try {

                const [
                    projectData,
                    taskData,
                    statsData
                ] = await Promise.all([
                    getProject(projectId),
                    getProjectTasks(projectId),
                    getDashboardStats(projectId)
                ]);

                setProject(projectData);

                setTasks(
                    taskData.content || []
                );

                setStats(statsData);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);
            }
        };

        load();

    }, [projectId]);

    if (loading) return <Loader />;

    return (
        <div>

            <Link
                to="/projects"
                className="back-link"
            >
                ← Projects
            </Link>

            <div className="project-heading">

                <div>

                    <div className="large-project-icon">
                        {project?.name
                            ?.charAt(0)
                            ?.toUpperCase()}
                    </div>

                    <div>
                        <h2>{project?.name}</h2>
                        <p>
                            {project?.description ||
                                "No description"}
                        </p>
                    </div>

                </div>

            </div>

            {stats && (
                <div className="stats-grid">

                    <StatCard
                        title="Total tasks"
                        value={stats.total}
                        icon="▦"
                    />

                    <StatCard
                        title="To do"
                        value={stats.todo}
                        icon="○"
                    />

                    <StatCard
                        title="In progress"
                        value={stats.inProgress}
                        icon="◷"
                    />

                    <StatCard
                        title="Completed"
                        value={stats.completed}
                        icon="✓"
                    />

                </div>
            )}

            <div className="panel">

                <div className="panel-header">

                    <div>
                        <h2>Tasks</h2>
                        <p>
                            Work items in this project
                        </p>
                    </div>

                </div>

                <div className="task-list">

                    {tasks.map(task => (
                        <TaskCard
                            key={task.id}
                            task={task}
                        />
                    ))}

                </div>

            </div>

        </div>
    );
}
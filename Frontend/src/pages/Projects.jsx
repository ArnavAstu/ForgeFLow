import { useEffect, useState } from "react";
import ProjectCard from "../components/projects/ProjectCard";
import Loader from "../components/common/Loader";
import EmptyState from "../components/common/EmptyState";
import { getProjects } from "../services/projectService";

export default function Projects() {

    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        const load = async () => {

            try {

                const data = await getProjects();

                setProjects(
                    Array.isArray(data)
                        ? data
                        : data.content || []
                );

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);
            }
        };

        load();

    }, []);

    if (loading) return <Loader />;

    return (
        <div>

            <div className="page-heading">

                <div>

                    <span className="eyebrow">
                        WORKSPACE
                    </span>

                    <h2>Your projects</h2>

                    <p>
                        Manage everything you're building.
                    </p>

                </div>

                <button className="btn btn-primary">
                    + New project
                </button>

            </div>

            {projects.length === 0 ? (

                <EmptyState
                    icon="▦"
                    title="No projects yet"
                    description="Create your first project and start building."
                />

            ) : (

                <div className="projects-grid">

                    {projects.map(project => (

                        <ProjectCard
                            key={project.id}
                            project={project}
                        />

                    ))}

                </div>

            )}

        </div>
    );
}
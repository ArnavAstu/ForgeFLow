import { Link } from "react-router-dom";

export default function ProjectCard({
                                        project
                                    }) {

    return (
        <Link
            to={`/projects/${project.id}`}
            className="project-card"
        >

            <div className="project-card-top">

                <div className="project-icon">
                    {project.name
                        ?.charAt(0)
                        ?.toUpperCase()}
                </div>

                <span className="project-arrow">
                    →
                </span>

            </div>

            <h3>
                {project.name}
            </h3>

            <p>
                {project.description ||
                    "No description provided."}
            </p>

            <div className="project-card-footer">

                <span>
                    Open project
                </span>

                <span>↗</span>

            </div>

        </Link>
    );
}
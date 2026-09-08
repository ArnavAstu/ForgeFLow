import { Link } from "react-router-dom";

export default function TaskCard({
                                     task
                                 }) {

    return (
        <div className="task-card">

            <div className="task-main">

                <div
                    className={`status-indicator status-${task.status?.toLowerCase()}`}
                ></div>

                <div>

                    <h3>{task.title}</h3>

                    <p>
                        {task.description ||
                            "No description"}
                    </p>

                </div>

            </div>

            <div className="task-meta">

                <span className={`priority ${task.priority?.toLowerCase()}`}>
                    {task.priority}
                </span>

                <span>
                    {task.status}
                </span>

                {task.dueDate && (
                    <span>
                        Due{" "}
                        {new Date(
                            task.dueDate
                        ).toLocaleDateString()}
                    </span>
                )}

            </div>

        </div>
    );
}
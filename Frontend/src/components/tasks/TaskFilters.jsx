export default function TaskFilters({
                                        status,
                                        priority,
                                        setStatus,
                                        setPriority
                                    }) {

    return (
        <div className="task-filters">

            <select
                value={status}
                onChange={(e) =>
                    setStatus(e.target.value)
                }
            >
                <option value="">All statuses</option>
                <option value="TODO">To Do</option>
                <option value="IN_PROGRESS">
                    In Progress
                </option>
                <option value="COMPLETED">
                    Completed
                </option>
            </select>

            <select
                value={priority}
                onChange={(e) =>
                    setPriority(e.target.value)
                }
            >
                <option value="">All priorities</option>
                <option value="HIGH">High</option>
                <option value="MEDIUM">Medium</option>
                <option value="LOW">Low</option>
            </select>

        </div>
    );
}
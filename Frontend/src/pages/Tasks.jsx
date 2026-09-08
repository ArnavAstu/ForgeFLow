import { useState } from "react";
import TaskFilters from "../components/tasks/TaskFilters";
import EmptyState from "../components/common/EmptyState";

export default function Tasks() {

    const [status, setStatus] = useState("");
    const [priority, setPriority] = useState("");

    return (
        <div>

            <div className="page-heading">

                <div>

                    <span className="eyebrow">
                        PRODUCTIVITY
                    </span>

                    <h2>My tasks</h2>

                    <p>
                        Keep track of the work assigned to you.
                    </p>

                </div>

            </div>

            <TaskFilters
                status={status}
                priority={priority}
                setStatus={setStatus}
                setPriority={setPriority}
            />

            <EmptyState
                icon="✓"
                title="Your task inbox is clear"
                description="Assigned tasks will appear here."
            />

        </div>
    );
}
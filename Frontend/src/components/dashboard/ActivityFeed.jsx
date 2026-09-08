export default function ActivityFeed({
                                         activities = []
                                     }) {

    return (
        <div className="panel">

            <div className="panel-header">

                <div>
                    <h2>Recent activity</h2>
                    <p>What's happening in your workspace</p>
                </div>

            </div>

            {activities.length === 0 ? (

                <div className="activity-empty">
                    No recent activity
                </div>

            ) : (

                <div className="activity-list">

                    {activities.map((activity) => (

                        <div
                            className="activity-item"
                            key={activity.id}
                        >

                            <div className="activity-avatar">
                                {activity.userName
                                    ?.charAt(0)
                                    ?.toUpperCase()}
                            </div>

                            <div className="activity-content">

                                <strong>
                                    {activity.userName}
                                </strong>

                                <p>
                                    {activity.message}
                                </p>

                                <small>
                                    {new Date(
                                        activity.createdAt
                                    ).toLocaleString()}
                                </small>

                            </div>

                        </div>

                    ))}

                </div>

            )}

        </div>
    );
}
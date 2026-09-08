export default function StatCard({
                                     title,
                                     value,
                                     subtitle,
                                     icon
                                 }) {

    return (
        <div className="stat-card">

            <div className="stat-top">

                <span className="stat-icon">
                    {icon}
                </span>

                <span className="stat-dot"></span>

            </div>

            <div className="stat-value">
                {value}
            </div>

            <div className="stat-title">
                {title}
            </div>

            {subtitle && (
                <div className="stat-subtitle">
                    {subtitle}
                </div>
            )}

        </div>
    );
}
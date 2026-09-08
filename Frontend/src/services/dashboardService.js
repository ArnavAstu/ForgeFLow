import api from "./api";

export const getDashboardStats = async (projectId) => {
    const response = await api.get(
        `/projects/${projectId}/dashboard`
    );

    return response.data;
};
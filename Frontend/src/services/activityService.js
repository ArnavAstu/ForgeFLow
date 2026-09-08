import api from "./api";

export const getProjectActivity = async (
    projectId,
    limit = 20
) => {

    const response = await api.get(
        `/projects/${projectId}/activities`,
        {
            params: { limit }
        }
    );

    return response.data;
};
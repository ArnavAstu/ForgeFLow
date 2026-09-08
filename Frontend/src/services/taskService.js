import api from "./api";

export const getProjectTasks = async (
    projectId,
    params = {}
) => {

    const response = await api.get(
        `/projects/${projectId}/tasks`,
        {
            params
        }
    );

    return response.data;
};

export const createTask = async (
    projectId,
    data
) => {

    const response = await api.post(
        `/projects/${projectId}/tasks`,
        data
    );

    return response.data;
};
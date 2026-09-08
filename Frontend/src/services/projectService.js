import api from "./api";

export const getProjects = async () => {

    const response = await api.get("/projects");

    return response.data;
};

export const getProject = async (projectId) => {

    const response = await api.get(
        `/projects/${projectId}`
    );

    return response.data;
};
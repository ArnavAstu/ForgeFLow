import api from "./api";

export const getTaskComments = async (taskId) => {

    const response = await api.get(
        `/tasks/${taskId}/comments`
    );

    return response.data;
};

export const createComment = async (
    taskId,
    content
) => {

    const response = await api.post(
        `/tasks/${taskId}/comments`,
        { content }
    );

    return response.data;
};

export const deleteComment = async (
    taskId,
    commentId
) => {

    await api.delete(
        `/tasks/${taskId}/comments/${commentId}`
    );
};
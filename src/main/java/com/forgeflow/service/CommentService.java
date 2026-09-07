package com.forgeflow.service;

import com.forgeflow.dto.CommentResponse;
import com.forgeflow.dto.CreateCommentRequest;
import com.forgeflow.entity.Comment;
import com.forgeflow.entity.Project;
import com.forgeflow.entity.Task;
import com.forgeflow.entity.User;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.CommentRepository;
import com.forgeflow.repository.ProjectMemberRepository;
import com.forgeflow.repository.TaskRepository;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ProjectMemberRepository projectMemberRepository;


    // =========================================================
    // CREATE COMMENT
    // =========================================================

    public CommentResponse createComment(
            Long taskId,
            CreateCommentRequest request,
            String userEmail
    ) {

        Task task =
                getTask(taskId);

        User user =
                getUserByEmail(userEmail);

        checkProjectAccess(
                task.getProject(),
                user
        );

        Comment comment =
                new Comment();

        comment.setContent(
                request.getContent()
        );

        comment.setTask(task);

        comment.setUser(user);

        Comment saved =
                commentRepository.save(comment);

        return new CommentResponse(saved);
    }


    // =========================================================
    // GET TASK COMMENTS
    // =========================================================

    public List<CommentResponse> getTaskComments(
            Long taskId,
            String userEmail
    ) {

        Task task =
                getTask(taskId);

        User user =
                getUserByEmail(userEmail);

        checkProjectAccess(
                task.getProject(),
                user
        );

        Sort sort =
                Sort.by(
                        Sort.Direction.ASC,
                        "createdAt"
                );

        return commentRepository
                .findByTaskId(taskId, sort)
                .stream()
                .map(CommentResponse::new)
                .toList();
    }


    // =========================================================
    // DELETE COMMENT
    // comment author OR project owner
    // =========================================================

    public void deleteComment(
            Long commentId,
            String userEmail
    ) {

        Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comment not found with id: "
                                                + commentId
                                )
                        );

        User currentUser =
                getUserByEmail(userEmail);

        boolean isAuthor =
                comment
                        .getUser()
                        .getId()
                        .equals(
                                currentUser.getId()
                        );

        boolean isProjectOwner =
                comment
                        .getTask()
                        .getProject()
                        .getOwner()
                        .getId()
                        .equals(
                                currentUser.getId()
                        );

        if (!isAuthor && !isProjectOwner) {

            throw new AccessDeniedException(
                    "You cannot delete this comment"
            );
        }

        commentRepository.delete(comment);
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private Task getTask(Long taskId) {

        return taskRepository
                .findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: "
                                        + taskId
                        )
                );
    }


    private User getUserByEmail(
            String email
    ) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }


    private void checkProjectAccess(
            Project project,
            User user
    ) {

        boolean owner =
                project
                        .getOwner()
                        .getId()
                        .equals(
                                user.getId()
                        );

        boolean member =
                projectMemberRepository
                        .existsByProjectIdAndUserId(
                                project.getId(),
                                user.getId()
                        );

        if (!owner && !member) {

            throw new AccessDeniedException(
                    "You do not have access to this project"
            );
        }
    }
}
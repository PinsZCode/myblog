package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentserviceImpl implements CommentService {
    private PostRepository postRepository;

    private CommentRepository commentRepository;

    public CommentserviceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long postId) {
         Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        );
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setEmail(commentDto.getEmail());
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        CommentDto dto = new CommentDto();
        dto.setId(savedComment.getId());
        dto.setEmail(savedComment.getEmail());
      //  System.out.println(savedComment.getEmail());
        dto.setText(savedComment.getText());
        //System.out.println(savedComment.getText());
        return dto;
    }
}

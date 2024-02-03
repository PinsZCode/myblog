package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentserviceImpl implements CommentService {
    private PostRepository postRepository;

    private CommentRepository commentRepository;
    private ModelMapper modelMapper;

    public CommentserviceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper= modelMapper;
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

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto updateComment(long id, CommentDto commentDto, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found for id : " + postId)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found for id" + id)

        );
        Comment c = mapToEntity(commentDto);
        c.setId(comment.getId());
        c.setPost(post);
        Comment saveComment = commentRepository.save(c);
        return mapToDto(saveComment);


    }
    CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        return dto;
    }
    Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }
}




















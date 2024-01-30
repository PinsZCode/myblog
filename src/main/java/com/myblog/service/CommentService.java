package com.myblog.service;

import com.myblog.payload.CommentDto;
import org.springframework.stereotype.Service;


public interface CommentService {
   CommentDto createComment(CommentDto commentDto, long postId);
}

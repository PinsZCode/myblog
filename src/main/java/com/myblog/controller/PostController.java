package com.myblog.controller;

import com.myblog.payload.PostDto;
import com.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto>createPost(@RequestBody PostDto postDto){
        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

//http://localhost:8080/api/posts/particular/?id=4
    @GetMapping("/particular")
    public ResponseEntity<PostDto> getPostById(@RequestParam long id){
        PostDto dto = postService.getPostById(id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/posts?pageNo=0&pageSize=3
    //http://localhost:8080/api/posts?pageNo=0&pageSize=3&sortBy="title"
    @GetMapping
    public List<PostDto> getAllPost(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false,defaultValue = "3")int pageSize,
            @RequestParam(name ="sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name="sortDir", required = false,defaultValue = "id") String sortDir
    ){
        List <PostDto> postDtos = postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
        return  postDtos;
    }

}
 
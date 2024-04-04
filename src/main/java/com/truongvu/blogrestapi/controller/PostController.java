package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for POST resource"
)
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "Create POST REST API",
            description = "Create POST REST API is used to save post into database"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get all POSTS REST API",
            description = "Get all POSTS REST API used to fetch all posts from DB"
    )
    @GetMapping
    public List<PostDTO> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        return postService.getAllPosts(pageNo, pageSize, sortBy);
    }

    @GetMapping("/all-post")
    public List<PostDTO> getAllPostsWithoutPageSize() {
        return postService.getAllPostsWithoutPageSize();
    }


    @Operation(
            summary = "Get POST by ID REST API",
            description = "Get POST By ID REST API is used to get single post from DB"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id) {

        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Update POST REST API",
            description = "Update POST REST API is used to update a particular post in DB"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(postService.updatePost(postDTO, id), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete POST REST API",
            description = "Delete POST REST API is used to delete a particular post from DB"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post with id= " + id + " deleted successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Get POSTS by CATEGORY REST API",
            description = "Get POSTS by CATEGORY REST API is used to get a list of posts by common CATEGORY"
    )
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getAllPostsByCategory(@PathVariable("id") long id) {
        return new ResponseEntity<>(postService.getPostsByCategory(id), HttpStatus.OK);
    }

    @GetMapping("/category/{id}/filter")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(
            @PathVariable("id") long id,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        return new ResponseEntity<>(postService.getPostsByCategoryWithPageSize(id, pageNo, pageSize, sortBy), HttpStatus.OK);
    }

}

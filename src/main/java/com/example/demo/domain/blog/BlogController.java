package com.example.demo.domain.blog;

import com.example.demo.core.context.LocalContext;
import com.example.demo.domain.blog.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/blogs")
@Log4j2
public class BlogController {
    private final BlogService blogService;
    private final LocalContext localContext;
    private final BlogMapper blogMapper;

    @Autowired
    public BlogController(BlogService blogService, LocalContext localContext, BlogMapper blogMapper) {
        this.blogService = blogService;
        this.blogMapper = blogMapper;
        this.localContext = localContext;
    }


    @Operation(summary = "get all the blogs")
    @GetMapping("")
    public Optional<List<Blog>> getAllBlogs(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy) {
        if (page == null && size == null && sortBy == null) {
            return Optional.ofNullable(blogService.getAllBlogs());
        } else if (page != null && size != null && sortBy == null) {
            return Optional.ofNullable(blogService.getAllBlogsPageable(page, size));
        }
        else {
            return Optional.ofNullable(blogService.getAllSortedBlogsPageable(page, size, sortBy));
        }
    }

    @Operation(summary = "returns number of pages required in frontend")
    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfBlogs() {
        return new ResponseEntity<>(blogService.getNumberOfBlogs(), HttpStatus.OK);
    }
    @Operation(summary = "find a blog by its ID.")
    @GetMapping("/{blogId}")
    public Optional<Blog> findById(@PathVariable("blogId") String blogId) {
        return Optional.ofNullable(blogService.findById(UUID.fromString(blogId)));
    }
    @Operation(summary = "add a blog")
    @PostMapping("")
    @PreAuthorize("hasAuthority('BLOG_READ_WRITE')")
    public void addBlog(@Valid @RequestBody PostBlogDTO postBlogDTO) {
        blogService.addBlog(postBlogDTO);
    }

    @Operation(summary = "delete a blog by ID.")
    @DeleteMapping(path = "/{blogId}")
    @PreAuthorize("hasAuthority('BLOG_READ_WRITE')")
    public ResponseEntity<Void> deleteBlog(@PathVariable("blogId") UUID blogId) {
        Blog temp = blogService.findById(blogId);
        if ((temp != null && localContext.getCurrentUser().getId() == temp.getAuthor().getId()) || localContext.isUserAdministrator()) {
            blogService.deleteBlog(blogId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "update a blog by ID.")
    @PutMapping(path = "/{blogId}")
    @PreAuthorize("hasAuthority('BLOG_READ_WRITE')")
    public ResponseEntity<Void> updateBlog(@PathVariable("blogId") UUID blogId, @Valid @RequestBody UpdateBlogDTO blog) {
        Blog temp = blogService.findById(blogId);
        if ((temp != null && localContext.getCurrentUser().getId() == temp.getAuthor().getId()) || localContext.isUserAdministrator()) {
            blogService.updateBlog(blogId, blog.getTitle(), blog.getText(), localContext.getCurrentUser(), blog.getCategory());
            return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(400).body("Something went wrong: " + ex.getMessage());
    }

}

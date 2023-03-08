package com.example.demo.domain.blog;

import com.example.demo.domain.blog.dto.PostBlogDTO;
import com.example.demo.domain.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    public List<Blog> getAllSortedBlogsPageable(Integer pageNo, Integer pageSize, String sortBy) {
        return blogRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc(sortBy)))).getContent();
    }

    public List<Blog> getAllBlogsPageable(Integer page, Integer size) {
        return blogRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Blog findById(UUID Id) {
        return blogRepository.findById(Id).orElse(null);
    }

    public Blog addBlog(PostBlogDTO blog) {
            log.info("Blog " + blog.getTitle() + " got added");
            return blogRepository.save(new Blog(blog.getTitle(), blog.getText(), blog.getAuthor(), blog.getCategory()));
    }

    public void deleteBlog(UUID blogId) {
        boolean exists = blogRepository.existsById(blogId);
        if (!exists) {
            log.error("Blog doesn't exist");
            throw new IllegalStateException("Blog doesn't exist");
        } else {
            blogRepository.deleteById(blogId);
        }
    }

    public Blog updateBlog(UUID BlogId, String title, String text, User author, String category) {
        boolean exists = blogRepository.existsById(BlogId);
        if (!exists) {
            log.error("Blog " + title + " doesn't exist");
            throw new IllegalStateException("Blog doesn't exist");
        } else {
            Blog blog = blogRepository.getById(BlogId);
            blog.setTitle(title);
            blog.setText(text);
            blog.setAuthor(author);
            blog.setCategory(category);
            log.info("Blog got updated");
            return blogRepository.save(blog);
        }
    }

    public Long getNumberOfBlogs() {
        long blogCount = blogRepository.count();
        int initialPageCount = (int) (blogCount / 5);

        if (blogCount % 5 != 0) {
            initialPageCount++;
        }
        return (long) initialPageCount;
    }

}

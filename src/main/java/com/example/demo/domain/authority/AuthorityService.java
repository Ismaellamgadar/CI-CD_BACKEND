package com.example.demo.domain.authority;

import com.example.demo.core.generic.AbstractRepository;
import com.example.demo.core.generic.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService extends AbstractServiceImpl<Authority> {
    @Autowired
    protected AuthorityService(AuthorityRepository repository) {
        super(repository);
    }

    public List<Authority> getAllAuthorities() {
        return repository.findAll();
    }
}

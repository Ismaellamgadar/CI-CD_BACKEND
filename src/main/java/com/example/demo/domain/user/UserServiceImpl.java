package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.authority.AuthorityService;
import com.example.demo.domain.role.Role;
import com.example.demo.domain.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final AuthorityService authorityService;
  private final RoleRepository roleRepository;

  @Autowired
  public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, AuthorityService authorityService, RoleRepository roleRepository) {
    super(repository);
    this.roleRepository = roleRepository;
    this.authorityService = authorityService;
    this.passwordEncoder = passwordEncoder;
  }

  @Cacheable("local-cache")
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return ((UserRepository) repository).findByEmail(email)
                                        .map(entry -> new UserDetailsImpl(entry, authorityService))
                                        .orElseThrow(() -> new UsernameNotFoundException(email));
  }

  @Override
  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Set<Role> defaultRoles = new HashSet<>();
    defaultRoles.add(roleRepository.getReferenceById(UUID.fromString("aed650d9-cae0-4b48-877c-932a7f347786")));
    user.setRoles(defaultRoles);
    return save(user);
  }
  @Override
  public User registerUser(User user){
    user.setPassword(getRandomSpecialChars(20).toString());
    return save(user);
  }

  public Stream<Character> getRandomSpecialChars(int count) {
    Random random = new SecureRandom();
    IntStream specialChars = random.ints(count, 33, 45);
    return specialChars.mapToObj(data -> (char) data);
  }

}

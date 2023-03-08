package com.example.demo.domain.user;

import com.example.demo.core.context.LocalContext;
import com.example.demo.core.logger.AutomaticLogger;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import com.example.demo.domain.user.dto.UserRegisterDTO;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import com.example.demo.domain.user.dto.UserRegisterMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final UserRegisterMapper userRegisterMapper;

  private final LocalContext localContext;

  @Autowired
  public UserController(UserService userService, UserMapper userMapper, LocalContext localContext, UserRegisterMapper userRegisterMapper) {
    this.userService = userService;
    this.userRegisterMapper = userRegisterMapper;
    this.localContext = localContext;
    this.userMapper = userMapper;
  }
  @Operation(summary = "get a user by id")
  @AutomaticLogger
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> retrieveById(@PathVariable UUID id) {
    User user = userService.findById(id);
    if (localContext.getCurrentUser().getId() == id || localContext.isUserAdministrator()) {
      return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
  @Operation(summary = "gets all the users")
  @AutomaticLogger
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping({ "/"})
  public ResponseEntity<List<UserDTO>> retrieveAll() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(userMapper.toDTOs(users), HttpStatus.OK);
  }
  @Operation(summary = "user registers himself through this api. Only requires email, password, firstname and lastname")
  @AutomaticLogger
  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
    return new ResponseEntity<>(userMapper.toDTO(userService.register(userRegisterMapper.fromDTO(userRegisterDTO))), HttpStatus.CREATED);
  }
  @Operation(summary = "admin can register a user through this api so you can also give roles.")
  @AutomaticLogger
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/registerUser")
  public ResponseEntity<UserDTO> registerWithoutPassword(@Valid @RequestBody UserDTO userDTO) {
    User user = userService.registerUser(userMapper.fromDTO(userDTO));
    return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.CREATED);
  }
  @Operation(summary = "update a user by ID")
  @AutomaticLogger
  @PutMapping("/{id}")
  @PreAuthorize(
      "hasAuthority('USER_MODIFY') && @userPermissionEvaluator.isUserAboveAge(authentication.principal.user,18)")
  public ResponseEntity<UserDTO> updateById(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
    if (localContext.getCurrentUser().getId() == id || localContext.isUserAdministrator()) {
      User user = userService.updateById(id, userMapper.fromDTO(userDTO));
      return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
  @Operation(summary = "delete a user by ID")
  @AutomaticLogger
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER_DELETE')")
  public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
    if (localContext.getCurrentUser().getId() == id || localContext.isUserAdministrator()) {
      userService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}

package com.example.demo.domain.page;

import com.example.demo.core.logger.AutomaticLogger;
import com.example.demo.domain.group.Group;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.group.dto.GroupMapper;
import com.example.demo.domain.page.dto.CustomPageDTO;
import com.example.demo.domain.page.dto.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 Controller for CustomPageService API.
 */
@RestController
@RequestMapping("/custom-page")
public class CustomPageController {
    private final CustomPageService customPageService;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;

    @Autowired
    public CustomPageController(GroupMapper groupMapper, CustomPageService customPageService, PageMapper pageMapper) {
        this.groupMapper = groupMapper;
        this.pageMapper = pageMapper;
        this.customPageService = customPageService;
    }


    /**

     Endpoint to create a new custom page.
     @param title the title of the custom page.
     @param body the body of the custom page.
     @param ownerGroup the owner group of the custom page.
     @param rules the list of rules for the custom page.
     @return the created custom page.
     */
    @Operation(summary = "to create a custom page")
    @AutomaticLogger
    @PostMapping("/create")
    public ResponseEntity<CustomPageDTO> createCustomPage(@RequestBody String title, @RequestBody String body,
                                       @Valid @RequestBody GroupDTO ownerGroup, @RequestBody List<String> rules) {
        return new ResponseEntity<>(pageMapper.toDTO(customPageService.createPage(title, body, groupMapper.fromDTO(ownerGroup), rules)), HttpStatus.CREATED);
    }
    /**

     Endpoint to delete a custom page by id.
     @param id the id of the custom page to delete.
     @throws NoSuchElementException if the custom page with the specified id does not exist.
     */
    @Operation(summary = "delete a custom page by ID")
    @AutomaticLogger
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomPageById(@PathVariable UUID id) throws NoSuchElementException {
        customPageService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    /**

     Endpoint to get a custom page by id.
     @param id the id of the custom page to get.
     @return the custom page with the specified id.
     */
    @Operation(summary = "get a custom page by ID")
    @AutomaticLogger
    @GetMapping("/{id}")
    public ResponseEntity<CustomPageDTO> getCustomPageById(@PathVariable UUID id) {
        return new ResponseEntity<>(pageMapper.toDTO(customPageService.findById(id)), HttpStatus.OK);
    }
    /**

     Endpoint to get all custom pages by group.
     @param ownerGroup the group to filter by.
     @return a list of custom pages belonging to the specified group.
     */
    @Operation(summary = "get all custom pages by group")
    @AutomaticLogger
    @GetMapping("/group/{ownerGroup}")
    public ResponseEntity<List<CustomPageDTO>> getAllCustomPagesByGroup(@PathVariable GroupDTO ownerGroup) {
        return new ResponseEntity<>(customPageService.findAllByGroup(groupMapper.fromDTO(ownerGroup)).parallelStream().map(pageMapper::toDTO).toList(), HttpStatus.OK);
    }
}
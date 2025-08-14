package com.in28minutes.rest.webservices.restful_web_services.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResource {

    private UserDaoService service;

    public UserResource(UserDaoService service) {
        this.service = service;
    }
    // GET/users
    @GetMapping("/users")
    public List<User> retreiveAllUsers(){
        return service.findAll();
    }



    // link -> Entity model ,
    // GET/user
    @GetMapping("/users/{id}")
    public EntityModel<User> retreiveUser(@PathVariable int id){
        User user = service.findUser(id);

        if(user == null){
            throw new UserNotFoundException("id: "+ id);
        }

        EntityModel <User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retreiveAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    // DELETE/user
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
       service.deleteById(id);
    }

    // POST/users
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);  // executes and saves user details so it can be processed in next step

        // to get the uri of the user being appended into the json -> can view in location (POSTMAN)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }
}

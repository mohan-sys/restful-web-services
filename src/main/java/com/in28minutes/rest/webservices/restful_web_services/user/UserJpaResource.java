package com.in28minutes.rest.webservices.restful_web_services.user;

import com.in28minutes.rest.webservices.restful_web_services.jpa.PostRepository;
import com.in28minutes.rest.webservices.restful_web_services.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;

    }
    // GET/users
    @GetMapping("/jpa/users")
    public List<User> retreiveAllUsers(){
        return userRepository.findAll();
    }




    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retreiveUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("id: "+ id);
        }

        EntityModel <User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retreiveAllUsers());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }



    // DELETE/user
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
       userRepository.deleteById(id);
    }



    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("id: "+ id);
        }

        return user.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new UserNotFoundException("id: "+ id);
        }

        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        // to get the uri of the user being appended into the json -> can view in location (POSTMAN)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();


    }

    // POST/users
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);  // executes and saves user details so it can be processed in next step

        // to get the uri of the user being appended into the json -> can view in location (POSTMAN)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }
}

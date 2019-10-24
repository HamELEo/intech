package com.intech.controller;

import com.intech.Content;
import com.intech.User;
import com.intech.repository.ContentRepository;
import com.intech.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ServerController {

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @PostMapping(value = "save_user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        User save = userRepository.save(user);
        Long id = save.getId();
        return new ResponseEntity<>("User saved with id: " + id, HttpStatus.OK);
    }

    @GetMapping(value = "find_user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@RequestBody User login) {
        Optional<User> byLogin = userRepository.findByLogin(login.getLogin());
        if (byLogin.isPresent()) {
            return new ResponseEntity<>(byLogin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "get_user_content", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserContent(@RequestBody User login) {
        Optional<User> byLogin = userRepository.findByLogin(login.getLogin());
        if (!byLogin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = byLogin.get();
        return new ResponseEntity<>(user.getContents(), HttpStatus.OK);
    }

    @GetMapping(value = "get_all_content", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllContent() {
        List<Content> all = contentRepository.findAllByOrderByIdAsc();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping(value = "set_user_content", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setUserContent(@RequestBody User user) {
        Optional<User> byId = userRepository.findByLogin(user.getLogin());
        if (!byId.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user1 = byId.get();
        for (Content c : user.getContents()) {
            user1.getContents().add(contentRepository.findById(c.getId()).get());
        }
        userRepository.save(user1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "delete_user_content", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUserContent(@RequestBody User user) {
        Optional<User> byId = userRepository.findByLogin(user.getLogin());
        if (!byId.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user1 = byId.get();
        for (Content c : user.getContents()) {
            user1.getContents().remove(contentRepository.findById(c.getId()).get());
        }
        userRepository.save(user1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}



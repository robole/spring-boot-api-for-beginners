package net.roboleary.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping ("/users")
public class UserController {
    List<User> users = new ArrayList<User>();

    //no users
    public UserController(){ }

    //GET for http://localhost:8080/users . Returns all users. Alternatively, you can use @GetMapping
    //@RequestMapping(method=GET, value="/users")
    @GetMapping
    public List<User> getUsers(){
        return users;
    }

    //GET for http://localhost:8080/users/id .Returns all users with the id provided
    @GetMapping(value="/{id}")
    public ResponseEntity getUsersById(@PathVariable("id") long id){
        User userFound = null;

        for(User user: users){
            if(user.getId() == id){
                userFound = user;
                break;
            }
        }

        if (userFound == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //found
        return new ResponseEntity(userFound, HttpStatus.OK);
    }

    //GET for http://localhost:8080/users?name=rob oleary .Returns all users with the name provided
    @GetMapping(params = "name")
    public ResponseEntity getUsersByName(@RequestParam(value="name") String name){
        List<User> filteredUsers = new ArrayList<User>();

        for(User user: users){
            if(user.getName().equalsIgnoreCase(name)) {
                filteredUsers.add(user);
            }
        }

        if (filteredUsers.isEmpty() == true) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //found
        return new ResponseEntity(filteredUsers, HttpStatus.OK);
    }

    //GET for http://localhost:8080/users?age=21 .Returns all users with the age provided
    @GetMapping(params= "age")
    public ResponseEntity getUsersByAge(@RequestParam(value="age") int age){
        List<User> filteredUsers = new ArrayList<User>();

        for(User user: users){
            if(user.getAge() == age) {
                filteredUsers.add(user);
            }
        }

        if (filteredUsers.isEmpty() == true) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //found
        return new ResponseEntity(filteredUsers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody User u) {
        users.add(u);
        return new ResponseEntity(u, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity addOrUpdate(@RequestBody User u) {
        ResponseEntity response;

        if(users.contains(u)){
            //update by setting it at the specified position
            int index = users.indexOf(u);
            users.set(index, u);
            response = new ResponseEntity(u, HttpStatus.OK);
        }
        else{
            users.add(u);
            response = new ResponseEntity(u, HttpStatus.CREATED);
        }

        return response;
    }


    @DeleteMapping(value="/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        boolean found = false;

        for(User user: users){
            if(user.getId() == id){
                users.remove(user);
                found = true;
                break;
            }
        }

        if (found == false) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //found
        return new ResponseEntity(HttpStatus.OK);
    }
}


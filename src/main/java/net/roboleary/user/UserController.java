package net.roboleary.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class UserController {
    List<User> users = new ArrayList<User>();

    //We create data to use in our Controller. You can learn how to get the data from a database later, this is
    //what is usually done!
    public UserController(){
        users.add(new User(1, "Rob OLeary", 21));
        users.add(new User(2, "Angela Merkel", 20));
        users.add(new User(3, "Tamer Osman", 20));
    }

    //GET for http://localhost:8080/users . Returns all users. Alternatively, you can use @GetMapping
    //@RequestMapping(method=GET, value="/users")
    @GetMapping(value="/users")
    public List<User> getUsers(){
        return users;
    }

    //GET for http://localhost:8080/users/id .Returns all users with the id provided
    @GetMapping(value="/users/{id}")
    public User getUsersById(@PathVariable("id") long id){
        User found = null;

        for(User user: users){
            if(user.getId() == id){
                found = user;
                break;
            }
        }

        return found;
    }

    //GET for http://localhost:8080/users?name=rob oleary .Returns all users with the name provided
    @GetMapping(value="/users", params = "name")
    public List<User> getUsersByName(@RequestParam(value="name") String name){
        List<User> filteredUsers = new ArrayList<User>();

        for(User user: users){
            if(user.getName().equalsIgnoreCase(name)) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

    //GET for http://localhost:8080/users?age=21 .Returns all users with the age provided
    @GetMapping(value = "/users", params= "age")
    public List<User> getUsersByAge(@RequestParam(value="age") int age){
        List<User> filteredUsers = new ArrayList<User>();

        for(User user: users){
            if(user.getAge() == age) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

    @PostMapping(value="users")
    public ResponseEntity add(@RequestBody User u) {
        users.add(u);
        return new ResponseEntity(u, HttpStatus.CREATED);
    }

    @PutMapping(value="users")
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


    @DeleteMapping(value="users/{id}")
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


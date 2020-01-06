package pisibg.ittalents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import pisibg.ittalents.model.User;
import pisibg.ittalents.dao.UserDao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao dao;

    @GetMapping(value ="/users/all")
    public  List<User> getAll(){
        return dao.getAllUsers();
    }

    @PostMapping(value= "/users/add")
    public void saveUser(@RequestBody User u){
        //validation! - error - return statuscode
        System.out.println(u.toString());
    }

    @GetMapping (value = "users/{name}")
    public User getByName(@PathVariable("name") String name){
        for (User u : getAll()) {
            if(u.getFirstName().equals(name)){
                return u;
            }
        }
        return null;
    }

//    @PostMapping
//    public void login(HttpServletResponse resp){
//    resp.setStatus(418);
//    }
}

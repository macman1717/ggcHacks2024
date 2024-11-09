package com.example.mdbspringboot.controllers;

import com.example.mdbspringboot.model.Card;
import com.example.mdbspringboot.model.User;
import com.example.mdbspringboot.repository.CardRepository;
import com.example.mdbspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    CardRepository cardRepo;
    @GetMapping("/{username}")
    public User findUser(@PathVariable("username") String username){
        User user = userRepo.findUserByUsername(username);
        user.setPassWord("Hidden");
        return user;
    }

    @GetMapping("/login")
    public User loginUser(@RequestParam String username, @RequestParam String password){
        User user = userRepo.findUserByUsername(username);
        if(user.getPassWord().equals(password)){
            return user;
        }else{
            return new User("ERROR","ERROR",new ArrayList<>());
        }
    }

    @GetMapping("/{username}/sets")
    public List<String> getListOfSets(@PathVariable("username") String username){
        return userRepo.findUserByUsername(username).getSets();
    }

    @PostMapping("/{username}/addSet/{setName}")
    public void addSet(@PathVariable("username") String username, @RequestBody List<Card> cards, @PathVariable("setName") String setName){
        User user = userRepo.findUserByUsername(username);
        user.getSets().add(setName);
        userRepo.save(user);
        for(Card card : cards){
            card.setOwner(username);
            card.setParentSet(username + "-" + setName);
        }
        cardRepo.saveAll(cards);
    }

}

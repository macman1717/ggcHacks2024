package com.example.mdbspringboot.controllers;

import com.example.mdbspringboot.model.Card;
import com.example.mdbspringboot.model.User;
import com.example.mdbspringboot.repository.CardRepository;
import com.example.mdbspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
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
        System.out.println("User:" + username);
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

    @PostMapping("/{username}/addSet/{parentSet}")
    public void addSet(@PathVariable("username") String username, @RequestBody List<Card> cards, @PathVariable("parentSet") String parentSet){
        System.out.println("HERE");
        User user = userRepo.findUserByUsername(username);
        user.getSets().add(parentSet);
        userRepo.save(user);
        for(Card card : cards){
            System.out.println(card);
            card.setOwner(username);
            card.setParentSet(username + "-" + parentSet);
        }
        cardRepo.saveAll(cards);
    }

    @DeleteMapping("/{username}/deleteSet/{setName}")
    public void deleteSet(@PathVariable("username") String username, @PathVariable("setName") String setName){
        User user = userRepo.findUserByUsername(username);
        ArrayList<String> sets = user.getSets();
        sets.removeIf(existingSetName -> existingSetName.equals(setName));
        userRepo.save(user);

        String parentSet = username + "-" + setName;
        List<Card> cards = cardRepo.findAll(parentSet);
        cardRepo.deleteAll(cards);
    }

}

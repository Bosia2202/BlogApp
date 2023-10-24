package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.models.Follower;
import com.denisvasilenko.BlogApp.models.Subscription;
import com.denisvasilenko.BlogApp.models.User;
import com.denisvasilenko.BlogApp.repositories.FollowerRepository;
import com.denisvasilenko.BlogApp.repositories.SubscriptionRepository;
import com.denisvasilenko.BlogApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServices {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    private final SubscriptionRepository subscriptionRepository;
    @Autowired
    public UserServices(UserRepository userRepository, FollowerRepository followerRepository,
                        SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User uploadUser(Long id,User user)
    {
      user.setId(id);
      return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User findUserByUserName(String name){
       Optional<User> user = Optional.ofNullable(userRepository.findByUsername(name));
       return user.orElse(null);
    }

    public List<Follower> getAllUserFollowers(User user){
        return user.getFollowers();
    }

    public void addSubscriptionOnUser(Long id, User user){
        User subscriptionUser=userRepository.getReferenceById(id);
        String userOwner= user.getUsername();
        Follower follower =new Follower();
        follower.setFollower(userOwner);
        follower.setUserOwner(user);
        followerRepository.save(follower);

        Subscription subscription=new Subscription();
        subscription.setSubscriptionUserName(userOwner);
        subscription.setUserOwner(user);
        subscriptionRepository.save(subscription);
    }



}

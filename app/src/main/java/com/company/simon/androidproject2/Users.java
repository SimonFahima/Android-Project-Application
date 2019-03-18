package com.company.simon.androidproject2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Users {

    private static Users users;
    private Map<String, User> userMap;

    Users(){
        //singleton - prevent creation of an instance;
        userMap = new HashMap<>();
    }


    public static Users getUsers(){
        if(users == null){
            users = new Users();
        }
        return users;
    }


    public boolean hasUser(User user){
        if(userMap.containsKey(user.getUserName())){
            User existingUser = userMap.get(user.getUserName());
            if (existingUser == null)
                throw new AssertionError();
            String existingPassword = existingUser.getPassword();
            return user.getPassword().equals(existingPassword);
        }
        return false;
    }


    public void loadUsers(Set<String> usersAsString){
        for(String userAsString : usersAsString){
            User user = new User(userAsString);
            userMap.put(user.getUserName(), user);
        }
    }

    public Set<String> getUsersAsString(){
        Set<String> set = new HashSet<>();
        for(User user : userMap.values()){
            set.add(user.toString());
        }
        return set;
    }
}

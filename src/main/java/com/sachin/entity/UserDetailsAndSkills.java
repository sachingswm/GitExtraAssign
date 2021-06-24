package com.sachin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

@Getter
@Setter
@ToString
public class UserDetailsAndSkills {
    private String email;
    private String name;
    private String phone;
    private String state;
    private String gender;
    private String photos;
    private List<String> skills;


    public UserDetails getUserDetails()
    {
        return new UserDetails(this.email,this.name,this.phone,this.state,this.gender,this.photos);
    }
}

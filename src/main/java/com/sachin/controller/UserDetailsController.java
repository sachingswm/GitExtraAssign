package com.sachin.controller;

import com.sachin.entity.FileMaster;
import com.sachin.entity.Skill;
import com.sachin.entity.UserDetailsAndSkills;
import com.sachin.service.FileMasterService;
import com.sachin.service.SkillService;
import com.sachin.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private FileMasterService fileMasterService;

    @GetMapping("/")
    public String showForm()
    {
        return "user_details_form";
    }


    @GetMapping("/getfile/{id}")
    public ResponseEntity files(@PathVariable("id") int id) throws Exception
    {
        FileMaster file_master = fileMasterService.getFileMasterById(id);
        if(file_master==null)
        {
            return ResponseEntity.badRequest().body("file not found");
        }
        byte[] image = Files.readAllBytes(Paths.get(file_master.getFileDirectory()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }




    @PostMapping("/addUserDetails")
    public ModelAndView addUserDetails(@ModelAttribute UserDetailsAndSkills userDetailsAndSkills, @RequestParam("image") MultipartFile multipartFile, ModelAndView modelAndView)
    throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        for(String skill:userDetailsAndSkills.getSkills())
        {
            Skill currSkill=skillService.addSkill(skill,userDetailsAndSkills.getEmail());
            skillService.addSkill(currSkill);
        }
        modelAndView.addObject("userDetailsAndSkills",userDetailsAndSkills);
        Files.copy(multipartFile.getInputStream(), Paths.get("E:\\SpringBoot\\Project5\\src\\main\\resources\\static\\images\\"+fileName), StandardCopyOption.REPLACE_EXISTING);
        FileMaster fileMaster=fileMasterService.addFileMaster(fileName,userDetailsAndSkills.getEmail());
        fileMasterService.addFileMaster(fileMaster);
        userDetailsAndSkills.setPhotos("http://localhost:8080/getfile/"+String.valueOf(fileMaster.getId()));
        userDetailsService.addUserDetails(userDetailsAndSkills.getUserDetails());
        modelAndView.addObject("image","http://localhost:8080/getfile/"+String.valueOf(fileMaster.getId()));
        modelAndView.setViewName("user_details_success");
        return modelAndView;
    }

    @PostMapping("/updateUser")
    public  ModelAndView updateUser(@RequestParam("email") String email,ModelAndView modelAndView)
    {
        System.out.println(email);
        modelAndView.setViewName("update_user_details_form");
        modelAndView.addObject("userDetails",userDetailsService.getUserDetailsByEmail(email));
        modelAndView.addObject("userSkills",skillService.getAllByUserDetailsId(email));
        modelAndView.addObject("image",userDetailsService.getUserDetailsByEmail(email).getPhotos());
        System.out.println(skillService.getAllNameByUserDetailsId(email));
        modelAndView.addObject("allSkills",skillService.getAllNameByUserDetailsId(email));
        return modelAndView;
    }

    @PostMapping("/updateEditedUserDetails")
    public ModelAndView updateEditedUserDetails(@ModelAttribute UserDetailsAndSkills userDetailsAndSkills
            , @RequestParam("oldEmail") String oldEmail
            , @RequestParam("updateimage") MultipartFile multipartFile, ModelAndView modelAndView)
            throws IOException
    {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println(userDetailsAndSkills+" "+oldEmail+fileName);
        for(String skill:userDetailsAndSkills.getSkills())
        {
            Skill currSkill=skillService.addSkill(skill,userDetailsAndSkills.getEmail());
            skillService.updateSkill(oldEmail,currSkill);
        }
        modelAndView.setViewName("user_details_success");
        modelAndView.addObject("userDetailsAndSkills",userDetailsAndSkills);
        Files.copy(multipartFile.getInputStream(), Paths.get("E:\\SpringBoot\\Project5\\src\\main\\resources\\static\\images\\"+fileName), StandardCopyOption.REPLACE_EXISTING);
        FileMaster fileMaster=fileMasterService.addFileMaster(fileName,userDetailsAndSkills.getEmail());
        fileMasterService.updateFileMaster(oldEmail,fileMaster);
        userDetailsAndSkills.setPhotos("http://localhost:8080/getfile/"+String.valueOf(fileMaster.getId()));
        userDetailsService.updateUserDetails(oldEmail,userDetailsAndSkills.getUserDetails());
        modelAndView.addObject("image","http://localhost:8080/getfile/"+String.valueOf(fileMaster.getId()));
        return modelAndView;
    }

}

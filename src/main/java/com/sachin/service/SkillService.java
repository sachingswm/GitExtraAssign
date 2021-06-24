package com.sachin.service;

import com.sachin.dao.SkillDao;
import com.sachin.entity.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SkillService {
    @Autowired
    private SkillDao skillDao;

    public Skill addSkill(String skill,String userDetailsEmail)
    {
        Skill currSkill=new Skill();
        currSkill.setName(skill);
        currSkill.setUserDetailsId(userDetailsEmail);
        return currSkill;
    }

    public Skill addSkill(Skill skill)
    {
        return skillDao.save(skill);
    }

    public List<Skill> getAllByUserDetailsId(String userDetailsId)
    {
        return skillDao.getAllByUserDetailsId(userDetailsId);
    }

    public List<String> getAllNameByUserDetailsId(String userDetailsId)
    {
        //System.out.println(skillDao.getAllNameByUserDetailsId(userDetailsId));
        return  skillDao.getAllNameByUserDetailsId(userDetailsId);
    }

    public void deleteByUserDetailsId(String id)
    {
        skillDao.deleteByUserDetailsId(id);
    }

    @Modifying
    public void updateSkill(String id,Skill newSkill)
    {
        skillDao.deleteByUserDetailsId(id);
        Skill sk=skillDao.save(newSkill);
    }

}

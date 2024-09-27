package com.example.Admin.repository;

import com.example.Admin.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<Profile,Long>{
    Profile findByProfileName(String profileName);

}


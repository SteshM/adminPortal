package com.example.Admin.repository;

import com.example.Admin.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepo extends JpaRepository<MyUser,Long> {
    public MyUser findByEmail(String email);

}

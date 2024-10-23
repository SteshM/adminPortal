package com.example.service.Components.Main;

import com.example.dto.ExtendedRes;
import com.example.dto.RetailerDto;
import com.example.model.MyUser;
import com.example.model.Profile;
import com.example.repository.MyUserRepo;
import com.example.repository.ProfileRepo;
import com.example.service.Components.utils.MyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class RetailerService {

        private final MyUserRepo myUserRepo;
        private final ProfileRepo profileRepo;

        public ExtendedRes getAllRetailers(){
            Profile profile = profileRepo.findById(2l).get();
            List<MyUser> retailers= myUserRepo.findAll().stream().filter(user-> user.getProfiles().contains(profile)).toList();
            List<RetailerDto> retailerDtos =retailers.stream()
                    .map(retailer-> MyDtoMapper.mapDtoToClass(retailer, RetailerDto.class))
                    .collect(Collectors.toList());
            return  ExtendedRes.builder()
                    .status(200)
                    .message("A list of all retailers")
                    .body(retailerDtos)
                    .build();
        }

    }

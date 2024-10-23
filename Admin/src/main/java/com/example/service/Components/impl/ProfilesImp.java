package com.example.service.Components.impl;


//import com.example.Admin.dto.*;
import com.example.dto.*;
import com.example.model.MyUser;
import com.example.model.Profile;
import com.example.model.Role;
import com.example.repository.MyUserRepo;
import com.example.repository.ProfileRepo;
import com.example.repository.RoleRepo;
import com.example.service.Components.Main.ProfileServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilesImp implements ProfileServices {
    private final ProfileRepo profileRepo;
    private final RoleRepo roleRepo;
    private final MyUserRepo myUserRepo;

    @Override
    public MinimalRes assignUserProfile(UserProfileDto userProfileDto) {
        Optional<MyUser> user = myUserRepo.findById(userProfileDto.getUserId());
        if(user.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }

        Optional<Profile> profile = profileRepo.findById(userProfileDto.getProfileId());
        if(profile.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile not found")
                    .build();
        }

        Collection<Profile> profiles = user.get().getProfiles();
        if(profiles.contains(profile.get())){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile already assigned to user")
                    .build();
        }
        profiles.add(profile.get());
        user.get().setProfiles(profiles);
        myUserRepo.save(user.get());
        return MinimalRes.builder()
                .status(200)
                .message("Profile assigned to "+user.get().getFullName()+" successfully")
                .build();
    }

    @Override
    public MinimalRes removeProfileFromUser(UserProfileDto userProfileDto) {
        Optional<MyUser> user = myUserRepo.findById(userProfileDto.getUserId());
        if(user.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }

        Optional<Profile> profile = profileRepo.findById(userProfileDto.getProfileId());
        if(profile.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile not found")
                    .build();
        }

        Collection<Profile> profiles = user.get().getProfiles();
        if(!profiles.contains(profile.get())){
            return MinimalRes.builder()
                    .status(400)
                    .message("User has no such profile")
                    .build();
        }
        profiles.remove(profile.get());
        user.get().setProfiles(profiles);
        myUserRepo.save(user.get());
        return MinimalRes.builder()
                .status(200)
                .message("Profile removed from "+user.get().getFullName()+" successfully")
                .build();
    }

    @Override
    public MinimalRes addRoleToProfile(RoleProfileDto roleProfileDto) {
        Optional<Role> role = roleRepo.findById(roleProfileDto.getRoleId());
        if(role.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Role not found")
                    .build();
        }

        Optional<Profile> profile = profileRepo.findById(roleProfileDto.getProfileId());
        if(profile.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile not found")
                    .build();
        }

        Collection<Role> roles = profile.get().getRoles();
        if(roles.contains(role.get())){
            return MinimalRes.builder()
                    .status(400)
                    .message("Role already assigned to profile")
                    .build();
        }
        roles.add(role.get());
        profile.get().setRoles(roles);
        profileRepo.save(profile.get());
        return MinimalRes.builder()
                .status(200)
                .message("Role assigned to profile "+profile.get().getProfileName())
                .build();
    }

    @Override
    public MinimalRes removeRoleFromProfile(RoleProfileDto roleProfileDto) {
        Optional<Role> role = roleRepo.findById(roleProfileDto.getRoleId());
        if(role.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Role not found")
                    .build();
        }

        Optional<Profile> profile = profileRepo.findById(roleProfileDto.getProfileId());
        if(profile.isEmpty()){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile not found")
                    .build();
        }

        Collection<Role> roles = profile.get().getRoles();
        if(!roles.contains(role.get())){
            return MinimalRes.builder()
                    .status(400)
                    .message("Role not assigned to profile")
                    .build();
        }
        roles.remove(role.get());
        profile.get().setRoles(roles);
        profileRepo.save(profile.get());
        return MinimalRes.builder()
                .status(200)
                .message("Role removed from profile "+profile.get().getProfileName())
                .build();
    }

    @Override
    public ExtendedRes getProfiles() {
        return ExtendedRes.builder()
                .status(200)
                .message("List of profiles")
                .body(profileRepo.findAll())
                .build();
    }

    @Override
    public ExtendedRes getRoles() {
        return ExtendedRes.builder()
                .status(200)
                .message("List of roles")
                .body(roleRepo.findAll())
                .build();
    }

    @Override
    public ExtendedRes getUserProfiles(Long userId) {
        Optional<MyUser> user = myUserRepo.findById(userId);
        if(user.isEmpty()){
            return ExtendedRes.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }
        return ExtendedRes.builder()
                .status(200)
                .message("List of "+user.get().getFullName()+" profiles")
                .body(user.get().getProfiles())
                .build();
    }

    @Override
    public MinimalRes createProfile(ProfileDto profile) {
        Profile profile2= profileRepo.findByProfileName(profile.getProfileName());
        if(profile2 != null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Profile exists")
                    .build();
        }
        profile2 = new Profile();
        profile2.setProfileName(profile.getProfileName());
        profileRepo.save(profile2);
        return MinimalRes.builder()
                .status(200)
                .message("Profile added")
                .build();
    }

}

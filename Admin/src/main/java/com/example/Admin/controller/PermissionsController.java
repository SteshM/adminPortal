package com.example.Admin.controller;

import com.example.Admin.dto.*;
import com.example.Admin.service.Components.Main.ProfileServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
    @RequiredArgsConstructor
    @PreAuthorize("hasAuthority('Permisions Management')")
    public class PermissionsController {
        private final ProfileServices profileServices;

        @PostMapping("addProfileToUser")
        public MinimalRes assignUserProfile(@RequestBody UserProfileDto userProfileDto) {
            return profileServices.assignUserProfile(userProfileDto);
        }

        @PostMapping("removeProfileFromUser")
        public MinimalRes removeProfileFromUser(@RequestBody UserProfileDto userProfileDto) {
            return profileServices.removeProfileFromUser(userProfileDto);
        }

        @PostMapping("addRoleToProfile")
        public MinimalRes addRoleToProfile(@RequestBody RoleProfileDto roleProfileDto) {
            return profileServices.addRoleToProfile(roleProfileDto);
        }

        @PostMapping("removeRoleFromProfile")
        public MinimalRes removeRoleFromProfile(@RequestBody RoleProfileDto roleProfileDto) {
            return profileServices.removeRoleFromProfile(roleProfileDto);
        }

        @GetMapping("getProfiles")
        public ExtendedRes getProfiles() {
            return profileServices.getProfiles();
        }
        @GetMapping("getRoles")
        public ExtendedRes getRoles() {
            return profileServices.getRoles();
        }
        @GetMapping("getUserProfiles/{userId}")
        public ExtendedRes getUserProfiles(@PathVariable Long userId) {
            return profileServices.getUserProfiles(userId);
        }

        @PostMapping("createProfile")
        public MinimalRes addProfile(@RequestBody ProfileDto profile){
            return profileServices.createProfile(profile);
        }
    }


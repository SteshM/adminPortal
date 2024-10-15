package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.*;

public interface ProfileServices {
    public MinimalRes assignUserProfile(UserProfileDto userProfileDto);
    public MinimalRes removeProfileFromUser(UserProfileDto userProfileDto);
    public MinimalRes addRoleToProfile(RoleProfileDto roleProfileDto);
    public MinimalRes removeRoleFromProfile(RoleProfileDto roleProfileDto);
    public MinimalRes createProfile(ProfileDto profile);
    public ExtendedRes getProfiles();
    public ExtendedRes getRoles();
    public ExtendedRes getUserProfiles(Long userId);
}
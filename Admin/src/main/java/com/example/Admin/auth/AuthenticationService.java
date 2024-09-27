package com.example.Admin.auth;

import com.example.Admin.dto.MinimalRes;
import com.example.Admin.dto.RegisterDto;
import com.example.Admin.dto.UserResponseDto;
import com.example.Admin.model.MyUser;
import com.example.Admin.model.Profile;
import com.example.Admin.repository.MyUserRepo;
import com.example.Admin.repository.ProfileRepo;
import com.example.Admin.service.Components.Main.CustomUser;
import com.example.Admin.service.Components.impl.AnalyticGuide;
import com.example.Admin.service.Components.utils.Exchanger;
import com.example.Admin.service.Components.utils.JwtGenerator2;
import com.example.Admin.service.Components.utils.PhoneNumberEditor;
import com.example.Admin.service.Components.utils.RandomGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
//    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AnalyticGuide analyticGuide;
    private final ProfileRepo profileRepo;
    //private final LocationRepository locationRepository;

    private final JwtGenerator2 jwtGenerator2;
    private final Exchanger exchanger;

    private final MyUserRepo myUserRepo;

    @Value("${pass.length}")
    private int length;

    @Value("${email.sender}")
    private String emailSenderUrl;

    public Map<String, Object> register(RegisterDto registerDto) {
        log.info("adding new user");
        Map<String, Object> map = new HashMap<>();
        if(myUserRepo.findByEmail(registerDto.getEmail()) != null){
            map.put("status", 409);
            map.put("success", false);
            map.put("message", "user exists");
            return  map;
        }else{
            MyUser user = new MyUser();
            String password = RandomGenerator.generateRandom(length);
            log.info("password: "+password);
            //authenticate email
            JsonNode emailValidation = exchanger.sendGetRequest(emailSenderUrl+"/mail/validate/"+registerDto.getEmail());

            if(!emailValidation.get("deliverability").asText().equals("DELIVERABLE")){
                map.put("status", 400);
                map.put("success", false);
                map.put("message", "email invalid");
                return  map;
            }
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(password));
            user.setContact(PhoneNumberEditor.resolveNumber(registerDto.getContact()));
            user.setOtp(true);
            user.setTracker(0);
            user.setFullName(registerDto.getFullName());
            user.setEmployeeNo(registerDto.getEmployeeNo());

            //send otp message
            Map<String ,String> messageMap = new HashMap<>();
            messageMap.put("subject", "OTP(do not disclose)");
            messageMap.put("receiverName", registerDto.getFullName());
            messageMap.put("templateName", "otp");
            messageMap.put("to",registerDto.getEmail());
            messageMap.put("otp", password);
            messageMap.put("message", "You have been registered to our admin portal. We're excited to have you on board.");

            exchanger.sendPostEmailRequest(emailSenderUrl+"/mail/sendMail", messageMap);

            Collection<Profile> profiles = new ArrayList<>();
            if(registerDto.getWho().equalsIgnoreCase("admin") || registerDto.getWho().equalsIgnoreCase("SuperAdmin")){
                if(registerDto.getWho().equalsIgnoreCase("admin")){
                    Profile profile = profileRepo.findById(1l).get();
                    profiles.add(profile);
                    user.setProfiles(profiles);
                    myUserRepo.save(user);
                }else{
                    Profile profile = profileRepo.findById(3l).get();
                    profiles.add(profile);
                    user.setProfiles(profiles);
                    myUserRepo.save(user);
                }


                ///change later
                map.put("status", 200);
                map.put("success", true);
                map.put("object", registerDto);
                return map;
            }else if(registerDto.getWho().equalsIgnoreCase("driver")){
                Profile profile = profileRepo.findById(4l).get();
                profiles.add(profile);
                user.setProfiles(profiles);
                myUserRepo.save(user);
                map.put("status", 200);
                map.put("success", true);
                map.put("object", registerDto);
                return map;
            }else {
                map.put("status", 400);
                map.put("success", false);
                map.put("message", "Who is a null value");
                return  map;
            }
        }
    }

    public Map<String, Object> registerRetailer(RegisterDto registerDto){
        log.info("adding new user");
        Map<String, Object> map = new HashMap<>();
        if(myUserRepo.findByEmail(registerDto.getEmail()) != null){
            map.put("status", 409);
            map.put("success", false);
            map.put("message", "user exists");
            return  map;
        }else{
            MyUser user = new MyUser();
            String password = RandomGenerator.generateRandom(length);
            log.info("password: "+password);
            //authenticate email
            JsonNode emailValidation = exchanger.sendGetRequest(emailSenderUrl+"/mail/validate/"+registerDto.getEmail());

            if(!emailValidation.get("deliverability").asText().equals("DELIVERABLE")){
                map.put("status", 400);
                map.put("success", false);
                map.put("message", "email invalid");
                return  map;
            }
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(password));
            user.setContact(PhoneNumberEditor.resolveNumber(registerDto.getContact()));
            user.setOtp(true);
            user.setTracker(0);
            user.setFullName(registerDto.getFullName());
            user.setEmployeeNo(registerDto.getEmployeeNo());


            //send otp message
            Map<String ,String> messageMap = new HashMap<>();
            messageMap.put("subject", "OTP(do not disclose)");
            messageMap.put("receiverName", registerDto.getFullName());
            messageMap.put("templateName", "otp");
            messageMap.put("to",registerDto.getEmail());
            messageMap.put("otp", password);
            messageMap.put("message", "You have been registered to our admin portal. We're excited to have you on board.");

            exchanger.sendPostEmailRequest(emailSenderUrl+"/mail/sendMail", messageMap);

            Collection<Profile> profiles = new ArrayList<>();
            Profile profile = profileRepo.findById(2l).get();
            profiles.add(profile);
            user.setProfiles(profiles);
            myUserRepo.save(user);


            analyticGuide.addCustomer();
            map.put("status", 200);
            map.put("success", true);
            map.put("object", registerDto);
            return map;
        }
    }



    public Map<String, Object> resetPassword(String email){
        MyUser user = myUserRepo.findByEmail(email);
        log.info(email);
        if(user == null){
            Map<String, Object> map = new HashMap<>();
            map.put("status", 400);
            map.put("success", false);
            map.put("message", "email invalid");
            return map;
        }else{
            String password = RandomGenerator.generateRandom(length);
            user.setOtp(true);
            user.setPassword(passwordEncoder.encode(password));
            myUserRepo.save(user);
            String fullName= user.getFullName();

            Map<String ,String> messageMap = new HashMap<>();
            messageMap.put("subject", "OTP(do not disclose)");
            messageMap.put("receiverName", fullName);
            messageMap.put("templateName", "otp");
            messageMap.put("to",user.getEmail());
            messageMap.put("otp", password);
            messageMap.put("message", "Here is your forget password OTP");

            exchanger.sendPostEmailRequest(emailSenderUrl+"/mail/sendMail", messageMap);

            Map<String, Object> map = new HashMap<>();
            map.put("status", 200);
            map.put("success", true);
            map.put("message", "email sent with otp");
            return map;
        }
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        MyUser user =myUserRepo.findByEmail(request.getEmail());
        Map<String, Object> map = new HashMap<>();
        if(user == null){
            map.put("success", false);
            map.put("message", "no user");
            return AuthenticationResponse.builder()
                    .status(400)
                    .object(map)
                    .build();
        }else{
            if(user.isOtp()){
                if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                    map.put("email", request.getEmail());
                    map.put("otp", request.getPassword());
                    map.put("Result", "Validated otp");
                    return AuthenticationResponse.builder()
                            .status(204)
                            .object(map)
                            .build();
                }else{
                    map.put("success", false);
                    map.put("result", "Otp not valid");
                    return AuthenticationResponse.builder()
                            .status(404)
                            .object(map)
                            .build();
                }

            }
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                List<String> roleList = new ArrayList<>();
                for(Profile profile: user.getProfiles()){
                    profile.getRoles().forEach(role->{
                        roleList.add(role.getRoleName());
                        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
                    });
                }
                User userDetails = new User(user.getEmail(), user.getPassword(), authorities);
                UserResponseDto userResponseDto = UserResponseDto.builder()
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .build();
                return AuthenticationResponse.builder()
                        .status(200)
                        .token(jwtGenerator2.token(userDetails))
                        .refreshToken(jwtGenerator2.refreshToken(userDetails))
                        .picture(user.getPicture())
                        .cloud(user.isCloud())
                        .roles(roleList)
                        .object(userResponseDto)
                        .build();
            }else{
                map.put("success", false);
                map.put("message", "wrong credentials");
                return AuthenticationResponse.builder()
                        .status(400)
                        .object(map)
                        .build();
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    ///used to update password, either with otp or with old password
    public MinimalRes changePassword(ResetPasswordDto resetPasswordDto){
        String email =resetPasswordDto.getEmail();

        if( email== null){
            log.info("normal change password");
            email = UserName.getUsername();
            MyUser user = myUserRepo.findByEmail(email);
            if(passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
                myUserRepo.save(user);
                return MinimalRes.builder()
                        .status(200)
                        .success(true)
                        .message("updated password")
                        .build();
            }else{
                return MinimalRes.builder()
                        .status(409)
                        .success(false)
                        .message("wrong password")
                        .build();
            }
        }else{
            log.info("otp change password");
            MyUser user = myUserRepo.findByEmail(email);

            if(passwordEncoder.matches(resetPasswordDto.getOtp(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
                user.setOtp(false);
                myUserRepo.save(user);
                return MinimalRes.builder()
                        .status(200)
                        .success(true)
                        .message("updated password")
                        .build();
            }else{
                return MinimalRes.builder()
                        .status(400)
                        .success(false)
                        .message("wrong otp")
                        .build();
            }
        }
    }

    public AuthenticationResponse refreshToken( HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null){
            return AuthenticationResponse.builder()
                    .status(300)
                    .message("no refresh token")
                    .build();
        }else{
            token = token.substring("Bearer ".length());
            if(token == null){
                return AuthenticationResponse.builder()
                        .status(300)
                        .message("token empty")
                        .build();
            }else{
                try{
                    CustomUser userDetails = jwtGenerator2.getUser(token);
                    log.info(userDetails.getUsername());
                    MyUser user = myUserRepo.findByEmail(userDetails.getUsername());
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    List<String> profileList = new ArrayList<>();
                    for(Profile profile: user.getProfiles()){
                        profileList.add(profile.getProfileName());
                        authorities.add(new SimpleGrantedAuthority(profile.getProfileName()));
                    }
                    User us = new User(user.getEmail(), user.getPassword(), authorities);
                    UserResponseDto userResponseDto = UserResponseDto.builder()
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .build();
                    return AuthenticationResponse.builder()
                            .status(200)
                            .token(jwtGenerator2.token(us))
                            .refreshToken(jwtGenerator2.refreshToken(us))
                            .roles(profileList)
                            .object(userResponseDto)
                            .build();

                }catch(Exception e){
                    return AuthenticationResponse.builder()
                            .status(303)
                            .message(e.getMessage())
                            .build();
                }

            }
        }
    }
}



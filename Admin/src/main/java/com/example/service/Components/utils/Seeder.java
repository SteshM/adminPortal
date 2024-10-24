package com.example.service.Components.utils;


import com.example.model.*;
import com.example.repository.*;
import com.example.service.Components.Main.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class Seeder {
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    DataSource dataSource;

    @Autowired
    MyUserRepo myUserRepo;
    @Autowired
    ProfileRepo profileRepo;

    @Autowired
    OrderService orderService;
    @Autowired
    DepotAdminRepo depotAdminRepo;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    DepotRepository depotRepository;
    @Autowired
    RoleRepo roleRepo;

    public void startSeeding(){
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRoleId(1l);
        role.setRoleName("User Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(2l);
        role.setRoleName("Depot Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(3l);
        role.setRoleName("Driver Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(4l);
        role.setRoleName("Retailer  Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(5l);
        role.setRoleName("Inventory  Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(6l);
        role.setRoleName("Retailer");
        roles.add(role);
        role = new Role();
        role.setRoleId(7l);
        role.setRoleName("Driver");
        roles.add(role);
        role = new Role();
        role.setRoleId(8l);
        role.setRoleName("Order Management");
        roles.add(role);
        role = new Role();
        role.setRoleId(9l);
        role.setRoleName("Permisions Management");
        roles.add(role);
        roleRepo.saveAll(roles);

        //saving profiles
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setProfileId(1L);
        profile.setProfileName("Admin");
        profiles.add(profile);
        profile = new Profile();
        profile.setProfileId(2L);
        profile.setProfileName("Retailer");
        profiles.add(profile);
        profile = new Profile();
        profile.setProfileId(3L);
        profile.setProfileName("SuperAdmin");
        Collection<Role> roles2 = new ArrayList<>();
        role = roleRepo.findById(1l).get();
        roles2.add(role);
        role = roleRepo.findById(2l).get();
        roles2.add(role);
        role = roleRepo.findById(3l).get();
        roles2.add(role);
        role = roleRepo.findById(4l).get();
        roles2.add(role);
        role = roleRepo.findById(9l).get();
        roles2.add(role);
        profile.setRoles(roles2);
        profiles.add(profile);
        profile = new Profile();
        profile.setProfileId(4L);
        profile.setProfileName("Driver");
        profiles.add(profile);
        profileRepo.saveAll(profiles);




        MyUser myUser = new MyUser();
        Collection<Profile> profiles2 = new ArrayList<>();
        Optional<Profile> profile2 = profileRepo.findById(3L);
        profiles2.add(profile2.get());
        myUser.setId(1L);
        myUser.setProfiles(profiles2);
        myUser.setFullName("John Doe");
        myUser.setEmployeeNo("EMP/001");
        myUser.setEmail("superadmin@gmail.com");
        myUser.setOtp(false);
        myUser.setContact("+2547585578");
        myUser.setPassword("$2a$10$6yyGwPXq12WSjTHgijtFZum6K6iVfkyJWhbdkwBQ/jJw03u7TkEOO");
        MyUser savedUser = myUserRepo.save(myUser);

        Depot depot = new Depot();

        depot.setName("Nairobi(Main)");
        depot.setDepotId(1l);
        Location location = new Location();
        location.setId(1l);
        location.setLat(-1.41555f);
        location.setLongitude(37.56665f);
        Location savedLocation = locationRepository.save(location);
        depot.setLocation(savedLocation);
        Depot savedDepot = depotRepository.save(depot);
        DepotAdmin depotAdmin = new DepotAdmin();
        depotAdmin.setId(1l);
        depotAdmin.setDepot(savedDepot);
        depotAdmin.setMyUser(savedUser);
        depotAdminRepo.save(depotAdmin);
    }
}

package com.example.Admin.service.Components.impl;

import com.example.Admin.dto.*;
import com.example.Admin.model.MyUser;
import com.example.Admin.model.Truck;
import com.example.Admin.model.TruckImages;
import com.example.Admin.repository.MyUserRepo;
import com.example.Admin.repository.TruckImagesRepo;
import com.example.Admin.repository.TruckRepo;
import com.example.Admin.service.Components.Main.TruckServices;
import com.example.Admin.service.Components.utils.FileUploader;
import com.example.Admin.service.Components.utils.MyDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TruckImpl implements TruckServices {

    @Value("${truck.pic}")
    private String filePath;

    @Value("${uploader.link}")
    private String uploadLink;

    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private TruckImagesRepo truckImagesRepo;
    @Autowired
    private TruckRepo truckRepo;
    @Autowired
    private MyUserRepo myUserRepo;

    @Override
    public MinimalRes createTruck(String data, List<MultipartFile> images) {
        Truck truck = MyDtoMapper.stringToClass(data, Truck.class);
        if (truckRepo.findByTruckNo(truck.getTruckNo()) != null) {
            return MinimalRes.builder()
                    .status(400)
                    .message("Truck exists in records")
                    .build();
        }
        Truck finaltruck = truckRepo.save(truck);
        List<TruckImages> truckImages = new ArrayList<>();
        images.forEach(image -> {
            TruckImages truckImage = new TruckImages();
            ImageUploadRes imageUploadRes = fileUploader.uploadImage(filePath, image, uploadLink + "/uploader/upload");
            if (imageUploadRes.getPublicId() != null) {
                truckImage.setCloud(true);
                truckImage.setPublicId(imageUploadRes.getPublicId());
            }
            truckImage.setPicture(imageUploadRes.getFileName());
            truckImage.setTruck(finaltruck);
            truckImages.add(truckImage);
        });
        truckImagesRepo.saveAll(truckImages);
        return MinimalRes.builder()
                .status(200)
                .message("Truck added successfully")
                .build();
    }

    @Override
    public MinimalRes updateTruck(TruckUpdateDto truckDto) {
        Optional<Truck> truck = truckRepo.findById(truckDto.getTruckId());
        if (!truck.isPresent()) {
            return MinimalRes.builder()
                    .status(400)
                    .message("Truck not found")
                    .build();
        }
        Truck truck2 = MyDtoMapper.mapDtoToClass(truckDto, Truck.class);
        truck2.setTruckId(truck.get().getTruckId());
        truck2.setTruckNo(truck.get().getTruckNo());
        truck2.setAvailable(truck.get().isAvailable());
        truck2.setUnderMaintenance(truck.get().isUnderMaintenance());
        truck2.setCreatedOn(truck.get().getCreatedOn());
        truckRepo.save(truck2);
        return MinimalRes.builder()
                .status(200)
                .message("Truck records updated")
                .build();
    }

    @Override
    public ExtendedRes getTrucks() {
        return ExtendedRes.builder()
                .status(200)
                .message("All trucks no filter")
                .body(truckRepo.findAll())
                .build();
    }


    @Override
    public ExtendedRes getTruckWithNoDrivers() {

        List<Truck> trucks = truckRepo.findByMyUserIsNull();
        return ExtendedRes.builder()
                .status(200)
                .message("Trucks with no drivers")
                .body(trucks)
                .build();
    }


    @Override
    public MinimalRes assignTruckToDriver(AssignTruckDto assignTruckDto) {
        Optional<MyUser> driver = myUserRepo.findById(assignTruckDto.getDriverId());
        if (!driver.isPresent()) {
            return MinimalRes.builder()
                    .status(400)
                    .message("Driver not found")
                    .build();
        }
        Optional<Truck> truck = truckRepo.findById(assignTruckDto.getTruckId());
        if (!truck.isPresent()) {
            return MinimalRes.builder()
                    .status(400)
                    .message("Truck not found")
                    .build();
        }

        truck.get().setMyUser(driver.get());
        truckRepo.save(truck.get());
        return MinimalRes.builder()
                .status(200)
                .message("Truck assigned to driver")
                .build();
    }



}

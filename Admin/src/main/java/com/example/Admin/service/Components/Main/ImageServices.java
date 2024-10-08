package com.example.Admin.service.Components.Main;


import com.example.Admin.dto.*;
import com.example.Admin.model.*;
import com.example.Admin.repository.*;
import com.example.Admin.service.Components.impl.ImageGuide;
import com.example.Admin.service.Components.utils.FileUploader;
import com.example.Admin.service.Components.utils.UserName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServices implements ImageGuide {
    @Autowired
    MyUserRepo myUserRepo;
    @Autowired
    DepotRepository depotRepository;
    @Autowired
    FileUploader fileUploader;
    @Autowired
    PendingLocalFileRepo pendingLocalFileRepo;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    QuantityAttributeRepo quantityAttributeRepo;
    @Autowired
    ShopRepo shopRepo;
    @Autowired
    ShopImagesRepo shopImagesRepo;
    @Autowired
    TruckRepo truckRepo;
    @Autowired
    TruckImagesRepo truckImagesRepo;



    @Value("${profile.pic}")
    String profileRootFolder;
    @Value("${depot.pic}")
    String depotRootFolder;
    @Value("${product.pic}")
    String productRootFolder;
    @Value("${uploader.link}")
    String uploaderLink;
    @Value("${shop.pic}")
    String shopRootFolder;
    @Value("${truck.pic}")
    String truckRootFolder;


    @Override
    public MinimalRes uploadProfilePic(MultipartFile file) {
        log.info(depotRootFolder);
        log.info(uploaderLink);
        MyUser user=  myUserRepo.findByEmail(UserName.getUsername());
        if(user == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("user not found")
                    .build();
        }
        if(user.getPicture() == null){
            ImageUploadRes imageUploadRes = fileUploader.uploadImage(profileRootFolder, file, uploaderLink+"/uploader/upload");
            if(imageUploadRes == null){
                return MinimalRes.builder()
                        .status(400)
                        .message("Image did not upload")
                        .build();
            }
            if(imageUploadRes.getPublicId() != null){
                user.setCloud(true);
                user.setPublicId(imageUploadRes.getPublicId());
            }
            user.setPicture(imageUploadRes.getFileName());
            myUserRepo.save(user);
            return MinimalRes.builder()
                    .status(200)
                    .message("uploadedSuccessfully")
                    .build();

        }else{
            String oldPublicId = user.getPublicId();
            String oldPictureName = user.getPicture();
            boolean isInCloud;

            ImageUploadRes imageUploadRes = new ImageUploadRes();



            if(user.isCloud()){
                imageUploadRes = fileUploader.uploadImage(profileRootFolder, file,  uploaderLink+"/uploader/update/"+user.getPublicId());
            }else{
                isInCloud = !fileUploader.deleteLocalPicture(profileRootFolder+"/"+oldPictureName);
                if(isInCloud){
                    PendingLocalFile pendingLocalFile = new PendingLocalFile();
                    pendingLocalFile.setFileName(oldPictureName);
                    pendingLocalFileRepo.save(pendingLocalFile);
                }
                imageUploadRes = fileUploader.uploadImage(profileRootFolder, file,  uploaderLink+"/uploader/upload");
            }

            if(imageUploadRes.getPublicId() == null){
                if(user.isCloud()){
                    fileUploader.deleteCloudPicture(uploaderLink, oldPublicId);
                }
                user.setPublicId(null);
                user.setCloud(false);
                user.setPicture(imageUploadRes.getFileName());
                myUserRepo.save(user);
            }else{
                user.setCloud(true);
                user.setPublicId(imageUploadRes.getPublicId());
                user.setPicture(imageUploadRes.getFileName());
                myUserRepo.save(user);
            }

            return MinimalRes.builder()
                    .status(200)
                    .message("updated successfully")
                    .build();
        }
    }

    @Override
    public MinimalRes uploadDepotPic(Long depotId, MultipartFile file) {
        Depot depot=  depotRepository.findById(depotId).get();
        if(depot == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("depot not found")
                    .build();
        }
        if(depot.getPicture() == null){
            ImageUploadRes imageUploadRes = fileUploader.uploadImage(profileRootFolder, file, uploaderLink+"/uploader/upload");
            if(imageUploadRes.getPublicId() != null){
                depot.setCloud(true);
                depot.setPublicId(imageUploadRes.getPublicId());
            }
            depot.setPicture(imageUploadRes.getFileName());
            depotRepository.save(depot);
            return MinimalRes.builder()
                    .status(200)
                    .message("uploadedSuccessfully")
                    .build();

        }else{
            String oldPublicId = depot.getPublicId();
            String oldPictureName = depot.getPicture();
            boolean isInCloud;

            ImageUploadRes imageUploadRes = new ImageUploadRes();



            if(depot.isCloud()){
                imageUploadRes = fileUploader.uploadImage(profileRootFolder, file,  uploaderLink+"/uploader/update/"+depot.getPublicId());
            }else{
                isInCloud = !fileUploader.deleteLocalPicture(profileRootFolder+"\\"+oldPictureName);
                if(isInCloud){
                    PendingLocalFile pendingLocalFile = new PendingLocalFile();
                    pendingLocalFile.setFileName(oldPictureName);
                    pendingLocalFileRepo.save(pendingLocalFile);
                }
                imageUploadRes = fileUploader.uploadImage(profileRootFolder, file,  uploaderLink+"/uploader/upload");
            }

            if(imageUploadRes.getPublicId() == null){
                if(depot.isCloud()){
                    fileUploader.deleteCloudPicture(uploaderLink, oldPublicId);
                }
                depot.setPublicId(null);
                depot.setCloud(false);
                depot.setPicture(imageUploadRes.getFileName());
                depotRepository.save(depot);
            }else{
                depot.setCloud(true);
                depot.setPublicId(imageUploadRes.getPublicId());
                depot.setPicture(imageUploadRes.getFileName());
                depotRepository.save(depot);
            }

            return MinimalRes.builder()
                    .status(200)
                    .message("updated successfully")
                    .build();
        }
    }

    @Override
    public ResponseEntity<byte[]> getLocalImage(String who, String imageName) {

        String filePath = "";
        if(who.equalsIgnoreCase("profile")){
            filePath = profileRootFolder+"/"+imageName;
        }else if(who.equalsIgnoreCase("depot")){
            filePath = depotRootFolder+"/"+imageName;
        }else if(who.equalsIgnoreCase("shop")){
            filePath = shopRootFolder+"/"+imageName;
        }else if(who.equalsIgnoreCase("truck")){
            filePath = truckRootFolder+"/"+imageName;
        }else{
            filePath = productRootFolder+"/"+imageName;
        }
        log.info(filePath);
        return fileUploader.getPic(filePath);
    }

    @Override
    public MinimalRes updateProductAttributePic(Long qattid,MultipartFile file) {
        QuantityAttribute quantityAttribute = quantityAttributeRepo.findById(qattid).get();
        if(quantityAttribute == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Attribute not found")
                    .build();
        }
        if(quantityAttribute.getPicture() == null){
            ImageUploadRes imageUploadRes = fileUploader.uploadImage(productRootFolder, file, uploaderLink+"/uploader/upload");
            if(imageUploadRes.getPublicId() != null){
                quantityAttribute.setCloud(true);
                quantityAttribute.setPublicId(imageUploadRes.getPublicId());
            }
            quantityAttribute.setPicture(imageUploadRes.getFileName());
            quantityAttributeRepo.save(quantityAttribute);
            return MinimalRes.builder()
                    .status(200)
                    .message("uploadedSuccessfully")
                    .build();

        }else{
            String oldPublicId = quantityAttribute.getPublicId();
            String oldPictureName = quantityAttribute.getPicture();
            boolean isInCloud;

            ImageUploadRes imageUploadRes = new ImageUploadRes();



            if(quantityAttribute.isCloud()){
                imageUploadRes = fileUploader.uploadImage(productRootFolder, file,  uploaderLink+"/uploader/update/"+quantityAttribute.getPublicId());
            }else{
                isInCloud= !fileUploader.deleteLocalPicture(productRootFolder+"/"+oldPictureName);
                if(isInCloud){
                    PendingLocalFile pendingLocalFile = new PendingLocalFile();
                    pendingLocalFile.setFileName(oldPictureName);
                    pendingLocalFileRepo.save(pendingLocalFile);
                }
                imageUploadRes = fileUploader.uploadImage(productRootFolder, file,  uploaderLink+"/uploader/upload");
            }

            if(imageUploadRes.getPublicId() == null){
                if(quantityAttribute.isCloud()){
                    fileUploader.deleteCloudPicture(uploaderLink, oldPublicId);
                }
                quantityAttribute.setPublicId(null);
                quantityAttribute.setCloud(false);
                quantityAttribute.setPicture(imageUploadRes.getFileName());
                quantityAttributeRepo.save(quantityAttribute);
            }else{
                quantityAttribute.setCloud(true);
                quantityAttribute.setPublicId(imageUploadRes.getPublicId());
                quantityAttribute.setPicture(imageUploadRes.getFileName());
                quantityAttributeRepo.save(quantityAttribute);
            }

            return MinimalRes.builder()
                    .status(200)
                    .message("updated successfully")
                    .build();
        }
    }

    @Override
    public ExtendedRes getProfilePic() {
        MyUser myUser = myUserRepo.findByEmail(UserName.getUsername());
        ImageDto profileImageDto = ImageDto.builder()
                .cloud(myUser.isCloud())
                .picture(myUser.getPicture())
                .build();
        if(profileImageDto.getPicture() == null){
            return ExtendedRes.builder()
                    .status(202)
                    .message("No image")
                    .build();
        }
        return ExtendedRes.builder()
                .status(200)
                .message("Picture")
                .body(profileImageDto)
                .build();
    }

    @Override
    public ExtendedRes getTruckImages(Long truckId) {
        Optional<Truck> truck = truckRepo.findById(truckId);
        List<TruckImages> truckImages = truckImagesRepo.findByTruck(truck.get());
        List<ImageDto> imageDtos = truckImages.stream().map(
                (truckImage)->{
                    return ImageDto.builder()
                            .cloud(truckImage.isCloud())
                            .id(truckImage.getId())
                            .picture(truckImage.getPicture())
                            .build();
                }
        ).collect(Collectors.toList());
        if(truck.isPresent()){
            return ExtendedRes.builder()
                    .status(200)
                    .message("Truck images")
                    .body(imageDtos)
                    .build();
        }else{
            return ExtendedRes.builder()
                    .status(400)
                    .message("Truck not found")
                    .build();
        }

    }

    @Override
    public ExtendedRes getShopImages(Long shopId) {
        Optional<Shop> shop = shopRepo.findById(shopId);
        if(shop.isPresent()){
            List<ShopImages> shopImages = shopImagesRepo.findByShop(shop.get());
            List<ImageDto> imageDtos = shopImages.stream().map(
                    (shopImage)->{
                        return ImageDto.builder()
                                .cloud(shopImage.isCloud())
                                .id(shopImage.getId())
                                .picture(shopImage.getPicture())
                                .build();
                    }
            ).collect(Collectors.toList());
            return ExtendedRes.builder()
                    .status(200)
                    .message("Shop images")
                    .body(imageDtos)
                    .build();
        }else{
            return ExtendedRes.builder()
                    .status(400)
                    .message("Shop not found")
                    .build();
        }
    }

    @Override
    public MinimalRes deleteShopImages(ImageDeleteDto imageDeleteDto) {
        log.info("Shop images: {}", imageDeleteDto.getImgIds());
        for(var i = 0; i< imageDeleteDto.getImgIds().size(); i++){
            Optional<ShopImages> shopImage = shopImagesRepo.findById(imageDeleteDto.getImgIds().get(i));
            if(shopImage.isPresent()){
                ShopImages shopImages = shopImage.get();
                if(shopImages.isCloud()){
                    fileUploader.deleteCloudPicture(uploaderLink, shopImages.getPublicId());
                }else{
                    fileUploader.deleteLocalPicture(shopImages.getPicture());
                }
                shopImagesRepo.delete(shopImages);
            }
        }
        return MinimalRes.builder()
                .status(200)
                .message("deleted image(s)")
                .build();
    }

    @Override
    public MinimalRes deleteTruckImages(ImageDeleteDto imageDeleteDto) {
        for(var i = 0; i< imageDeleteDto.getImgIds().size(); i++){
            Optional<TruckImages> truckImage = truckImagesRepo.findById(imageDeleteDto.getImgIds().get(i));
            if(truckImage.isPresent()){
                TruckImages truckImages = truckImage.get();
                if(truckImages.isCloud()){
                    fileUploader.deleteCloudPicture(uploaderLink, truckImages.getPublicId());
                }else{
                    fileUploader.deleteLocalPicture(truckImages.getPicture());
                }
                truckImagesRepo.delete(truckImages);
            }
        }
        return MinimalRes.builder()
                .status(200)
                .message("deleted image(s)")
                .build();
    }


}

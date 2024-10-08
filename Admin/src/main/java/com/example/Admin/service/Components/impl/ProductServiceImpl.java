package com.example.Admin.service.Components.impl;


import com.example.Admin.dto.*;
import com.example.Admin.model.Product;
import com.example.Admin.model.QuantityAttribute;
import com.example.Admin.repository.ProductsRepository;
import com.example.Admin.repository.QuantityAttributeRepo;
import com.example.Admin.service.Components.Main.ProductService;
import com.example.Admin.service.Components.utils.DateUtils;
import com.example.Admin.service.Components.utils.MyDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private QuantityAttributeRepo quantityAttributeRepo;
    @Autowired
    private ImageGuide imageGuide;

    @Autowired
//     private FileUploader fileUploader;
    @Value("${product.pic}")
    String productRootFolder;
    @Value("${uploader.link}")
    String uploaderLink;

    @Override
    public MinimalRes createProduct(ProductsDto productsDto) {
        Product product = MyDtoMapper.mapDtoToClass(productsDto,Product.class);
        product.setSoftDelete(false);
        productsRepository.save(product);
        return  MinimalRes.builder()
                .status(200)
                .message("Product created successfully")
                .build();

    }

    @Override
    public ExtendedRes findById(Long id) {
        Product product =  productsRepository.findByProductId(id);
        if(product == null){
            return ExtendedRes.builder()
                    .status(404)
                    .message("No product Found")
                    .body(null)
                    .build();
        }
        return ExtendedRes.builder()
                .status(200)
                .message("Found")
                .body(product)
                .build();
    }

    @Override
    public ExtendedRes updateById(ProductsDto productsDto) {
        Product product = productsRepository.findByProductId(productsDto.getId());
        product.setProductName(productsDto.getProductName());
        product.setDescription(productsDto.getDescription());
        Product updatedProduct= productsRepository.save(product);
        ProductsDto productDto = MyDtoMapper.mapDtoToClass(updatedProduct,ProductsDto.class);
        return ExtendedRes.builder()
                .status(200)
                .body(productDto)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public ExtendedRes findAll() {
        List<Product> products=productsRepository.findAll();
        List <ProductsDto> productsDto = products.stream().map(product ->
                        MyDtoMapper.mapDtoToClass(products,ProductsDto.class))
                .collect(Collectors.toList());
        return ExtendedRes.builder()
                .status(200)
                .message("A list of all products ")
                .body(productsDto)
                .build();
    }

    @Override
    public MinimalRes deleteById(Long id) {
        Product product = productsRepository.findByProductId(id);
        if(product == null){
            return  MinimalRes.builder()
                    .status(400)
                    .message("product not found")
                    .build();
        }
        product.setSoftDelete(true);
        productsRepository.save(product);
        return  MinimalRes.builder()
                .status(200)
                .message("product deleted successfully")
                .build();

    }

    @Override
    public MinimalRes addQAttribute(AddAttributeDto addAttributeDto, MultipartFile file) {
        Product product = productsRepository.findByProductId(addAttributeDto.getProductId());
        if(product != null){
            QuantityAttribute quantityAttribute = new QuantityAttribute();
            quantityAttribute.setAttrDescription(addAttributeDto.getAttrName());
            quantityAttribute.setProduct(product);
            quantityAttribute.setPrice(addAttributeDto.getPrice());
            QuantityAttribute attribute = quantityAttributeRepo.save(quantityAttribute);
            imageGuide.updateProductAttributePic(attribute.getQuantityAttId(), file);
            return MinimalRes.builder()
                    .status(200)
                    .message("Attribute added successfully")
                    .build();
        }
        return MinimalRes.builder()
                .status(400)
                .message("The product don't exis")
                .build();
    }

    @Override
    public ExtendedRes getProductQAttributes(Long productId) {
        Product product = productsRepository.findByProductId(productId);
        List<QuantityAttribute> quantityAttributes = quantityAttributeRepo.findByProduct(product);
        List<QuantityAttributeResponseDto> quantityAttributeResponseDtos = quantityAttributes.stream().map(
                quantityAttribute->{
                    QuantityAttributeResponseDto quantityAttributeResponseDto = QuantityAttributeResponseDto.builder()
                            .attrDescription(quantityAttribute.getAttrDescription())
                            .cloud(quantityAttribute.isCloud())
                            .picture(quantityAttribute.getPicture())
                            .productId(quantityAttribute.getProduct().getProductId())
                            .quantityAttId(quantityAttribute.getQuantityAttId())
                            .build();
                    if(quantityAttribute.getOfferFrom() != null|| quantityAttribute.getOfferTo() != null){
                        if(DateUtils.dayBeforeToday(quantityAttribute.getOfferFrom()) && DateUtils.dayAfterToday(quantityAttribute.getOfferTo())){
                            quantityAttributeResponseDto.setOfferOn(true);
                            quantityAttributeResponseDto.setPrice(quantityAttribute.getOfferPrice());
                            quantityAttributeResponseDto.setBeforeOffer(quantityAttribute.getPrice());
                            quantityAttributeResponseDto.setOfferTo(quantityAttribute.getOfferTo());
                        }else{
                            quantityAttributeResponseDto.setOfferOn(false);
                            quantityAttributeResponseDto.setPrice(quantityAttribute.getPrice());
                        }
                    }else{
                        quantityAttributeResponseDto.setOfferOn(false);
                        quantityAttributeResponseDto.setPrice(quantityAttribute.getPrice());
                    }
                    return quantityAttributeResponseDto;
                }
        ).toList();
        return ExtendedRes.builder()
                .status(200)
                .message("success")
                .body(quantityAttributeResponseDtos)
                .build();
    }

    @Override
    public ExtendedRes updateProductQAttById(QuantityAttribute attribute) {
        QuantityAttribute quantityAttribute = quantityAttributeRepo.findById(attribute.getQuantityAttId()).get();
        if (quantityAttribute != null) {

            quantityAttribute.setAttrDescription(attribute.getAttrDescription());
            quantityAttribute.setPrice(attribute.getPrice());

            QuantityAttribute quantityAttribute1=quantityAttributeRepo.save(quantityAttribute);

            return ExtendedRes.builder()
                    .status(200)
                    .message("Attribute updated successfully")
                    .body(quantityAttribute1)
                    .build();
        } else {
            return ExtendedRes.builder()
                    .status(404)
                    .message("Attribute not found")
                    .build();
        }

    }


    @Override
    public ExtendedRes getDisplayProducts() {
        List<DisplayProductDto> displayProductDtos = new ArrayList<>();
        List<Product> products = productsRepository.findAll().stream().filter(prod-> !prod.isSoftDelete()).toList();
        products.forEach(product->{
            QuantityAttribute quantityAttribute= quantityAttributeRepo.findByProduct(product, Limit.of(1));
            DisplayProductDto displayProductDto = DisplayProductDto.builder()
                    .productDescription(product.getDescription())
                    .productName(product.getProductName())
                    .productId(product.getProductId())
                    .build();

            if(quantityAttribute != null){
                displayProductDto.setCloud(quantityAttribute.isCloud());
                displayProductDto.setProduct1AttName(quantityAttribute.getAttrDescription());
                displayProductDto.setProductImage(quantityAttribute.getPicture());
                displayProductDto.setProduct1AttPrice(quantityAttribute.getPrice());
            }
            displayProductDtos.add(displayProductDto);
        });
        return ExtendedRes.builder()
                .status(200)
                .message("success")
                .body(displayProductDtos)
                .build();
    }

    @Override
    public MinimalRes addOffer(OfferDto offerDto) {
        Product product= productsRepository.findByProductId(offerDto.getProductId());
        if(product == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Product not found")
                    .build();
        }
        QuantityAttribute quantityAttribute = quantityAttributeRepo.findByProductAndQuantityAttId(product, offerDto.getProductAttrId());

        if(quantityAttribute == null){
            return MinimalRes.builder()
                    .status(400)
                    .message("Attribute not found")
                    .build();
        }

        quantityAttribute.setOfferFrom(offerDto.getOfferFrom());
        quantityAttribute.setOfferTo(offerDto.getOfferTo());
        quantityAttribute.setOfferPrice(offerDto.getOfferPrice());
        quantityAttributeRepo.save(quantityAttribute);

        return MinimalRes.builder()
                .status(200)
                .message("Offer added")
                .build();
    }

}

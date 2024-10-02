package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.AssignTruckDto;
import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.dto.MinimalRes;
import com.example.Admin.dto.TruckUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TruckServices {
    public MinimalRes createTruck(String data, List<MultipartFile> images);
    public MinimalRes updateTruck(TruckUpdateDto truckDto);
    public ExtendedRes getTrucks();
    public ExtendedRes getTruckWithNoDrivers();
    public MinimalRes assignTruckToDriver(AssignTruckDto assignTruckDto);

}

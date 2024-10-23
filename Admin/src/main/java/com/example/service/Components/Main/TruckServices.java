package com.example.service.Components.Main;

import com.example.dto.AssignTruckDto;
import com.example.dto.ExtendedRes;
import com.example.dto.MinimalRes;
import com.example.dto.TruckUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TruckServices {
    public MinimalRes createTruck(String data, List<MultipartFile> images);
    public MinimalRes updateTruck(TruckUpdateDto truckDto);
    public ExtendedRes getTrucks();
    public ExtendedRes getTruckWithNoDrivers();
    public MinimalRes assignTruckToDriver(AssignTruckDto assignTruckDto);

}

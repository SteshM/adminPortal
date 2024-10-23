package com.example.service.Components.Main;

import com.example.dto.DriverAssignDto;
import com.example.dto.DriverUpdateDto;
import com.example.dto.ExtendedRes;
import com.example.dto.MinimalRes;

public interface DriverService {
    ExtendedRes findDriverById(Long id);
    ExtendedRes findAllDrivers();
    ExtendedRes findAllDeletedDrivers();
    ExtendedRes getUnallocatedDrivers();
    ExtendedRes updateById(DriverUpdateDto driver);
    MinimalRes deleteById(Long id);
    MinimalRes assignDriverToDepot(DriverAssignDto driverAssignDto);

}

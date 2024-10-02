package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.DriverAssignDto;
import com.example.Admin.dto.DriverUpdateDto;
import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.dto.MinimalRes;

public interface DriverService {
    ExtendedRes findDriverById(Long id);
    ExtendedRes findAllDrivers();
    ExtendedRes findAllDeletedDrivers();
    ExtendedRes getUnallocatedDrivers();
    ExtendedRes updateById(DriverUpdateDto driver);
    MinimalRes deleteById(Long id);
    MinimalRes assignDriverToDepot(DriverAssignDto driverAssignDto);

}

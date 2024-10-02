package com.example.Admin.service.Components.Main;

import com.example.Admin.dto.DepotDto;
import com.example.Admin.dto.ExtendedRes;
import com.example.Admin.dto.MinimalRes;

public interface DepotService {
    MinimalRes createDepot(DepotDto depot);
    ExtendedRes findDepotById(Long id);
    ExtendedRes findAllDepots();
    ExtendedRes updateById(DepotDto depotDto);
    ExtendedRes getMyStockDetails();
    ExtendedRes getDepotStockDetails(Long depotId);
    ExtendedRes myDepotDrivers();
    ExtendedRes getDepotDrivers(Long depotId);
    MinimalRes deleteById(Long id);
    //ExtendedRes dispatchProductsToDepot(DispatchGoodsToDepot dispatch);

}

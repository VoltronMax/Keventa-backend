package com.keventa.sale.service;

import com.keventa.sale.dto.CreateSaleRequest;
import com.keventa.sale.dto.SaleResponse;
import java.util.List;

public interface SaleService {
    SaleResponse registrarVenta(CreateSaleRequest request);
    SaleResponse cancelarVenta(Long id);
    SaleResponse obtenerVentaPorId(Long id);
    List<SaleResponse> obtenerVentas();
}

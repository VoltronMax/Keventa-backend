package com.keventa.sale.service;

import com.keventa.common.exception.InsufficientStockException;
import com.keventa.common.exception.ProductNotFoundException;
import com.keventa.common.exception.SaleAlreadyCancelledException;
import com.keventa.common.exception.SaleNotFoundException;
import com.keventa.product.entity.Product;
import com.keventa.product.repository.ProductRepository;
import com.keventa.sale.dto.CreateSaleRequest;
import com.keventa.sale.dto.SaleResponse;
import com.keventa.sale.entity.Sale;
import com.keventa.sale.enums.SaleStatus;
import com.keventa.sale.mapper.SaleMapper;
import com.keventa.sale.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleServiceImp implements SaleService{

    private final SaleRepository repository;
    private final ProductRepository productRepository;
    private final SaleMapper mapper;

    public SaleServiceImp(SaleRepository repository, ProductRepository productRepository, SaleMapper mapper) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public SaleResponse registrarVenta(CreateSaleRequest request) {
        Product producto = productRepository.findById(request.productId()).
                orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        if(producto.getStock() < request.quantity()){
            throw new InsufficientStockException("Stock insuficiente para realizar la venta");
        }

        BigDecimal total = producto.getSalePrice()
                .multiply(BigDecimal.valueOf(request.quantity()));

        BigDecimal costoTotal = producto.getPurchasePrice()
                .multiply(BigDecimal.valueOf(request.quantity()));

        BigDecimal utilidad = total.subtract(costoTotal);

        producto.setStock(producto.getStock() - request.quantity());

        Sale venta = new Sale();
        venta.setProductId(producto.getId());
        venta.setProductName(producto.getName());
        venta.setPurchasePrice(producto.getPurchasePrice());
        venta.setSalePrice(producto.getSalePrice());
        venta.setQuantity(request.quantity());
        venta.setTotal(total);
        venta.setProfit(utilidad);
        venta.setSaleDate(LocalDateTime.now());
        venta.setStatus(SaleStatus.COMPLETED);

        repository.save(venta);

        return mapper.toResponse(venta);

    }

    @Transactional
    @Override
    public SaleResponse cancelarVenta(Long id) {
        Sale venta = repository.findById(id).
                orElseThrow(() -> new SaleNotFoundException("Venta no encontrada"));

        if(venta.getStatus() == SaleStatus.CANCELLED){
            throw new SaleAlreadyCancelledException("No se puede cancelar una venta ya cancelada");
        }

        Product producto = productRepository.findById(venta.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        producto.setStock(producto.getStock() + venta.getQuantity());

        venta.setStatus(SaleStatus.CANCELLED);

        repository.save(venta);

        return mapper.toResponse(venta);
    }

    @Override
    public SaleResponse obtenerVentaPorId(Long id) {
        return mapper.toResponse(repository.findById(id).
                orElseThrow(() -> new SaleNotFoundException("Venta no encontrada")));
    }

    @Override
    public List<SaleResponse> obtenerVentas() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}

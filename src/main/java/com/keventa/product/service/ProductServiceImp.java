package com.keventa.product.service;

import com.keventa.common.exception.ProductNameAlreadyRegisteredException;
import com.keventa.common.exception.ProductNotFoundException;
import com.keventa.product.dto.CreateProductRequest;
import com.keventa.product.dto.ProductResponse;
import com.keventa.product.dto.UpdateProductRequest;
import com.keventa.product.entity.Product;
import com.keventa.product.mapper.ProductMapper;
import com.keventa.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImp(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional
    @Override
    public ProductResponse registrarProducto(CreateProductRequest request) {
        if(repository.existsByNameIgnoreCase(request.name())){
            throw new ProductNameAlreadyRegisteredException("Ya existe un producto con este nombre");
        }
        Product producto = mapper.toEntity(request);
        repository.save(producto);
        return mapper.toResponse(producto);
    }

    @Override
    public ProductResponse obtenerProductoPorId(Long id) {
        return repository.findById(id).
                map(mapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
    }

    @Override
    public List<ProductResponse> obtenerTodosLosProductos() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public ProductResponse actualizarProducto(Long id,
                                              UpdateProductRequest request) {
        Product producto = repository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Producto a actualizar no encontrado"));

        if(request.name() != null
                && !request.name().equalsIgnoreCase(producto.getName())
                && repository.existsByNameIgnoreCase(request.name())){
            throw new ProductNameAlreadyRegisteredException("Ya existe un producto con ese nombre");
        }

        if(request.name() != null){
            producto.setName(request.name());
        }

        if(request.purchasePrice() != null){
            producto.setPurchasePrice(request.purchasePrice());
        }

        if(request.salePrice() != null){
            producto.setSalePrice(request.salePrice());
        }

        if(request.stock() != null){
            producto.setStock(request.stock());
        }

        return mapper.toResponse(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        repository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Producto a eliminar no encontrado"));
        repository.deleteById(id);
    }

    @Override
    public ProductResponse obtenerProductoPorNombre(String nombre) {
        return mapper.toResponse(repository.findByNameIgnoreCase(nombre)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado")));
    }

    @Override
    public List<ProductResponse> obtenerProductosPorNombre(String nombre) {
        return repository.findByNameContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


}

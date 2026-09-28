package com.keventa.product.service;

import com.keventa.product.dto.CreateProductRequest;
import com.keventa.product.dto.ProductResponse;
import com.keventa.product.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {

    ProductResponse registrarProducto(CreateProductRequest request);
    ProductResponse obtenerProductoPorId(Long id);
    List<ProductResponse> obtenerTodosLosProductos();
    ProductResponse actualizarProducto(Long id, UpdateProductRequest request);
    void eliminarProducto(Long id);
    ProductResponse obtenerProductoPorNombre(String nombre);
    List<ProductResponse> obtenerProductosPorNombre(String nombre);


}

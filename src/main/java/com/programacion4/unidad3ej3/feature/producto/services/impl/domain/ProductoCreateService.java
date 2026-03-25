package com.programacion4.unidad3ej3.feature.producto.services.impl.domain;

import com.programacion4.unidad3ej3.config.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import com.programacion4.unidad3ej3.feature.producto.services.interfaces.domain.IProductoCreateService;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej3.feature.producto.models.Producto;
import com.programacion4.unidad3ej3.feature.producto.repositories.IProductoRepository;
import com.programacion4.unidad3ej3.feature.producto.mappers.ProductoMapper;
import com.programacion4.unidad3ej3.feature.producto.services.interfaces.commons.IProductoExistByNameService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductoCreateService implements IProductoCreateService {

    private final IProductoExistByNameService productoExistByNameService;
    private final IProductoRepository productoRepository;

    @Override
    public ProductoResponseDto create(ProductoCreateRequestDto dto) {

        // 1. VALIDAR NOMBRE DUPLICADO
        if (productoExistByNameService.existByName(dto.getNombre())) {
            throw new BadRequestException("El nombre del producto ya existe");
        }

        // 2. CAPITALIZE (REQUERIDO POR EL PDF)
        dto.setNombre(capitalize(dto.getNombre()));
        dto.setDescripcion(capitalize(dto.getDescripcion()));

        // 3. MAPEAR A ENTIDAD
        Producto productoAGuardar = ProductoMapper.toEntity(dto);

        // 4. INICIALIZAR SOFT DELETE
        productoAGuardar.setEstaEliminado(false);

        // 5. GUARDAR EN BD
        Producto productoGuardado = productoRepository.save(productoAGuardar);

        // 6. DEVOLVER RESPUESTA
        return ProductoMapper.toResponseDto(productoGuardado);
    }

    // MÉTODO PARA FORMATEAR TEXTO
    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        texto = texto.toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
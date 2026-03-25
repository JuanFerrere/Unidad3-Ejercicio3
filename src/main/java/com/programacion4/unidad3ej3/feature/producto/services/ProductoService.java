package com.programacion4.unidad3ej3.feature.producto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors; // Se usa en listarTodos()

import com.programacion4.unidad3ej3.feature.producto.models.Producto;
import com.programacion4.unidad3ej3.feature.producto.repositories.IProductoRepository;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.response.ProductoResponseDto;
import com.programacion4.unidad3ej3.config.exceptions.ConflictException;
import com.programacion4.unidad3ej3.config.exceptions.ResourceNotFoundException;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    // US01: Crear con formato Capitalize [cite: 11, 22]
    public ProductoResponseDto crearProducto(ProductoCreateRequestDto dto) {
        if (productoRepository.existsByNombre(dto.getNombre())) {
            throw new ConflictException("El producto ya existe."); // [cite: 16]
        }
        Producto producto = new Producto();
        producto.setNombre(formatearTexto(dto.getNombre())); // [cite: 22, 23]
        producto.setDescripcion(formatearTexto(dto.getDescripcion()));
        producto.setCodigo(dto.getCodigo());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setEstaEliminado(false); // [cite: 25]
        
        return mapearAResponseDto(productoRepository.save(producto));
    }

    // US02: Listar todos [cite: 28, 33]
    public List<ProductoResponseDto> listarTodos() {
        List<Producto> productos = (List<Producto>) productoRepository.findAll();
        // Aquí es donde se usa el import de Collectors
        return productos.stream()
                .map(this::mapearAResponseDto)
                .collect(Collectors.toList());
    }

    // US03: Buscar por ID [cite: 34, 38]
    public ProductoResponseDto buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID no encontrado: " + id));
        return mapearAResponseDto(producto);
    }

    // US04: Actualización Total 
    public ProductoResponseDto actualizarTotal(Long id, ProductoCreateRequestDto dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar, ID no existe."));
        
        producto.setNombre(formatearTexto(dto.getNombre()));
        producto.setDescripcion(formatearTexto(dto.getDescripcion()));
        producto.setCodigo(dto.getCodigo());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        
        return mapearAResponseDto(productoRepository.save(producto));
    }

    // US05: Actualización Parcial (Patch) 
    public ProductoResponseDto actualizarParcial(Long id, ProductoUpdateRequestDto dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID no encontrado."));

        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
        if (dto.getNombre() != null) producto.setNombre(formatearTexto(dto.getNombre()));
        
        return mapearAResponseDto(productoRepository.save(producto));
    }

    // --- Métodos Auxiliares ---
    private String formatearTexto(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase(); // [cite: 22, 23]
    }

    private ProductoResponseDto mapearAResponseDto(Producto p) {
        return new ProductoResponseDto(p.getId(), p.getNombre(), p.getCodigo(), p.getDescripcion(), p.getPrecio(), p.getStock());
    }
    // --- US06: Baja de productos (Soft Delete) ---
    public void eliminarProducto(Long id) {
    // 1. Buscamos el producto. Si no existe, lanzamos error 404 
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar: No existe el producto con ID " + id));

    // 2. Aplicamos el borrado lógico 
        producto.setEstaEliminado(true);

    // 3. Guardamos el cambio
        productoRepository.save(producto);
    }

}
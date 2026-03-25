package com.programacion4.unidad3ej3.feature.producto.controllers.post;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programacion4.unidad3ej3.feature.producto.services.ProductoService;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoCreateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.request.ProductoUpdateRequestDto;
import com.programacion4.unidad3ej3.feature.producto.dtos.response.ProductoResponseDto;

import java.util.List; // <--- Esta es la línea que faltaba

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // US01: Registro de nuevos productos
    @PostMapping
    public ResponseEntity<ProductoResponseDto> crearProducto(@Valid @RequestBody ProductoCreateRequestDto request) {
        ProductoResponseDto response = productoService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // US02: Consulta de listado completo
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> obtenerTodos() {
        List<ProductoResponseDto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    // US03: Búsqueda de producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerPorId(@PathVariable Long id) {
        ProductoResponseDto response = productoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
    // US04: Actualización total de información [cite: 44, 52]
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizarTotal(@PathVariable Long id, @Valid @RequestBody ProductoCreateRequestDto request) {
        return ResponseEntity.ok(productoService.actualizarTotal(id, request)); // [cite: 54]
    }

    // US05: Actualización parcial (Patch) [cite: 55, 60]
    @PatchMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizarParcial(@PathVariable Long id, @RequestBody ProductoUpdateRequestDto request) {
        return ResponseEntity.ok(productoService.actualizarParcial(id, request)); // [cite: 63]
    }
    // US06: Baja de productos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
    productoService.eliminarProducto(id);
    // Retorna status 204 No Content 
        return ResponseEntity.noContent().build();
}
}
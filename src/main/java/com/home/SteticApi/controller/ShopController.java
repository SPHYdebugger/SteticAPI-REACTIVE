package com.home.SteticApi.controller;

import com.home.SteticApi.domain.Shop;
import com.home.SteticApi.domain.ErrorResponse;
import com.home.SteticApi.exception.ShopException.ShopNotFoundException;
import com.home.SteticApi.service.ShopService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    // Obtener todas las tiendas
    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> findAll(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String solarium
    ) throws ShopNotFoundException {
        if (!name.isEmpty() && city.isEmpty()) {
            List<Shop> shops = shopService.findShopByName(name);
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } else if (name.isEmpty() && !city.isEmpty()) {
            List<Shop> shops = shopService.findShopsByCity(city);
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } else if (name.isEmpty() && city.isEmpty() && solarium.equals("true")) {
            List<Shop> solariumShops = shopService.findShopsBySolarium(true);
            return new ResponseEntity<>(solariumShops, HttpStatus.OK);
        } else if (name.isEmpty() && city.isEmpty() && solarium.equals("false")) {
            List<Shop> solariumShops = shopService.findShopsBySolarium(false);
            return new ResponseEntity<>(solariumShops, HttpStatus.OK);
        }
        List<Shop> allShops = shopService.findAll();
        return new ResponseEntity<>(allShops, HttpStatus.OK);
    }


    // Obtener los datos de una tienda por ID
    @GetMapping("/shops/{shopId}")
    public ResponseEntity<Shop> findShopById(@PathVariable long shopId) throws ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            return new ResponseEntity<>(shop, HttpStatus.OK);
        } else {
            throw new ShopNotFoundException(shopId);
        }
    }

    // Añadir una nueva tienda
    @PostMapping("/shops")
    public ResponseEntity<Shop> addShop(@Valid @RequestBody Shop shop) {
        shopService.saveShop(shop);
        return new ResponseEntity<>(shop, HttpStatus.CREATED);
    }

    // Modificar una tienda por ID
    @PutMapping("/shops/{shopId}")
    public ResponseEntity<Void> modifyShop(@Valid @RequestBody Shop shop, @PathVariable long shopId) throws ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);

        if (optionalShop.isPresent()) {
            shopService.modifyShop(shop, shopId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ShopNotFoundException(shopId);
        }
    }

    // Eliminar una tienda por ID
    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<Void> removeShop(@PathVariable long shopId) throws ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);

        if (optionalShop.isPresent()) {
            shopService.removeShop(shopId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ShopNotFoundException(shopId);
        }
    }

    // Control de excepciones
    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<ErrorResponse> shopNotFoundException(ShopNotFoundException snfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, snfe.getMessage());
        logger.error(snfe.getMessage(), snfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

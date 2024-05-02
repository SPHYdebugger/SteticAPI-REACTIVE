package com.home.SteticApi.controller;

import com.home.SteticApi.domain.Client;
import com.home.SteticApi.domain.ErrorResponse;
import com.home.SteticApi.domain.Order;
import com.home.SteticApi.dto.OrderInDto;
import com.home.SteticApi.dto.OrderOutDto;
import com.home.SteticApi.exception.ClientException.ClientNotFoundException;
import com.home.SteticApi.exception.OrderException.OrderNotFoundException;
import com.home.SteticApi.exception.ProductException.ProductNotFoundException;
import com.home.SteticApi.service.ClientService;
import com.home.SteticApi.service.OrderService;
import com.home.SteticApi.service.ProductService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    // Obtener todas las compras o buscar por número de compra, fecha o si es compra online
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> findOrders(
            @RequestParam(defaultValue = "") String number,
            @RequestParam(defaultValue = "") String creationDate,
            @RequestParam(defaultValue = "") Boolean onlineOrder
    ) throws OrderNotFoundException {
        if (!number.isEmpty()) {
            Optional<Order> optionalOrder = orderService.findOrderByNumber(number);
            Order order = optionalOrder.orElseThrow(() -> new OrderNotFoundException(number));
            return new ResponseEntity<>(Collections.singletonList(order), HttpStatus.OK);
        } else if (!creationDate.isEmpty()) {
            List<Order> orders = orderService.findOrdersByCreationDate(creationDate);
            if (orders.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            }            
        } else if (onlineOrder != null) {
            List<Order> orders = orderService.findOrdersByOnLineOrder(onlineOrder);
            if (orders.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            }            
        }

        List<Order> allOrders = orderService.findAll();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }




    // Obtener los datos de una compra
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable long orderId) throws OrderNotFoundException {
        Optional<Order> optionalOrder = orderService.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            throw new OrderNotFoundException(orderId);
        }
    }



    // Obtener todas las compras de un cliente
    @GetMapping("/client/{clientId}/orders")
    public ResponseEntity<List<Order>> findClientOrders(@PathVariable long clientId) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findById(clientId);

        if (optionalClient.isPresent()) {
            List<Order> orders = orderService.findByClient(clientId);

            if (!orders.isEmpty()) {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            
            throw new ClientNotFoundException(clientId);
        }
    }




    // Añadir una compra a un cliente
    @PostMapping("/client/{clientId}/orders")
    public ResponseEntity<Order> addOrder(@Valid @RequestBody OrderInDto orderInDto, @PathVariable long clientId)
            throws ClientNotFoundException, ProductNotFoundException {
        Optional<Client> optionalClient = clientService.findById(clientId);

        if (optionalClient.isPresent()) {
            Order order = orderService.addOrder(orderInDto, clientId);


            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } else {
            
            throw new ClientNotFoundException(clientId);
        }
    }


    // Modificar una compra
    @PutMapping("/order/{orderId}")
    public ResponseEntity<Void> modifyOrder(@Valid @RequestBody Order order, @PathVariable long orderId)
            throws OrderNotFoundException {
        Optional<Order> optionalOrder = orderService.findById(orderId);

        if (optionalOrder.isPresent()) {
            order.setCreationDate(LocalDate.now().toString());
            orderService.modifyOrder(order, orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            
            throw new OrderNotFoundException(orderId);
        }
    }


    // Eliminar una compra por ID
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> removeOrder(@PathVariable long orderId)
            throws OrderNotFoundException {
        Optional<Order> optionalOrder = orderService.findById(orderId);

        if (optionalOrder.isPresent()) {
            orderService.removeOrder(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            
            throw new OrderNotFoundException(orderId);
        }
    }


    // Control de excepciones
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> clientNotFoundException(ClientNotFoundException unfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, unfe.getMessage());
        logger.error(unfe.getMessage(), unfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(ProductNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
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

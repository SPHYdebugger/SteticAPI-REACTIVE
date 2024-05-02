package com.home.SteticApi.controller;

import com.home.SteticApi.domain.Client;
import com.home.SteticApi.domain.ErrorResponse;
import com.home.SteticApi.exception.ClientException.ClientNotFoundException;
import com.home.SteticApi.service.ClientService;
import jakarta.validation.Valid;
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
public class ClientController {

    @Autowired
    private ClientService clientService;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    // Obtener todos los clientes o filtrar uno por el nombre, dni o ciudad
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> findAll(
            @RequestParam(defaultValue = "") String firstname,
            @RequestParam(defaultValue = "") String dni,
            @RequestParam(defaultValue = "") String city
    ) throws ClientNotFoundException{
        if (!firstname.isEmpty()) {
            List<Client> clients = clientService.findClientByFirstname(firstname);
            if (clients.isEmpty()){
                return new ResponseEntity<>(clients, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(clients, HttpStatus.OK);
            }
        } else if (!dni.isEmpty()) {
            Optional<Client> optionalClient = clientService.findClientByDni(dni);
            Client client = optionalClient.orElseThrow(() -> new ClientNotFoundException(dni));
            return new ResponseEntity<>(Collections.singletonList(client), HttpStatus.OK);
        } else if (!city.isEmpty()) {
            List<Client> clients = clientService.findClientByCity(city);
            if (clients.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(clients, HttpStatus.OK);
            }
        }

        List<Client> allClients = clientService.findAll();
        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }

    // Añadir un nuevo cliente
    @PostMapping("/clients")
    public ResponseEntity<Client> saveClient(@Valid @RequestBody Client client) {
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    // Obtener un cliente por ID
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<?> findById(@PathVariable long clientId) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findById(clientId);
        Client client = optionalClient.orElseThrow(() -> new ClientNotFoundException(clientId));
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // Obtener un cliente por el DNI
    @GetMapping("/client/{dni}")
    public ResponseEntity<Client> findClientByDni(@PathVariable String dni) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findClientByDni(dni);
        Client client = optionalClient.orElseThrow(() -> new ClientNotFoundException("El cliente no existe"));
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // Borrar un cliente por DNI
    @DeleteMapping("/client/{dni}")
    public ResponseEntity<Void> removeClient(@PathVariable String dni) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findClientByDni(dni);
        if (optionalClient.isPresent()) {
            clientService.removeClient(dni);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ClientNotFoundException(dni);
        }
    }

    //borrar un cliente por ID
    @DeleteMapping("/clients/{clientId}")
    public ResponseEntity<Void> removeClientById(@PathVariable long clientId) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findById(clientId);
        if (optionalClient.isPresent()) {
            clientService.removeClientById(clientId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ClientNotFoundException(clientId);
        }
    }

    // Modificar un cliente por DNI
    @PutMapping("/client/{dni}")
    public ResponseEntity<Client> modifyClient(@Valid @RequestBody Client client, @PathVariable String dni) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findClientByDni(dni);
        if (optionalClient.isPresent()) {
            clientService.modifyClient(client, dni);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ClientNotFoundException(dni);
        }
    }

    // modificar por Id

    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> modifyClientById(@Valid @RequestBody Client client, @PathVariable long id) throws ClientNotFoundException {
        Optional<Client> optionalClient = clientService.findById(id);
        if (optionalClient.isPresent()) {
            clientService.modifyClientById(client, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
      throw new ClientNotFoundException(client.getDni());
        }
    }

    // Control de excepciones
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> clientNotFoundException(ClientNotFoundException pnfe) {
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



// esto es una prueba



}

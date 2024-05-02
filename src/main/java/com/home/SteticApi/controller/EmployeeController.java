package com.home.SteticApi.controller;

import com.home.SteticApi.domain.Employee;
import com.home.SteticApi.domain.ErrorResponse;
import com.home.SteticApi.domain.Shop;
import com.home.SteticApi.exception.EmployeeException.EmployeeNotFoundException;
import com.home.SteticApi.exception.ShopException.ShopNotFoundException;
import com.home.SteticApi.service.EmployeeService;
import com.home.SteticApi.service.ShopService;
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
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ShopService shopService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> findAll(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String dni,
            @RequestParam(defaultValue = "") Boolean academicTitle
    ) throws EmployeeNotFoundException {
        if (!name.isEmpty()) {
            List<Employee> employees = employeeService.findEmployeeByName(name);
            if (employees.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(employees, HttpStatus.OK);
            }            
        } else if (!dni.isEmpty()) {
            Optional<Employee> optionalEmployee = employeeService.findEmployeeByDni(dni);
            Employee employee = optionalEmployee.orElseThrow(() -> new EmployeeNotFoundException(dni));
            return new ResponseEntity<>(Collections.singletonList(employee), HttpStatus.OK);
        } else if (academicTitle != null) {
            List<Employee> employees = employeeService.findEmployeeByAcademicTitle(academicTitle);
            if (employees.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(employees, HttpStatus.OK);
            }            
        }

        List<Employee> allEmployees = employeeService.findAll();
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }


    // Obtener un empleado por ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> findById(@PathVariable long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);
        Employee employee = optionalEmployee.orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // Añadir un nuevo empleado a una tienda
    @PostMapping("/employees/{shopId}")
    public ResponseEntity<Employee> saveEmployeeByShop(@Valid @RequestBody Employee employee, @PathVariable long shopId)
            throws ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            employee.setShop(shop);
            employeeService.saveEmployee(employee);
            shop.getEmployees().add(employee);
            shopService.saveShop(shop);

            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        } else {

            throw new ShopNotFoundException(shopId);
        }
    }

    // Añadir un nuevo empleado
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee)
             {
            employeeService.saveEmployee(employee);
            return new ResponseEntity<>(employee, HttpStatus.CREATED);

    }

    // Añadir un empleado a una tienda por ID
    @PostMapping("/employees/{shopId}/add/{employeeId}")
    public ResponseEntity<Employee> saveEmployeeById(@PathVariable long employeeId, @PathVariable long shopId)
            throws EmployeeNotFoundException, ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
                employee.setShop(shop);
                employeeService.saveEmployeeByShop(employee, shopId);
                shop.getEmployees().add(employee);
                shopService.saveShop(shop);
            } else {
                throw new EmployeeNotFoundException(employeeId);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {

            throw new ShopNotFoundException(shopId);
        }
    }

    // Borrar un empleado por ID
    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<Void> removeEmployee(@PathVariable long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            employeeService.removeEmployee(employeeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    // Eliminar un empleado de una tienda por ID
    @DeleteMapping("/employees/{shopId}/remove/{employeeId}")
    public ResponseEntity<Void> removeEmployeeFromShop(@PathVariable long employeeId, @PathVariable long shopId)
            throws EmployeeNotFoundException, ShopNotFoundException {
        Optional<Shop> optionalShop = shopService.findById(shopId);
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

        if (optionalShop.isPresent() && optionalEmployee.isPresent()) {
            Shop shop = optionalShop.get();
            Employee employee = optionalEmployee.get();

            if (employee.getShop() != null && employee.getShop().getId() == shopId) {

                shop.getEmployees().remove(employee);
                employee.setShop(null);
                shopService.saveShop(shop);
                employeeService.removeEmployee(employeeId);

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

                throw new EmployeeNotFoundException(employeeId);
            }
        } else {

            throw new ShopNotFoundException(shopId);
        }
    }


    // Modificar un empleado por ID
    @PutMapping("/employee/{employeeId}")
    public ResponseEntity<Employee> modifyEmployee(@RequestBody Employee employee, @PathVariable long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optionalEmployee = employeeService.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            employeeService.modifyEmployee(employee, employeeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    
    // Control de excepciones
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> employeeNotFoundException(EmployeeNotFoundException pnfe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        logger.error(pnfe.getMessage(), pnfe);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<ErrorResponse> shopNotFoundException(ShopNotFoundException pnfe) {
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

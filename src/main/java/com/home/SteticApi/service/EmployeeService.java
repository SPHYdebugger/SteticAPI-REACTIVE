package com.home.SteticApi.service;

import com.home.SteticApi.domain.Employee;
import com.home.SteticApi.domain.Shop;
import com.home.SteticApi.repository.EmployeeRepository;
import com.home.SteticApi.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ShopRepository shopRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> findEmployeeByName(String name) {
        return employeeRepository.findByName(name);
    }
    public Optional<Employee> findEmployeeByDni(String dni) { return employeeRepository.findByDni(dni);}
    public List<Employee> findEmployeeByAcademicTitle(boolean academicTitle) { return employeeRepository.findEmployeeByAcademicTitle(academicTitle);}

    public void saveEmployeeByShop(Employee employee, long shopId) {

        shopRepository.findById(shopId).ifPresent(employee::setShop);
        employee.setRegisterDate(LocalDate.now().toString());
        employeeRepository.save(employee);
    }

    public void saveEmployee(Employee employee) {

        employee.setRegisterDate(LocalDate.now().toString());
        employeeRepository.save(employee);
    }

    @Transactional
    public void removeEmployee(long id) {
        employeeRepository.deleteById(id);
    }

    public void modifyEmployee(Employee newEmployee, long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setName(newEmployee.getName());
            existingEmployee.setDni(newEmployee.getDni());
            existingEmployee.setAge(newEmployee.getAge());
            existingEmployee.setHeight(newEmployee.getHeight());
            existingEmployee.setAcademicTitle(newEmployee.isAcademicTitle());
            existingEmployee.setRegisterDate(newEmployee.getRegisterDate());
            existingEmployee.setShop(newEmployee.getShop());
            employeeRepository.save(existingEmployee);
        }
    }
}

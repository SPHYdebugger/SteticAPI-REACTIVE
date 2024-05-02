package com.home.SteticApi.repository;

import com.home.SteticApi.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findAll();
    List<Employee> findByName(String name);

    Optional<Employee> findByDni(String dni);
    List<Employee> findEmployeeByAcademicTitle(boolean academicTitle);

    void deleteByDni(String dni);
}

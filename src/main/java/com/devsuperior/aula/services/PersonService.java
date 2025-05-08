package com.devsuperior.aula.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.aula.dto.PersonDTO;
import com.devsuperior.aula.dto.PersonDepartmentDTO;
import com.devsuperior.aula.entities.Department;
import com.devsuperior.aula.entities.Person;
import com.devsuperior.aula.repositories.DepartmentRepository;
import com.devsuperior.aula.repositories.PersonRepository;

@Service
public class PersonService {

  @Autowired
  PersonRepository personRepository;

  @Autowired
  DepartmentRepository departmentRepository;

  @Transactional
  public PersonDepartmentDTO insert(PersonDepartmentDTO dto) {

    Person entity = new Person();
    entity.setName(dto.getName());
    entity.setSalary(dto.getSalary());

    // Retorna um proxy gerenciado pelo JPA para o departamento com o ID fornecido.
    // Quando esse objeto for serializado (ex: para JSON), o JPA pode carregar automaticamente os dados do banco,
    // resultando em um JSON com todos os atributos preenchidos (eager loading pode ocorrer dependendo da configuração).
    Department dept = departmentRepository.getReferenceById(dto.getDepartment().getId());

    // Cria manualmente um objeto transiente (não gerenciado pelo JPA), apenas com o ID.
    // Ao serializar, apenas o ID será exibido no JSON, já que os outros campos não foram carregados nem atribuídos.
    // Department dept = new Department();
    // dept.setId(dto.getDepartment().getId());

    entity.setDepartment(dept);

    entity = personRepository.save(entity);

    return new PersonDepartmentDTO(entity);
  }

  @Transactional
  public PersonDTO insert(PersonDTO dto) {

    Person entity = new Person();
    entity.setName(dto.getName());
    entity.setSalary(dto.getSalary());

    // Retorna um proxy gerenciado pelo JPA para o departamento com o ID fornecido.
    // Quando esse objeto for serializado (ex: para JSON), o JPA pode carregar automaticamente os dados do banco,
    // resultando em um JSON com todos os atributos preenchidos (eager loading pode ocorrer dependendo da configuração).
    Department dept = departmentRepository.getReferenceById(dto.getDepartmentId());

    // Cria manualmente um objeto transiente (não gerenciado pelo JPA), apenas com o ID.
    // Ao serializar, apenas o ID será exibido no JSON, já que os outros campos não foram carregados nem atribuídos.
    // Department dept = new Department();
    // dept.setId(dto.getDepartmentId());

    entity.setDepartment(dept);

    entity = personRepository.save(entity);

    return new PersonDTO(entity);
  }
}

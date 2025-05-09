package com.devsuperior.aula.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.aula.dto.PersonDTO;
import com.devsuperior.aula.dto.PersonDepartmentDTO;
import com.devsuperior.aula.services.PersonService;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(value = "/people")
public class PersonController {

  @Autowired
  private PersonService personService;

  @PostMapping
  public ResponseEntity<PersonDepartmentDTO> insert(@RequestBody PersonDepartmentDTO dto) {
    dto = personService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(dto.getId())
        .toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  // @PostMapping
  public ResponseEntity<PersonDTO> insert(@RequestBody PersonDTO dto) {
    dto = personService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(dto.getId())
        .toUri();
    return ResponseEntity.created(uri).body(dto);
  }
}

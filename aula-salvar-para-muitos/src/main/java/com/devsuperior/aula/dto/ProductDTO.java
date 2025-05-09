package com.devsuperior.aula.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.devsuperior.aula.entities.Product;

public class ProductDTO {

  private Long id;
  private String name;
  private Double price;

  private List<CategoryDTO> categories = new ArrayList<>();

  public ProductDTO() {
  }

  public ProductDTO(Long id, String name, Double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductDTO(Product entity) {
    id = entity.getId();
    name = entity.getName();
    price = entity.getPrice();

    // Ambas as abordagens produzem o mesmo resultado. No entanto, a versão com stream (abaixo) é mais moderna,
    // concisa e alinhada ao estilo funcional do Java. Além disso, ela evita a necessidade de uma lista externa,
    // tornando o código mais limpo e seguro. Em termos de desempenho, as diferenças são mínimas.

    // for (Category category : entity.getCategories()) {
    //   categories.add(new CategoryDTO(category));
    // }

    // entity.getCategories().forEach(category -> categories.add(new CategoryDTO(category)));

    categories = entity.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toList());

  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  public List<CategoryDTO> getCategories() {
    return categories;
  }

}

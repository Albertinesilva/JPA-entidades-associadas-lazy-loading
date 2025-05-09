package com.devsuperior.aula.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.aula.dto.CategoryDTO;
import com.devsuperior.aula.dto.ProductDTO;
import com.devsuperior.aula.entities.Category;
import com.devsuperior.aula.entities.Product;
import com.devsuperior.aula.repositories.CategoryRepository;
import com.devsuperior.aula.repositories.ProductRepository;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  /**
   * Insere um novo produto no banco de dados, associando categorias existentes
   * por ID.
   * <p>
   * O método itera sobre as categorias fornecidas no DTO e as associa ao produto.
   * Dependendo da abordagem escolhida para buscar as categorias, o comportamento
   * pode variar:
   * <ul>
   * <li><strong>Uso de `getReferenceById`: </strong>Obtém uma referência
   * gerenciada pelo JPA usando o ID da categoria.</li>
   * <li><strong>Criação manual de objetos `Category`: </strong>Cria um objeto
   * `Category` apenas com o ID preenchido.</li>
   * </ul>
   * 
   * A escolha entre essas abordagens tem implicações no comportamento de
   * carregamento dos dados das categorias:
   * <ul>
   * <li><strong>Proxies JPA (`getReferenceById`):</strong> Não executa
   * imediatamente uma consulta ao banco, retornando um proxy da entidade que será
   * carregado
   * apenas quando necessário (lazy loading). Ideal quando se precisa de
   * eficiência e não importa carregar todos os dados de uma vez.</li>
   * <li><strong>Instâncias manuais (`new Category().setId()`):</strong> Não
   * utiliza JPA e não cria proxies. A categoria será tratada como uma entidade
   * transiente, com
   * apenas o ID atribuído. Isso evita o carregamento de dados desnecessários e
   * pode ser útil quando não se precisa dos detalhes completos da categoria.</li>
   * </ul>
   * 
   * @param dto Objeto de transferência contendo os dados do produto e as categorias associadas.
   * @return Um DTO representando o produto salvo com suas categorias associadas.
   */
  public ProductDTO insert(ProductDTO dto) {
    // Cria uma nova instância de Product com os dados básicos
    Product entity = new Product();
    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());

    // Itera sobre as categorias do DTO
    for (CategoryDTO catDto : dto.getCategories()) {

      // 🔹 Opção 1: Obtém uma referência gerenciada pelo JPA usando o ID (proxy)
      // Isso não executa imediatamente uma consulta no banco, mas retorna um proxy que será carregado apenas se necessário.
      // O proxy garante que, caso o dado da categoria seja acessado posteriormente, ele seja carregado do banco automaticamente.
      Category category = categoryRepository.getReferenceById(catDto.getId());

      // 🔸 Opção 2 (comentada): Cria uma instância manual com o ID (não gerenciada)
      // Isso é útil se você quiser evitar o uso de proxies ou lazy loading, e está certo de que os IDs são válidos.
      // Essa abordagem não carrega os dados da categoria, apenas cria uma instância com o ID fornecido.
      // Category category = new Category();
      // category.setId(catDto.getId());

      // Adiciona a categoria ao produto
      entity.getCategories().add(category);
    }

    // Salva a entidade no banco de dados (gera ID, timestamps, etc.)
    entity = productRepository.save(entity);

    // Retorna o DTO representando o produto salvo
    return new ProductDTO(entity);
  }

  /**
   * Persiste um novo produto utilizando referências gerenciadas do banco de dados
   * para associar as categorias ao produto.
   * Essa abordagem é útil quando se deseja garantir que as categorias associadas
   * já existem no banco de dados e estão sendo gerenciadas pelo JPA.
   *
   * @param dto Objeto de transferência contendo os dados do produto e IDs de categorias.
   * @return Um DTO representando o produto salvo com suas categorias associadas.
   */
  public ProductDTO save(ProductDTO dto) {
    Product entity = new Product();
    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());

    dto.getCategories().forEach(catDto -> {
      Category category = categoryRepository.getReferenceById(catDto.getId());
      entity.getCategories().add(category);
    });

    productRepository.save(entity);

    return new ProductDTO(entity);
  }

  /**
   * Registra um novo produto associando categorias por instância com ID preenchido,
   * sem buscar as referências diretamente do banco de dados.
   * Essa abordagem pode ser útil para melhorar a performance quando se tem certeza
   * de que os IDs informados são válidos e não há necessidade de carregar entidades completas.
   * 
   * @param dto Objeto de transferência contendo os dados do produto e IDs de categorias.
   * @return Um DTO representando o produto registrado.
   */
  public ProductDTO register(ProductDTO dto) {
    Product entity = new Product();
    entity.setName(dto.getName());
    entity.setPrice(dto.getPrice());

    entity.getCategories().addAll(
        dto.getCategories().stream()
            .map(catDto -> {
              Category category = new Category();
              category.setId(catDto.getId());
              return category;
            })
            .collect(Collectors.toList()));

    entity = productRepository.save(entity);

    return new ProductDTO(entity);
  }

}

package br.edu.fatec2020.topicos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import br.edu.fatec2020.topicos.domain.Produto;
//será servido como um serviço REST
@RepositoryRestResource(collectionResourceRel = "produtos", path = "produtos")
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
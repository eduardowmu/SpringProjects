package com.edu.demoajax.web.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.edu.demoajax.web.domain.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long>
{	//como iremos querer a data mais recente, podemos usar a data de maior valor (max)
	@Query("SELECT count(p.id) AS count, max(p.cadastro) AS lastDate FROM Promocao p WHERE "
			+ "p.cadastro >:data")//queremos a qtde de promoções e a data mais recente que temos entre as promoções que foram cadastradas após a data que estamos passando como parâmetro.
	Map<String, Object> totalAndUltimaPromocaoByDataCadastro(@Param("data") LocalDateTime data);
	
	@Query("SELECT p.cadastro FROM Promocao p")
	Page<LocalDateTime> findUltimaDataDePromocao(Pageable pageable);
	 
	//consulta especifica para localizar as promoções a partir do preço. Consulta via JPQL
	@Query("SELECT p FROM Promocao p WHERE p.preco = :preco")
	Page<Promocao> findByPrice(@Param("preco") BigDecimal preco, Pageable pageable);
	
	//devemos buscar todos os campos que devemos trabalhar com o campo de input
	@Query("SELECT p FROM Promocao p WHERE p.titulo like %:search% OR p.site like %:search% OR "
			+ "p.categoria.titulo like %:search%")
	Page<Promocao> findByTitleOrSiteOrCategory(@Param("search") String search, Pageable pageable);
	
	//Retorno do tipo Page, que é do SpringData para quando vc trabalha com paginação de dados.
	@Query("SELECT p FROM Promocao p WHERE p.site LIKE :site")
	Page<Promocao> findBySite(@Param("site") String site, Pageable pageable);
	
	//inclui uma consulta que localize os sites conforme os caracteres que estamos digitando 
	//no campo de input. O comando DISTINCT serve para restringir a apenas uma ocorrência
	@Query("SELECT DISTINCT p.site FROM Promocao p WHERE p.site LIKE %:site%")
	List<String> findSitesByTermo(@Param("site") String site);
	
	//método update, para que some mais 1 like de um determinado produto, quando
	//algum usuário clicar no respectivo like
	/*Como @Query foi feito para fazer consultas e o que estamos fazendo é uma operação
	 de escrita, devemos dizer isso para o JPA através de outras notações.
	 O parametro readOnly, por padrão é TRUE*/
	@Transactional(readOnly = false)
	@Modifying
	@Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
	void updateSomarLikes(@Param("id") Long id);
	
	//variável onde será armazenado o id do like.
	//esta notação receberá a consulta JPQL. O ":id", representa o parâmetro da
	//notação "Param" do mátodo findLikesById
	@Query("SELECT p.likes FROM Promocao p WHERE p.id = :id")
	int findLikesById(@Param("id") Long id);
}
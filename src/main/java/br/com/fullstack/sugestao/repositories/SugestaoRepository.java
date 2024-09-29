package br.com.fullstack.sugestao.repositories;


import br.com.fullstack.sugestao.entities.Sugestao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {

    // Busca sugestões por título (parcial) e ordena por data de atualização decrescente
    @Query("SELECT s FROM Sugestao s WHERE (:titulo IS NULL OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) ORDER BY s.dataAtualizacao DESC")
    List<Sugestao> findByTituloContainingIgnoreCaseOrderByDataAtualizacaoDesc(@Param("titulo") String titulo);
}


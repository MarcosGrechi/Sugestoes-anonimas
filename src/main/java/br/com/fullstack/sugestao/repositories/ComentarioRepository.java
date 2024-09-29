package br.com.fullstack.sugestao.repositories;

import br.com.fullstack.sugestao.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Busca comentários relacionados à sugestão e ordena por data de envio decrescente
    @Query("SELECT c FROM Comentario c WHERE c.sugestao.id = :sugestaoId ORDER BY c.dataEnvio DESC")
    List<Comentario> findBySugestaoIdOrderByDataEnvioDesc(@Param("sugestaoId") Long sugestaoId);
}


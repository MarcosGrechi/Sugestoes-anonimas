package br.com.fullstack.sugestao.services;


import br.com.fullstack.sugestao.entities.Comentario;
import br.com.fullstack.sugestao.entities.Sugestao;
import br.com.fullstack.sugestao.repositories.ComentarioRepository;
import br.com.fullstack.sugestao.repositories.SugestaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SugestaoService {

    private final SugestaoRepository sugestaoRepository;
    private final ComentarioRepository comentarioRepository;

    // Método para adicionar um comentário a uma sugestão e atualizar dataAtualizacao
    @Transactional
    public Comentario adicionarComentario(Long sugestaoId, Comentario comentario) {
        log.info("Adicionando comentário à sugestão com ID: {}", sugestaoId);

        Sugestao sugestao = sugestaoRepository.findById(sugestaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sugestão não encontrada com o ID: " + sugestaoId));

        // Atualizar data de atualização da sugestão
        sugestao.setDataAtualizacao(LocalDateTime.now());
        sugestaoRepository.save(sugestao); // Persistir a atualização na sugestão

        // Associar o comentário à sugestão e salvar o comentário
        comentario.setSugestao(sugestao);
        comentario.setDataEnvio(LocalDateTime.now());
        return comentarioRepository.save(comentario);
    }

    // Método para listar sugestões com ou sem filtro de título, ordenadas por data de atualização
    public List<Sugestao> listarSugestoesComFiltro(String titulo) {
        log.info("Listando sugestões com filtro de título: {}", titulo != null ? titulo : "Nenhum filtro");
        return sugestaoRepository.findByTituloContainingIgnoreCaseOrderByDataAtualizacaoDesc(titulo);
    }

    // Método para obter uma sugestão por ID e seus comentários, ordenados por data de envio
    public Optional<Sugestao> obterSugestaoPorId(Long id) {
        log.info("Obtendo sugestão com ID: {}", id);
        return sugestaoRepository.findById(id);
    }

    public List<Comentario> listarComentariosPorSugestao(Long sugestaoId) {
        log.info("Listando comentários da sugestão com ID: {}", sugestaoId);
        return comentarioRepository.findBySugestaoIdOrderByDataEnvioDesc(sugestaoId);
    }

    public Sugestao criarSugestao(Sugestao sugestao) {
        log.info("Criando uma nova sugestão: {}", sugestao.getTitulo());
        return sugestaoRepository.save(sugestao);
    }

    public void deletarSugestao(Long id) {
        log.info("Deletando sugestão com ID: {}", id);
        if (!sugestaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Sugestão não encontrada com ID: " + id);
        }
        sugestaoRepository.deleteById(id);
    }
}

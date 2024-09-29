package br.com.fullstack.sugestao.services;

import br.com.fullstack.sugestao.entities.Comentario;
import br.com.fullstack.sugestao.repositories.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public List<Comentario> listarComentariosPorSugestao(Long sugestaoId) {
        log.info("Listando todos os comentários para a sugestão ID: {}", sugestaoId);
        return comentarioRepository.findAll();
    }

    public Comentario criarComentario(Comentario comentario) {
        log.info("Criando novo comentário para a sugestão ID: {}", comentario.getSugestao().getId());
        return comentarioRepository.save(comentario);
    }
}

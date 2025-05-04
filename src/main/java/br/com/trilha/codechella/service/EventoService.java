package br.com.trilha.codechella.service;

import br.com.trilha.codechella.client.TraducaoDeTextos;
import br.com.trilha.codechella.domain.EventoDTO;
import br.com.trilha.codechella.enumeration.TipoEvento;
import br.com.trilha.codechella.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    public Flux<EventoDTO> obterTodos() {
        return eventoRepository.findAll().map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> obterID(Long id) {
        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> cadastrar(EventoDTO dto) {
        return eventoRepository.save(dto.toEntity())
                .map(EventoDTO::toDto);
    }

    public Mono<Void> deletar(Long id) {
        return eventoRepository.findById(id)
                .flatMap(eventoRepository::delete);
    }

    public Mono<EventoDTO> alterar(Long id, EventoDTO dto) {
        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id do evento não encontrado.")))
                .flatMap(eventoExistente -> {
                    eventoExistente.setTipo(dto.tipo());
                    eventoExistente.setNome(dto.nome());
                    eventoExistente.setData(dto.data());
                    eventoExistente.setDescricao(dto.descricao());
                    return eventoRepository.save(eventoExistente);
                })
                .map(EventoDTO::toDto);
    }

    public Flux<EventoDTO> obeterPorTipo(String tipo) {
        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
        return eventoRepository.findByTipo(tipoEvento)
                .map(EventoDTO::toDto);
    }

    public Mono<String> obterTraducao(Long id, String idioma) {
        return eventoRepository.findById(id)
                .flatMap(evento -> TraducaoDeTextos.obterTraducao(evento.getDescricao(), idioma));
    }
}

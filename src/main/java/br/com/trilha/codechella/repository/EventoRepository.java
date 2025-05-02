package br.com.trilha.codechella.repository;

import br.com.trilha.codechella.Evento;
import br.com.trilha.codechella.enumeration.TipoEvento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {
    Flux<Evento> findByTipo(TipoEvento tipoEvento);
}

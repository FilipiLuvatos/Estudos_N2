package br.com.trilha.codechella.controller;

import br.com.trilha.codechella.Evento;
import br.com.trilha.codechella.domain.EventoDTO;
import br.com.trilha.codechella.repository.EventoRepository;
import br.com.trilha.codechella.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.awt.*;
import java.time.Duration;

@RestController
@RequestMapping("/eventos")
public class EventoController {


    private final EventoService eventoService;

    private final Sinks.Many<EventoDTO> eventoSink;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping()
    public Flux<EventoDTO> obterTodos(){
        return eventoService.obterTodos();
    }

    @GetMapping("/{id}")
    public Mono<EventoDTO> obterID(@PathVariable Long id){
        return eventoService.obterID(id);
    }

    @PostMapping
    public Mono<EventoDTO> cadastrar(@RequestBody EventoDTO dto){

        return eventoService.cadastrar(dto).doOnSuccess(e -> eventoSink.tryEmitNext(e));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> excluir(@PathVariable Long id){
        return eventoService.deletar(id);

    }

    @PutMapping("/{id}")
    public Mono<EventoDTO> alterar(@PathVariable Long id, @RequestBody EventoDTO dto){
        return eventoService.alterar(id, dto);
    }

    @GetMapping(value = "/cat/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO>obterPortipo(@PathVariable String tipo){
        return Flux.merge(eventoService.obeterPorTipo(tipo), eventoSink.asFlux())
                .delayElements(Duration.ofSeconds(4));
    }

}

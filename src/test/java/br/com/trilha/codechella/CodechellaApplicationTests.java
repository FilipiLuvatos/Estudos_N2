package br.com.trilha.codechella;

import br.com.trilha.codechella.domain.EventoDTO;
import br.com.trilha.codechella.enumeration.TipoEvento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodechellaApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void cadastraNovoEnvento() {
		EventoDTO dto = new EventoDTO(null, TipoEvento.SHOW, "Elvis",
				LocalDate.parse("2025-01-01"), "Show do Rei");

		webTestClient.post().uri("/eventos").bodyValue(dto)
				.exchange() // Envia a requisição
				.expectStatus()
				.isCreated()
				.expectBody(EventoDTO.class)
				.value(response ->{ //Resposta
					assertNotNull(response.id()); //Verificando se o ID não voltou nulo
					assertEquals(dto.tipo(), response.tipo()); // Verifica se o tipo bate com o tipo passado
					assertEquals(dto.nome(), response.nome());
					assertEquals(dto.data(), response.data());
					assertEquals(dto.descricao(), response.descricao());
				});


	}

	@Test
	void buscarEvento() {
		EventoDTO dto = new EventoDTO(10L, TipoEvento.CONCERTO, "The Weeknd",
				LocalDate.parse("2025-11-02"), "Um show eletrizante ao ar livre com muitos efeitos especiais.");

		webTestClient.get().uri("/eventos")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(EventoDTO.class)
				.value(response -> {
					EventoDTO eventoResponse = response.get(9);// Nesse caso precisamos passar a posição
					assertEquals(dto.id(), eventoResponse.id());
					assertEquals(dto.tipo(), eventoResponse.tipo());
					assertEquals(dto.nome(), eventoResponse.nome());
					assertEquals(dto.data(), eventoResponse.data());
					assertEquals(dto.descricao(), eventoResponse.descricao());
				});
	}

}

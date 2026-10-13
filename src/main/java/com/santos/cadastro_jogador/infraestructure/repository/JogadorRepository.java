
package com.santos.cadastro_jogador.infraestructure.repository;

import com.santos.cadastro_jogador.infraestructure.entitys.Jogador;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador, Integer> {

    Optional<Jogador> findByNumeroCamisa(Integer numeroCamisa);

    void deleteByNumeroCamisa(Integer numeroCamisa);

    List<Jogador> findAllByOrderByNomeAsc();

    List<Jogador> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Jogador> findByPosicaoOrderByNomeAsc(Posicao posicao);

    List<Jogador> findByNomeContainingIgnoreCaseAndPosicaoOrderByNomeAsc(
            String nome, Posicao posicao);

    boolean existsByNumeroCamisa(Integer numeroCamisa);

    boolean existsByNumeroCamisaAndIdNot(Integer numeroCamisa, Integer id);
}

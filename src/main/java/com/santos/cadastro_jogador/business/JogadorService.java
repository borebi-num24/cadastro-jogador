
package com.santos.cadastro_jogador.business;

import com.santos.cadastro_jogador.business.converter.JogadorConverter;
import com.santos.cadastro_jogador.business.dto.JogadorPatchRequest;
import com.santos.cadastro_jogador.business.dto.JogadorRequest;
import com.santos.cadastro_jogador.business.dto.JogadorResponse;
import com.santos.cadastro_jogador.infraestructure.entitys.Jogador;
import com.santos.cadastro_jogador.infraestructure.entitys.Posicao;
import com.santos.cadastro_jogador.infraestructure.exceptions.RecursoNaoEncontradoException;
import com.santos.cadastro_jogador.infraestructure.exceptions.RegraDeNegocioException;
import com.santos.cadastro_jogador.infraestructure.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogadorService {

    private final JogadorRepository repository;
    private final JogadorConverter converter;

    // CREATE
    @Transactional
    public JogadorResponse salvarJogador(JogadorRequest dados) {
        if (repository.existsByNumeroCamisa(dados.numeroCamisa())) {
            throw new RegraDeNegocioException(
                    "Já existe um jogador com a camisa " + dados.numeroCamisa()
            );
        }

        return JogadorResponse.de(
                repository.saveAndFlush(converter.paraEntidade(dados))
        );
    }

    // READ: filtros opcionais por ID, nome, posição e número da camisa
    @Transactional(readOnly = true)

    public List<JogadorResponse> listarJogadores(
            Integer id,
            String nome,
            Posicao posicao,
            Integer numeroCamisa) {

        String nomeFiltro = (nome == null || nome.isBlank())
                ? null
                : nome.trim();

        return repository.findAllByOrderByNomeAsc()
                .stream()
                .filter(j -> id == null || j.getId().equals(id))
                .filter(j -> nomeFiltro == null
                        || j.getNome().toLowerCase()
                        .contains(nomeFiltro.toLowerCase()))
                .filter(j -> posicao == null || j.getPosicao() == posicao)
                .filter(j -> numeroCamisa == null
                        || j.getNumeroCamisa().equals(numeroCamisa))
                .map(JogadorResponse::de)
                .toList();
    }


    // READ: por ID
    @Transactional(readOnly = true)
    public JogadorResponse buscarJogadorPorId(Integer id) {
        return JogadorResponse.de(buscarEntidade(id));
    }

    // READ: por número da camisa
    @Transactional(readOnly = true)
    public JogadorResponse buscarJogadorPorCamisa(Integer numeroCamisa) {
        return repository.findByNumeroCamisa(numeroCamisa)
                .map(JogadorResponse::de)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhum jogador encontrado com a camisa " + numeroCamisa
                ));
    }

    // UPDATE completo: PUT
    @Transactional
    public JogadorResponse atualizarJogador(Integer id, JogadorRequest dados) {
        Jogador jogador = buscarEntidade(id);

        if (repository.existsByNumeroCamisaAndIdNot(
                dados.numeroCamisa(), id)) {
            throw new RegraDeNegocioException(
                    "Já existe outro jogador com a camisa "
                            + dados.numeroCamisa()
            );
        }

        jogador.setNome(dados.nome().trim());
        jogador.setPosicao(dados.posicao());
        jogador.setNumeroCamisa(dados.numeroCamisa());
        jogador.setDataNascimento(dados.dataNascimento());
        jogador.setNacionalidade(dados.nacionalidade());

        return JogadorResponse.de(repository.saveAndFlush(jogador));
    }

    // UPDATE parcial: PATCH
    @Transactional
    public JogadorResponse atualizarParcialmente(
            Integer id, JogadorPatchRequest dados) {

        Jogador atual = buscarEntidade(id);

        if (dados.numeroCamisa() != null
                && repository.existsByNumeroCamisaAndIdNot(
                dados.numeroCamisa(), id)) {
            throw new RegraDeNegocioException(
                    "Já existe outro jogador com a camisa "
                            + dados.numeroCamisa()
            );
        }

        Jogador atualizado = converter.mesclar(atual, dados);

        return JogadorResponse.de(
                repository.saveAndFlush(atualizado)
        );
    }

    // DELETE: por ID
    @Transactional
    public void deletarJogador(Integer id) {
        repository.delete(buscarEntidade(id));
    }

    // DELETE: por número da camisa
    @Transactional
    public void deletarJogadorPorCamisa(Integer numeroCamisa) {
        if (!repository.existsByNumeroCamisa(numeroCamisa)) {
            throw new RecursoNaoEncontradoException(
                    "Nenhum jogador encontrado com a camisa "
                            + numeroCamisa
            );
        }

        repository.deleteByNumeroCamisa(numeroCamisa);
    }

    private Jogador buscarEntidade(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Jogador com id " + id + " não encontrado"
                ));
    }
}

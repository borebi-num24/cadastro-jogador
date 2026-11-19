const API = "/jogador";

const $ = (id) => document.getElementById(id);
const formJogador = $("formJogador");
const tabela = $("listaJogadores");
const mensagem = $("mensagem");
const modalPatch = $("modalPatch");

let temporizadorMensagem;

function mostrarMensagem(texto, tipo = "success") {
    clearTimeout(temporizadorMensagem);
    mensagem.textContent = texto;
    mensagem.className = `message ${tipo}`;
    temporizadorMensagem = setTimeout(() => {
        mensagem.className = "message";
        mensagem.textContent = "";
    }, 6000);
}

async function requisicao(url, opcoes = {}) {
    const resposta = await fetch(url, {
        ...opcoes,
        headers: {
            ...(opcoes.body ? { "Content-Type": "application/json" } : {}),
            ...opcoes.headers
        }
    });

    if (resposta.status === 204) {
        if (!resposta.ok) throw new Error(`Erro HTTP ${resposta.status}`);
        return null;
    }

    const tipo = resposta.headers.get("content-type") || "";
    let corpo = null;

    if (tipo.includes("application/json")) {
        corpo = await resposta.json();
    } else {
        const texto = await resposta.text();
        corpo = texto ? { detail: texto } : null;
    }

    if (!resposta.ok) {
        const detalhe = corpo?.detail || corpo?.message ||
            (corpo?.erros ? Object.values(corpo.erros).join("\n") : null) ||
            `Erro HTTP ${resposta.status}`;
        throw new Error(detalhe);
    }

    return corpo;
}

function formatarData(data) {
    if (!data) return "—";
    const partes = String(data).split("-");
    return partes.length === 3 ? `${partes[2]}/${partes[1]}/${partes[0]}` : data;
}

function nomePosicao(posicao) {
    const nomes = {
        GOLEIRO: "Goleiro", ZAGUEIRO: "Zagueiro", LATERAL: "Lateral",
        VOLANTE: "Volante", MEIA: "Meia", ATACANTE: "Atacante"
    };
    return nomes[posicao] || posicao || "—";
}

function criarCelula(texto, classe = "") {
    const td = document.createElement("td");
    td.textContent = texto ?? "—";
    if (classe) td.className = classe;
    return td;
}

function atualizarResumo(jogadores) {
    $("totalJogadores").textContent = jogadores.length;
    $("totalGoleiros").textContent = jogadores.filter(j => j.posicao === "GOLEIRO").length;
    $("totalAtacantes").textContent = jogadores.filter(j => j.posicao === "ATACANTE").length;
    $("quantidadeExibida").textContent =
        `${jogadores.length} ${jogadores.length === 1 ? "jogador" : "jogadores"}`;
}

function renderizarJogadores(jogadores) {
    tabela.replaceChildren();
    atualizarResumo(jogadores);

    if (!jogadores.length) {
        const linha = document.createElement("tr");
        const celula = criarCelula("Nenhum jogador encontrado.", "empty-state");
        celula.colSpan = 7;
        linha.appendChild(celula);
        tabela.appendChild(linha);
        return;
    }

    jogadores.forEach(jogador => {
        const linha = document.createElement("tr");
        linha.append(
            criarCelula(jogador.id, "id-cell"),
            criarCelula(jogador.nome, "player-name")
        );

        const posicao = criarCelula("");
        const badge = document.createElement("span");
        badge.className = "position-pill";
        badge.textContent = nomePosicao(jogador.posicao);
        posicao.appendChild(badge);
        linha.append(posicao);

        const camisa = criarCelula("");
        const numero = document.createElement("span");
        numero.className = "shirt-number";
        numero.textContent = jogador.numeroCamisa;
        camisa.appendChild(numero);
        linha.append(
            camisa,
            criarCelula(formatarData(jogador.dataNascimento)),
            criarCelula(jogador.nacionalidade || "—")
        );

        const acoes = criarCelula("");
        acoes.className = "row-actions";
        const editar = document.createElement("button");
        editar.type = "button";
        editar.textContent = "PUT · Editar";
        editar.addEventListener("click", () => preencherFormulario(jogador));

        const patch = document.createElement("button");
        patch.type = "button";
        patch.textContent = "PATCH";
        patch.addEventListener("click", () => abrirPatch(jogador));

        const excluir = document.createElement("button");
        excluir.type = "button";
        excluir.textContent = "Excluir";
        excluir.className = "delete-row";
        excluir.addEventListener("click", () => excluirPorId(jogador.id));

        acoes.append(editar, patch, excluir);
        linha.appendChild(acoes);
        tabela.appendChild(linha);
    });
}

async function carregarJogadores(filtros = {}) {
    try {
        const parametros = new URLSearchParams();
        Object.entries(filtros).forEach(([chave, valor]) => {
            if (valor !== "" && valor !== null && valor !== undefined) {
                parametros.set(chave, valor);
            }
        });

        const url = parametros.size ? `${API}?${parametros.toString()}` : API;
        const jogadores = await requisicao(url);
        renderizarJogadores(jogadores);
    } catch (erro) {
        mostrarMensagem(`Não foi possível carregar os jogadores: ${erro.message}`, "error");
    }
}

function lerDadosFormulario() {
    return {
        nome: $("nome").value.trim(),
        posicao: $("posicao").value,
        numeroCamisa: Number($("numeroCamisa").value),
        dataNascimento: $("dataNascimento").value,
        nacionalidade: $("nacionalidade").value.trim() || null
    };
}

formJogador.addEventListener("submit", async evento => {
    evento.preventDefault();
    const id = $("id").value;
    const dados = lerDadosFormulario();

    try {
        if (id) {
            await requisicao(`${API}/${id}`, {
                method: "PUT",
                body: JSON.stringify(dados)
            });
            mostrarMensagem(`Jogador #${id} atualizado com sucesso.`);
        } else {
            await requisicao(API, {
                method: "POST",
                body: JSON.stringify(dados)
            });
            mostrarMensagem("Jogador cadastrado com sucesso.");
        }
        limparFormulario();
        await carregarJogadores();
    } catch (erro) {
        mostrarMensagem(`Não foi possível salvar: ${erro.message}`, "error");
    }
});

function preencherFormulario(jogador) {
    $("id").value = jogador.id;
    $("nome").value = jogador.nome || "";
    $("posicao").value = jogador.posicao || "";
    $("numeroCamisa").value = jogador.numeroCamisa ?? "";
    $("dataNascimento").value = jogador.dataNascimento || "";
    $("nacionalidade").value = jogador.nacionalidade || "";
    $("tituloFormulario").textContent = `Editar jogador #${jogador.id} (PUT)`;
    $("btnSalvar").textContent = "Salvar alterações";
    $("nome").focus();
    window.scrollTo({ top: 0, behavior: "smooth" });
}

function limparFormulario() {
    formJogador.reset();
    $("id").value = "";
    $("tituloFormulario").textContent = "Adicionar jogador";
    $("btnSalvar").textContent = "Cadastrar jogador";
}

$("btnLimpar").addEventListener("click", limparFormulario);
$("btnAtualizar").addEventListener("click", () => carregarJogadores());

$("formFiltros").addEventListener("submit", evento => {
    evento.preventDefault();
    carregarJogadores({
        id: $("filtroId").value,
        nome: $("filtroNome").value.trim(),
        posicao: $("filtroPosicao").value,
        numeroCamisa: $("filtroCamisa").value
    });
});

$("btnLimparFiltros").addEventListener("click", () => {
    $("formFiltros").reset();
    carregarJogadores();
});

$("formBuscaId").addEventListener("submit", async evento => {
    evento.preventDefault();
    const id = $("buscaId").value;
    if (!id || Number(id) < 1) {
        mostrarMensagem("Informe um ID válido.", "error");
        return;
    }
    try {
        const jogador = await requisicao(`${API}/${id}`);
        renderizarJogadores([jogador]);
    } catch (erro) {
        renderizarJogadores([]);
        mostrarMensagem(`Busca por ID: ${erro.message}`, "error");
    }
});

$("formBuscaCamisa").addEventListener("submit", async evento => {
    evento.preventDefault();
    const numero = $("buscaCamisa").value;
    if (!numero || Number(numero) < 1 || Number(numero) > 99) {
        mostrarMensagem("Informe um número de camisa entre 1 e 99.", "error");
        return;
    }
    try {
        const jogador = await requisicao(`${API}/camisa/${numero}`);
        renderizarJogadores([jogador]);
    } catch (erro) {
        renderizarJogadores([]);
        mostrarMensagem(`Busca por camisa: ${erro.message}`, "error");
    }
});

async function excluirPorId(id) {
    if (!confirm(`Tem certeza que deseja excluir o jogador de ID ${id}?`)) return;
    try {
        await requisicao(`${API}/${id}`, { method: "DELETE" });
        mostrarMensagem(`Jogador #${id} excluído com sucesso.`);
        await carregarJogadores();
    } catch (erro) {
        mostrarMensagem(`Não foi possível excluir: ${erro.message}`, "error");
    }
}

$("formExcluirCamisa").addEventListener("submit", async evento => {
    evento.preventDefault();
    const numero = $("excluirCamisa").value;
    if (!numero || Number(numero) < 1 || Number(numero) > 99) {
        mostrarMensagem("Informe um número de camisa válido.", "error");
        return;
    }
    if (!confirm(`Deseja excluir o jogador que utiliza a camisa ${numero}?`)) return;
    try {
        await requisicao(`${API}/camisa/${numero}`, { method: "DELETE" });
        mostrarMensagem(`Jogador da camisa ${numero} excluído com sucesso.`);
        $("excluirCamisa").value = "";
        await carregarJogadores();
    } catch (erro) {
        mostrarMensagem(`Não foi possível excluir: ${erro.message}`, "error");
    }
});

function abrirPatch(jogador) {
    $("patchId").value = jogador.id;
    $("formPatch").reset();
    $("patchId").value = jogador.id;
    modalPatch.showModal();
}

$("fecharModal").addEventListener("click", () => modalPatch.close());
$("cancelarPatch").addEventListener("click", () => modalPatch.close());

$("formPatch").addEventListener("submit", async evento => {
    evento.preventDefault();
    const id = $("patchId").value;
    const dados = {};

    const nome = $("patchNome").value.trim();
    const posicao = $("patchPosicao").value;
    const numero = $("patchNumeroCamisa").value;
    const nascimento = $("patchDataNascimento").value;
    const nacionalidade = $("patchNacionalidade").value.trim();

    if (nome) dados.nome = nome;
    if (posicao) dados.posicao = posicao;
    if (numero) dados.numeroCamisa = Number(numero);
    if (nascimento) dados.dataNascimento = nascimento;
    if (nacionalidade) dados.nacionalidade = nacionalidade;

    if (Object.keys(dados).length === 0) {
        mostrarMensagem("Preencha pelo menos um campo para atualizar.", "error");
        return;
    }

    try {
        await requisicao(`${API}/${id}`, {
            method: "PATCH",
            body: JSON.stringify(dados)
        });
        modalPatch.close();
        mostrarMensagem(`Informações do jogador #${id} atualizadas parcialmente.`);
        await carregarJogadores();
    } catch (erro) {
        mostrarMensagem(`Não foi possível atualizar parcialmente: ${erro.message}`, "error");
    }
});

carregarJogadores();

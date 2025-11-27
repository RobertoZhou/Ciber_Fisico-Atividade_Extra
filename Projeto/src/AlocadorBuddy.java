public class AlocadorBuddy {
    private Memoria[] listaBlocos;
    private int quantidadeBlocos;
    private int capacidadeMaxima;

    public AlocadorBuddy() {
        capacidadeMaxima = 8192;
        listaBlocos = new Memoria[capacidadeMaxima];
        quantidadeBlocos = 0;
        long memoriaTotal = 4L * 1024L * 1024L;
        adicionar(new Memoria(memoriaTotal, null, true, 0, memoriaTotal));
    }

    private void adicionar(Memoria bloco) {
        if (quantidadeBlocos >= capacidadeMaxima)
            return;
        listaBlocos[quantidadeBlocos] = bloco;
        quantidadeBlocos++;
    }

    private void tirar(int posicao) {
        if (posicao < 0 || posicao >= quantidadeBlocos)
            return;
        int indice = posicao;
        while (indice < quantidadeBlocos - 1) {
            listaBlocos[indice] = listaBlocos[indice + 1];
            indice++;
        }
        listaBlocos[quantidadeBlocos - 1] = null;
        quantidadeBlocos--;
    }

    private long ajustarTamanho(long valor) {
        if ((valor & (valor - 1)) == 0)
            return valor;
        long potencia = 1;
        while (potencia < valor) {
            potencia *= 2;
        }
        return potencia;
    }

    private void partir(int indice) {
        Memoria blocoAtual = listaBlocos[indice];
        long metade = blocoAtual.getComprimento() / 2;
        long posicaoInicio = blocoAtual.getPosicaoComeco();

        Memoria esquerdo = new Memoria(metade, null, true, posicaoInicio, metade);
        Memoria direito = new Memoria(metade, null, true, posicaoInicio + metade, metade);

        tirar(indice);
        adicionar(esquerdo);
        adicionar(direito);
    }

    public boolean alocar(String rotulo, long tamanhoKB) {
        long tamanhoBytes = tamanhoKB * 1024L;
        long tamanhoNecessario = ajustarTamanho(tamanhoBytes);
        if (tamanhoNecessario < 1024L)
            tamanhoNecessario = 1024L;

        int indiceEscolhido = buscar(tamanhoNecessario);
        if (indiceEscolhido == -1)
            return false;

        while (listaBlocos[indiceEscolhido].getComprimento() > tamanhoNecessario) {
            long posicaoAtual = listaBlocos[indiceEscolhido].getPosicaoComeco();
            partir(indiceEscolhido);

            indiceEscolhido = -1;
            int contador = 0;
            while (contador < quantidadeBlocos) {
                if (listaBlocos[contador] != null && listaBlocos[contador].isSituacao()) {
                    if (listaBlocos[contador].getPosicaoComeco() == posicaoAtual
                            && listaBlocos[contador].getComprimento() >= tamanhoNecessario) {
                        indiceEscolhido = contador;
                        break;
                    }
                }
                contador++;
            }
            if (indiceEscolhido == -1)
                return false;
        }

        listaBlocos[indiceEscolhido].addItem(rotulo, tamanhoBytes);
        return true;
    }

    private int buscar(long tamanho) {
        int indiceMelhor = -1;
        long tamanhoMelhor = 4L * 1024L * 1024L + 1;

        for (int i = 0; i < quantidadeBlocos; i++) {
            if (listaBlocos[i] != null && listaBlocos[i].isSituacao()) {
                long tamanhoBloco = listaBlocos[i].getComprimento();
                if (tamanhoBloco == tamanho)
                    return i;
                if (tamanhoBloco >= tamanho && tamanhoBloco < tamanhoMelhor) {
                    tamanhoMelhor = tamanhoBloco;
                    indiceMelhor = i;
                }
            }
        }
        return indiceMelhor;
    }

    public boolean liberar(String rotulo) {
        for (int i = 0; i < quantidadeBlocos; i++) {
            if (listaBlocos[i] != null && !listaBlocos[i].isSituacao()) {
                String rotuloBloco = listaBlocos[i].getDisponibilidade();
                if (comparar(rotuloBloco, rotulo)) {
                    listaBlocos[i].situacaoAtual();
                    juntar();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean comparar(String primeira, String segunda) {
        if (primeira == null && segunda == null)
            return true;
        if (primeira == null || segunda == null)
            return false;

        char[] caracteresPrimeira = primeira.toCharArray();
        char[] caracteresSegunda = segunda.toCharArray();

        int tamanhoPrimeira = 0;
        for (int i = 0; i < caracteresPrimeira.length; i++) {
            tamanhoPrimeira++;
        }

        int tamanhoSegunda = 0;
        for (int i = 0; i < caracteresSegunda.length; i++) {
            tamanhoSegunda++;
        }

        if (tamanhoPrimeira != tamanhoSegunda)
            return false;

        for (int i = 0; i < tamanhoPrimeira; i++) {
            if (caracteresPrimeira[i] != caracteresSegunda[i])
                return false;
        }

        return true;
    }

    private void juntar() {
        boolean continuaJuntando = true;
        while (continuaJuntando) {
            continuaJuntando = false;
            int indice = 0;
            while (indice < quantidadeBlocos && !continuaJuntando) {
                if (listaBlocos[indice] == null || !listaBlocos[indice].isSituacao()) {
                    indice++;
                    continue;
                }
                int outroIndice = indice + 1;
                while (outroIndice < quantidadeBlocos && !continuaJuntando) {
                    if (listaBlocos[outroIndice] == null || !listaBlocos[outroIndice].isSituacao()) {
                        outroIndice++;
                        continue;
                    }
                    if (listaBlocos[indice].buddy(listaBlocos[outroIndice])) {
                        Memoria esquerdo;
                        Memoria direito;
                        int indiceEsquerdo;
                        int indiceDireito;

                        if (listaBlocos[indice].getPosicaoComeco() < listaBlocos[outroIndice].getPosicaoComeco()) {
                            esquerdo = listaBlocos[indice];
                            direito = listaBlocos[outroIndice];
                            indiceEsquerdo = indice;
                            indiceDireito = outroIndice;

                        } else {
                            esquerdo = listaBlocos[outroIndice];
                            direito = listaBlocos[indice];
                            indiceEsquerdo = outroIndice;
                            indiceDireito = indice;
                        }
                        long novoTamanho = esquerdo.getComprimento() + direito.getComprimento();
                        Memoria blocoNovo = new Memoria(novoTamanho, null, true, esquerdo.getPosicaoComeco(),
                                novoTamanho);
                        if (indiceEsquerdo < indiceDireito) {
                            tirar(indiceDireito);
                            tirar(indiceEsquerdo);
                        } else {
                            tirar(indiceEsquerdo);
                            tirar(indiceDireito);
                        }
                        adicionar(blocoNovo);
                        continuaJuntando = true;
                    }
                    outroIndice++;
                }
                indice++;
            }
        }
    }

    public long memoriaLivre() {
        long somaTotal = 0;
        for (int i = 0; i < quantidadeBlocos; i++) {
            if (listaBlocos[i] != null && listaBlocos[i].isSituacao()) {
                somaTotal += listaBlocos[i].getComprimento();
            }
        }
        return somaTotal;
    }

    public int contarLivres() {
        int contagem = 0;
        for (int j = 0; j < quantidadeBlocos; j++) {
            if (listaBlocos[j] != null && listaBlocos[j].isSituacao())
                contagem++;
        }
        return contagem;
    }

    public Memoria[] listarLivres() {
        int quantidade = contarLivres();
        Memoria[] blocosLivres = new Memoria[quantidade];
        int posicao = 0;
        int indice = 0;
        while (indice < quantidadeBlocos) {
            if (listaBlocos[indice] != null && listaBlocos[indice].isSituacao()) {
                blocosLivres[posicao] = listaBlocos[indice];
                posicao++;
            }
            indice++;
        }
        return blocosLivres;
    }

    public Memoria[] listarOcupados() {
        int quantidade = 0;
        int indice = 0;
        while (indice < quantidadeBlocos) {
            if (listaBlocos[indice] != null && !listaBlocos[indice].isSituacao())
                quantidade++;
            indice++;
        }
        Memoria[] blocosOcupados = new Memoria[quantidade];
        int posicao = 0;
        indice = 0;
        while (indice < quantidadeBlocos) {
            if (listaBlocos[indice] != null && !listaBlocos[indice].isSituacao()) {
                blocosOcupados[posicao] = listaBlocos[indice];
                posicao++;
            }
            indice++;
        }
        return blocosOcupados;
    }

    public int contarOcupados() {
        int contagem = 0;
        for (int k = 0; k < quantidadeBlocos; k++) {
            if (listaBlocos[k] != null && !listaBlocos[k].isSituacao())
                contagem++;
        }
        return contagem;
    }
}

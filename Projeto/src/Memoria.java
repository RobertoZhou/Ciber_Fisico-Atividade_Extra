public class Memoria {
    private long tamanhoMemoria;
    private String verificarMemoria;
    private boolean situacao;
    private long posicaoComeco;
    private long comprimento;

    public Memoria(long tamanhoMemoria, String verificarMemoria, boolean situacao, long posicaoComeco, long comprimento) {
        this.tamanhoMemoria = tamanhoMemoria;
        this.verificarMemoria = verificarMemoria;
        this.situacao = situacao;
        this.posicaoComeco = posicaoComeco;
        this.comprimento = comprimento;
    }

    public long getTamanhoMemoria() {
        return tamanhoMemoria;
    }

    public void setTamnahoMemoria(long tamnhoMemoria) {
        this.tamanhoMemoria = tamnhoMemoria;
    }

    public String getDisponibilidade() {
        return verificarMemoria;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.verificarMemoria = disponibilidade;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }

    public long getPosicaoComeco() {
        return posicaoComeco;
    }

    public void setPosicaoComeco(long posicaoComeco) {
        this.posicaoComeco = posicaoComeco;
    }

    public long getComprimento() {
        return comprimento;
    }

    public void setComprimento(long comprimento) {
        this.comprimento = comprimento;
    }


    public void addItem(String label, long progSize) {
        this.situacao = false;
        this.verificarMemoria = label;
        this.tamanhoMemoria = progSize;
    }

    public void situacaoAtual() {
        this.situacao = true;
        this.verificarMemoria = null;
        this.tamanhoMemoria = 0;
    }

    public boolean buddy(Memoria itens) {
        if (this.tamanhoMemoria != itens.tamanhoMemoria) {
            return false;
        }

        long posBuddy;
        if ((this.posicaoComeco / this.tamanhoMemoria) % 2 == 0) {
            posBuddy = this.posicaoComeco + this.tamanhoMemoria;
        } else {
            // Este bloco está em posição ímpar, buddy está à esquerda
            posBuddy = this.posicaoComeco - this.tamanhoMemoria;
        }

        return itens.posicaoComeco == posBuddy;
    }
}


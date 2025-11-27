public class Memoria {
    private long tamnhoMemoria;
    private String disponibilidade;
    private boolean situacao;
    private long posicaoComeco;
    private long comprimento;

    public Memoria(long tamnhoMemoria, String disponibilidade, boolean situacao, long posicaoComeco, long comprimento) {
        this.tamnhoMemoria = tamnhoMemoria;
        this.disponibilidade = disponibilidade;
        this.situacao = situacao;
        this.posicaoComeco = posicaoComeco;
        this.comprimento = comprimento;
    }

    public long getTamnhoMemoria() {
        return tamnhoMemoria;
    }

    public void setTamnhoMemoria(long tamnhoMemoria) {
        this.tamnhoMemoria = tamnhoMemoria;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
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
}

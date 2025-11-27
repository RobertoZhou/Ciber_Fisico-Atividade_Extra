public class BuffyPrograma {

    // Classe simples representando um programa do arquivo
    public static class BlocoPrograma {
        public String nome;
        public long tamanhoKB;

        public BlocoPrograma(String nome, long tamanhoKB) {
            this.nome = nome;
            this.tamanhoKB = tamanhoKB;
        }

        public static int contarProgramas(BlocoPrograma[] lista) {
            if (lista == null) {
                return 0;
            }

            int qtd = 0;
            for (int i = 0; i < lista.length; i++) {
                qtd++;
            }
            return qtd;
        }

        // Lê arquivo TXT e devolve vetor com programas encontrados
        public static BlocoPrograma[] carregarDoArquivo(String caminho) {

            BlocoPrograma[] vetorTemp = new BlocoPrograma[100]; // limite simples
            int indice = 0;

            BufferedReader leitor = null;

            try {
                leitor = new BufferedReader(new FileReader(caminho));
                String linha;
            }
        }
    }
}

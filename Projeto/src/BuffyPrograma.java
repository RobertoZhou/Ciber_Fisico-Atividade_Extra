import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BuffyPrograma {

    // Classe simples para representar cada programa do arquivo
    public static class BlocoPrograma {

        public String nome;
        public long tamanhoKB;

        public BlocoPrograma(String nome, long tamanhoKB) {
            this.nome = nome;
            this.tamanhoKB = tamanhoKB;
        }
    }

    // Retorna quantos programas existem no vetor
    public static int contar(BlocoPrograma[] lista) {

        if (lista == null) {
            return 0;
        }

        int total = 0;

        for (int i = 0; i < lista.length; i++) {
            total++;
        }

        return total;
    }

    // Lê o arquivo TXT e retorna um vetor de BlocoPrograma
    public static BlocoPrograma[] lerArquivo(String caminho) {

        BlocoPrograma[] listaTemp = new BlocoPrograma[100]; // limite simples
        int total = 0;

        BufferedReader leitor = null;

        try {

            leitor = new BufferedReader(new FileReader(caminho));
            String linhaAtual;

            while ((linhaAtual = leitor.readLine()) != null) {

                linhaAtual = linhaAtual.trim();

                if (linhaAtual.length() == 0) {
                    continue;
                }

                String[] partes = linhaAtual.split("\\s+");

                if (partes.length > 1) {

                    String rotulo = partes[0];
                    long tamKB = Long.parseLong(partes[1]);

                    if (total < 100) {
                        listaTemp[total] = new BlocoPrograma(rotulo, tamKB);
                        total++;
                    }

                }

            }

        } catch (IOException erro) {
            System.out.println("Erro ao abrir ou ler o arquivo: " + caminho);
            return null;
        } catch (NumberFormatException erro) {
            System.out.println("Erro ao converter o tamanho do programa");
            return null;
        } finally {

            if (leitor != null) {
                try {
                    leitor.close();
                } catch (IOException erro) {
                }
            }

        }

        BlocoPrograma[] saida = new BlocoPrograma[total];

        for (int i = 0; i < total; i++) {
            saida[i] = listaTemp[i];
        }

        return saida;
    }
}

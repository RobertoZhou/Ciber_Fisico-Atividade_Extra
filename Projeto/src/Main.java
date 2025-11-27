public class Main {
    
    public static void main(String[] args) {
        String nomeArquivo = "testes.txt";
        if (args.length > 0) {
            nomeArquivo = args[0];
        }
        
        imprimirCabecalho();
        System.out.println("Arquivo de entrada: " + nomeArquivo);
        System.out.println();
        
        LeituraFile.BlocoPrograma[] programas = LeituraFile.lerArquivo(nomeArquivo);
        
        if (programas == null) {
            System.out.println("ERRO: Não foi possível ler o arquivo de programas.");
            return;
        }
        
        int totalProgramas = LeituraFile.contar(programas);
        System.out.println("Total de programas lidos: " + totalProgramas);
        System.out.println();
        
        AlocadorBuddy alocador = new AlocadorBuddy();
        
        System.out.println("=== INICIANDO ALOCAÇÕES ===");
        System.out.println();
        
        int sucesso = 0;
        int falhas = 0;
        
        int index = 0;
        while (index < totalProgramas) {
            LeituraFile.BlocoPrograma prog = programas[index];
            System.out.print("Alocando programa " + prog.nome + " (" + prog.tamanhoKB + " KB)... ");
            
            boolean resultado = alocador.alocar(prog.nome, prog.tamanhoKB);
            
            if (resultado) {
                System.out.println("OK");
                sucesso++;
            } else {
                System.out.println("FALHA - memória insuficiente");
                falhas++;
            }
            index++;
        }
        
        System.out.println();
        System.out.println("Alocações bem-sucedidas: " + sucesso);
        System.out.println("Alocações falhadas: " + falhas);
        System.out.println();
        
        imprimirRelatorio(alocador);
    }
    
    private static void imprimirCabecalho() {
        System.out.println("================================================================");
        System.out.println("    ALOCADOR DE MEMÓRIA - BUDDY BINÁRIO");
        System.out.println("    Memória Total: 4 MB (4194304 bytes)");
        System.out.println("    Bloco Mínimo: 1 KB (1024 bytes)");
        System.out.println("================================================================");
        System.out.println();
    }
    

    private static void imprimirRelatorio(AlocadorBuddy alocador) {
        System.out.println("================================================================");
        System.out.println("    RELATÓRIO FINAL DE MEMÓRIA");
        System.out.println("================================================================");
        System.out.println();
        
        long memoriaLivre = alocador.memoriaLivre();
        System.out.println("--- MEMÓRIA LIVRE ---");
        System.out.println("Total livre: " + memoriaLivre + " bytes (" + (memoriaLivre / 1024) + " KB)");
        System.out.println();
        
        int blocosLivres = alocador.contarLivres();
        System.out.println("--- FRAGMENTAÇÃO ---");
        System.out.println("Número de blocos livres: " + blocosLivres);
        System.out.println();
        
        int totalAlocados = alocador.contarOcupados();
        System.out.println("--- PROGRAMAS ALOCADOS ---");
        System.out.println("Número de programas alocados: " + totalAlocados);
        
        if (totalAlocados > 0) {
            System.out.println();
            Memoria[] blocosAlocados = alocador.listarOcupados();
            
            ordenarBlocosPorPosicao(blocosAlocados, totalAlocados);
            
            System.out.println("Detalhes dos programas alocados:");
            System.out.println("========================================================");
            System.out.println("= Programa I Tamanho Real I Bloco Alocado I   Posição   =");
            System.out.println("--------------------------------------------------------");
            
            for (int i = 0; i < totalAlocados; i++) {
                Memoria bloco = blocosAlocados[i];
                String rotulo = bloco.getDisponibilidade();
                long tamanhoPrograma = bloco.getTamanhoMemoria();
                long tamanhoBloco = bloco.getComprimento();
                long posicao = bloco.getPosicaoComeco();
                
                String rotuloStr = preencherDireita(rotulo, 8);
                String tamProgStr = preencherEsquerda(formatarBytes(tamanhoPrograma), 13);
                String tamBlocoStr = preencherEsquerda(formatarBytes(tamanhoBloco), 13);
                String posStr = preencherEsquerda(String.valueOf(posicao), 13);
                
                System.out.println("= " + rotuloStr + "= " + tamProgStr + "= " + tamBlocoStr + "= " + posStr + "=");
            }
            
            System.out.println("========================================================");
        }
        System.out.println();
        
        long totalAlocado = 0;
        long totalUsado = 0;
        Memoria[] blocosAlocados = alocador.listarOcupados();
        
        int j = 0;
        while (j < totalAlocados) {
            totalAlocado += blocosAlocados[j].getComprimento();
            totalUsado += blocosAlocados[j].getTamanhoMemoria();
            j++;
        }
        
        long desperdicio = totalAlocado - totalUsado;
        double percentualDesperdicio = (totalAlocado > 0) ? (desperdicio * 100.0 / totalAlocado) : 0.0;
        
        System.out.println("--- ESTATÍSTICAS DE USO ---");
        System.out.println("Memória alocada (blocos): " + totalAlocado + " bytes (" + (totalAlocado / 1024) + " KB)");
        System.out.println("Memória efetivamente usada: " + totalUsado + " bytes (" + (totalUsado / 1024) + " KB)");
        System.out.println("Desperdício interno: " + desperdicio + " bytes (" + (desperdicio / 1024) + " KB)");
        System.out.println("Porcentagem de desperdício: " + formatarDouble(percentualDesperdicio) + "%");
        System.out.println();
        
        System.out.println("================================================================");
    }
    
    private static void ordenarBlocosPorPosicao(Memoria[] blocos, int quantidade) {
        int i = 0;
        while (i < quantidade - 1) {
            int k = 0;
            while (k < quantidade - i - 1) {
                if (blocos[k].getPosicaoComeco() > blocos[k + 1].getPosicaoComeco()) {
                    Memoria temp = blocos[k];
                    blocos[k] = blocos[k + 1];
                    blocos[k + 1] = temp;
                }
                k++;
            }
            i++;
        }
    }
    
    private static String formatarBytes(long bytes) {
        return bytes + " B (" + (bytes / 1024) + " KB)";
    }
    
    private static String formatarDouble(double valor) {
        long parteInteira = (long) valor;
        long parteDecimal = (long) ((valor - parteInteira) * 100);
        
        if (parteDecimal < 0) {
            parteDecimal = -parteDecimal;
        }
        
        String strDecimal = String.valueOf(parteDecimal);
        if (parteDecimal < 10) {
            strDecimal = "0" + strDecimal;
        }
        
        return parteInteira + "." + strDecimal;
    }
    private static String preencherDireita(String texto, int tamanho) {
        if (texto == null) {
            texto = "";
        }   
        
        char[] caracteres = texto.toCharArray();
        int tamanhoTexto = 0;
        int x = 0;
        while (x < caracteres.length) {
            tamanhoTexto++;
            x++;
        }
        
        if (tamanhoTexto >= tamanho) {
            return texto;
        }
        
        char[] resultado = new char[tamanho];
        for (int i = 0; i < tamanhoTexto; i++) {
            resultado[i] = caracteres[i];
        }
        int j = tamanhoTexto;
        while (j < tamanho) {
            resultado[j] = ' ';
            j++;
        }
        
        return new String(resultado);
    }
    
    private static String preencherEsquerda(String texto, int tamanho) {
        if (texto == null) {
            texto = "";
        }
        
        char[] caracteres = texto.toCharArray();
        int tamanhoTexto = 0;
        for (int i = 0; i < caracteres.length; i++) {
            tamanhoTexto++;
        }
        
        if (tamanhoTexto >= tamanho) {
            return texto;
        }
        
        int espacos = tamanho - tamanhoTexto;
        char[] resultado = new char[tamanho];
        
        int index = 0;
        while (index < espacos) {
            resultado[index] = ' ';
            index++;
        }
        for (int i = 0; i < tamanhoTexto; i++) {
            resultado[espacos + i] = caracteres[i];
        }
        
        return new String(resultado);
    }
}
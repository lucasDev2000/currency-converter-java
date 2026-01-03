import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        String apiKey = "997d8e7383ebdfc19f497d8a"; // Sua chave
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String base = "";
        String alvo = "";
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("*************************************************");
            System.out.println("Seja bem-vindo/a ao Conversor de Moeda =]");
            System.out.println("\n1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileiro");
            System.out.println("4) Real brasileiro => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Sair");
            System.out.println("Escolha uma opção válida:");
            System.out.println("*************************************************");

            opcao = leitura.nextInt();

            if (opcao == 7) {
                System.out.println("Saindo...");
                break;
            }

            switch (opcao) {
                case 1: base = "USD"; alvo = "ARS"; break;
                case 2: base = "ARS"; alvo = "USD"; break;
                case 3: base = "USD"; alvo = "BRL"; break;
                case 4: base = "BRL"; alvo = "USD"; break;
                case 5: base = "USD"; alvo = "COP"; break;
                case 6: base = "COP"; alvo = "USD"; break;
                default:
                    System.out.println("Opção inválida!");
                    continue;
            }

            System.out.println("Digite o valor que deseja converter:");
            double valor = leitura.nextDouble();

            String endereco = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + base;

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                Moeda minhaMoeda = gson.fromJson(response.body(), Moeda.class);

                if (minhaMoeda.conversion_rates().containsKey(alvo)) {
                    double taxa = minhaMoeda.conversion_rates().get(alvo);
                    double resultado = valor * taxa;

                    System.out.printf("Valor %.2f [%s] corresponde ao valor final de =>>> %.2f [%s]\n", valor, base, resultado, alvo);
                } else {
                    System.out.println("Erro: Moeda não encontrada na resposta da API.");
                }

            } catch (Exception e) {
                System.out.println("Erro na conexão ou leitura: " + e.getMessage());
            }
        }
    }
}
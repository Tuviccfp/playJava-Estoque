package com.metodosestaticos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    public static void createProduct(Scanner scanner) {
        System.out.println("Digite a quantidade de produtos que será inserida: ");
        int quantityArrayProduct = scanner.nextInt();
        List<Product> listaProducts = new ArrayList<>();
        for (int i = 0; i < quantityArrayProduct; i++) {
            System.out.print("Digite o nome do produto: ");
            String nameProduct = scanner.next();
            System.out.println("Digite o preço do produto: ");
            double price = scanner.nextDouble();
            System.out.println("Digite a quantidade em estoque: ");
            int quantityEstoque = scanner.nextInt();
            Product newProduct = new Product(nameProduct, price, quantityEstoque);
            listaProducts.add(newProduct);
        }

        for(Product itemProduct : listaProducts) {
            MongoConnect.insertDataProduct(itemProduct.getName(), itemProduct.getPrice(), itemProduct.getQuantity());
        }
    }

    //Função que escreve no meu arquivo
    @SuppressWarnings("resource")
    public static void escreverArquivo(String path, List<Product> product) throws IOException {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            for(Product itemProduct : product) {
                itemProduct.calculateTotal();
                String representString = itemProduct.toString();
                bufferedWriter.append(representString);
            }
            if (path.isEmpty()) {
                throw new RuntimeException("Ops.. Parece que o caminho específicado na máquina não existe, contate o suporte do sistema.");
            }
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    //Função responsável por criar o arquivo.
    public static void gerarArquivo(Scanner scanner, List<Product> product) throws IOException {
        System.out.println("\nSeleção escolhida: Gerar arquivo");
        System.out.println("\nPara funcionar com êxito, você precisará copiar o caminho de uma pasta que quer gerar o arquivo.");

        System.out.print("Digite o caminho desejado: ");
        String caminhoArquivo = scanner.next();

        System.out.println("Digite o nome do arquivo: ");
        String nameArquivo = scanner.next();

        System.out.println("Digite o tipo de arquivo que quer gerar: (Excel / Notas) ");
        String typeArquivo = scanner.next();

        if (typeArquivo.equals("excel")) {
            String path = String.format("%s/%s.xlsx", caminhoArquivo, nameArquivo);
            escreverArquivo(path, product);
            System.out.println("\nArquivo gerado com sucesso.");
        } else if (typeArquivo.equals("Notas")) {
            String path = String.format("%s/%s.txt", caminhoArquivo, nameArquivo);
            escreverArquivo(path, product);
            System.out.println("\nArquivo gerado com sucesso.");
        }
    }

    public static void option(int selectOptionMenu, Scanner scanner) throws IOException {
        switch (selectOptionMenu) {
            case 1:
                createProduct(scanner);
                break;

            case 2:
                System.out.println("\nSeleção escolhida: Calcular total em estoque");
                System.out.print("Digite o nome do produto: ");
                String name = scanner.next();
                List<Product> clienteEncontrado = MongoConnect.findOne(name);
                List<Product> result = clienteEncontrado.stream().filter(t -> t.getName().equals(name)).collect(Collectors.toList());
                result.forEach(t -> t.calculateTotal());
                clienteEncontrado.forEach(System.out::println);
                break;
        
            case 3:
                System.out.println("Seleção escolhida: Listar produtos");
                List<Product> listaDeProdutos = MongoConnect.findAllProduct();
                listaDeProdutos.forEach(System.out::println);
                System.out.println();
                break;

            case 4:
                System.out.println("Digite o nome do produto que deseja encarar: ");
                String nameCase4= scanner.next();
                List<Product> searchProduct = MongoConnect.findOne(nameCase4);
                searchProduct.forEach(t -> t.toString());
                break;
            
            case 5:
                System.out.println("Seleção escolhida: Editar produto");
                String nameCase5  = scanner.next();
                MongoConnect.UpdateDataByName(nameCase5, scanner);
                break;
            
            case 6: 
                System.out.println("\nSeleção escolhida: Remover um produto");
                System.out.print("Digite o nome do produto que será deletado: ");
                String nameProduct = scanner.next();
                MongoConnect.deleteDataByName(nameProduct);
                break;
            
            case 7:
                List<Product> listaProducts = MongoConnect.findAllProduct(); 
                gerarArquivo(scanner, listaProducts);
                break;
            
            default:
                System.out.println("Não existe nenhuma opção disponível.");
                break;
        }
    }
}

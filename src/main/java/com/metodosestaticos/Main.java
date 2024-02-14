package com.metodosestaticos;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        MongoConnect.connect();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Seja bem vindo.");
            System.out.println("");
            System.out.println("Escolha uma das opções abaixo: ");
            System.out.println("1° - Criar um novo produto: "); //Criar 
            System.out.println("2° - Calcular total em estoque: "); //Calcular total em estoque
            System.out.println("3° - Listar produtos: "); //Listar
            System.out.println("4° - Procurar um produto: "); //Procurar por nome ou id
            System.out.println("5° - Editar um produto"); //Editar
            System.out.println("6° - Remover um produto"); //remover
            System.out.println("7° - Gerar arquivo");
            System.out.println("8° - Sair do programa");
            int selectOptionMenu = scanner.nextInt();
            
            Menu.option(selectOptionMenu, scanner);
                    
            
            if(selectOptionMenu == 8) {
                scanner.close();
                break;
            }
        }
    }
}
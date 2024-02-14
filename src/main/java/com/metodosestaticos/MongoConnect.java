package com.metodosestaticos;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;


public class MongoConnect {
    static String uri = "mongodb://localhost:27017/";

    public static void connect() {
        try (MongoClient mongoClient = MongoClients.create(uri);){
            System.out.println("Conectado com o mongodb.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void insertDataProduct(String name, double price, int quantity) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("brincandoJava");
            MongoCollection<Document> collection = mongoDatabase.getCollection("product");
            try {
                InsertOneResult result = collection.insertOne(new Document().append("name", name). append("price", price).append("quantityEstoque", quantity));
                System.out.println("Sucesso. Dado inserido com sucesso." + result.getInsertedId());
            }
         catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar");
            }
        }
    }

    //Ler a documentação na parte de finds. IMPORTANTE!!
    //Método estático que retorna uma lista de Product
    public static List<Product> findAllProduct() {
        List<Product> productList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(uri)){
            MongoDatabase database = mongoClient.getDatabase("brincandoJava");
            MongoCollection<Document> collection = database.getCollection("product");
        
            Bson projectionFields = Projections.fields(Projections.include("name", "price", "quantityEstoque"), Projections.excludeId());

            FindIterable<Document> find = collection.find().projection(projectionFields);

            try (MongoCursor<Document> cursor = find.iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    
                    Product itemProduct = new Product(
                        document.getString("name"), 
                        document.getDouble("price"), 
                        document.getInteger("quantityEstoque")
                    );
                    
                    productList.add(itemProduct);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar os produtos: " + e.getMessage());
        }
        return productList;
    }

    public static List<Product> findOne(String name) {
        List<Product> product = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("brincandoJava");
            MongoCollection<Document> collection = database.getCollection("product");
            
            Bson filter = Filters.eq("name", name);

            FindIterable<Document> docFind = collection.find(filter).projection(null);

            try (MongoCursor<Document> cursor = docFind.iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();

                    Product unitProduct = new Product(document.getString("name"), document.getDouble("price"), document.getInteger("quantityEstoque"));
                    product.add(unitProduct);
                }
            } catch (Exception e) {
                System.out.println("Não existe nada com esse nome.");
            }
        }
        return product;
    }

    public static void UpdateDataByName(String name, Scanner scanner) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("brincandoJava");
            MongoCollection<Document> collection = database.getCollection("product");
            
            Bson filter = Filters.eq("name", name);
            FindIterable<Document> existingDocument = collection.find(filter);
            // MongoCursor<Document> cursor = existingDocument.iterator();

            if (existingDocument != null) {
                System.out.print("Digite o novo nome: ");
                String newName = scanner.next();
                System.out.println("Digite o novo preço: ");
                double newPrice = scanner.nextDouble();
                System.out.println("Digite a nova quantidade em estoque: ");
                int newQuantityEstoque = scanner.nextInt();
    
                Document replaceDocument = new Document().append("name", newName).append("price", newPrice).append("quantityEstoque", newQuantityEstoque);
    
                ReplaceOptions options = new ReplaceOptions().upsert(true);
    
                try {
                    UpdateResult result = collection.replaceOne(filter, replaceDocument, options);
                    System.out.println("\nDados alterado com sucesso.");
                    System.out.println("Quantidade de vezes que foi alterado: " + result.getModifiedCount());
                } catch (Exception e) {
                    System.out.println("Erro ao alterar.");
                }
            } else {
                System.out.println("Não é possível localizar nenhum produto com esse nome.");
            }
        } 
    }

    public static void deleteDataByName(String name) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("brincandoJava");
            MongoCollection<Document> collection = database.getCollection("product");

            Bson query = Filters.eq("name", name);

            try {
                DeleteResult result = collection.deleteOne(query);
                System.out.println("Documento deletado. Quantidade de documento deletado:  " + result.getDeletedCount());
            } catch (Exception e) {
                System.out.println("Erro ao deletar.");
            }
        }
    }
}

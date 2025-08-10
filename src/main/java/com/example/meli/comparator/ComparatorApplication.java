package com.example.meli.comparator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 *
 * Inicialização do contexto Spring usando a anotação
 * @SpringBootApplication, que indica que essa é a classe de boot da aplicação.
 * Também defino o pacote base para escanear componentes, serviços e configurações,
 * garantindo que toda a minha estrutura sob "com.example.meli.comparator" seja carregada.
 *
 * O mét0do main é o ponto de entrada da aplicação, que executa a inicialização do Spring.
 */
@SpringBootApplication(scanBasePackages = "com.example.meli.comparator")
public class ComparatorApplication {

    public static void main(String[] args) {
        // Inicio o contexto do Spring, que vai gerenciar toda a injeção de dependências
        SpringApplication.run(ComparatorApplication.class, args);
    }

}

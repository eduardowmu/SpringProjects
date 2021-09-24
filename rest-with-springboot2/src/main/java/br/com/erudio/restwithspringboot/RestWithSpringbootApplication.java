package br.com.erudio.restwithspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*Aqui temos duas novas notations:
 *1 - @EnableAutoConfiguration - permite que o Application Context do Spring
 *seja automaticamente carregado baseado nos JARS e nas configurações
 *que definimos. Isso sempre é feita depois que os BEANS já foram registrados
 *no Applications Context. Isso tem uma grande vantagem de diminuir sua responsa
 *bilidade da definição das configurações. Em alguns casos podemos excluir
 *algumas classes dessas configurações automáticas
 *
 *2 - @ComponentScan - usada para dizer ao springboot que devemos scanear nossos
 *pacotes e encontrar arquivos de configuração, como o WebConfig*/
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class RestWithSpringbootApplication 
{	public static void main(String[] args) 
	{SpringApplication.run(RestWithSpringbootApplication.class, args);}
}
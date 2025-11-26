package com.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Clase principal de la aplicación frontend.
 *
 * Funciones principales:
 * - Arranca la aplicación Spring Boot.
 * - Habilita el soporte para Feign Clients, permitiendo la comunicación
 *   con los microservicios (usuarios, citas, agenda, notificaciones).
 *
 * Anotaciones:
 * @SpringBootApplication: Indica que esta es la clase principal de Spring Boot,
 *                        habilita auto-configuración y escaneo de componentes.
 * @EnableFeignClients: Habilita la detección de interfaces Feign para la inyección
 *                      de clientes HTTP hacia los microservicios.
 */
@EnableFeignClients
@SpringBootApplication
public class FrontendApplication {

	/**
	 * Método principal que arranca la aplicación.
	 * @param args Argumentos de línea de comando (opcional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}
}

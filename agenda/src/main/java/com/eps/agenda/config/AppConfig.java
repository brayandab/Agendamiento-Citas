package com.eps.agenda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Clase de configuración principal de la aplicación.
 *
 * Esta clase se encarga de definir beans que estarán disponibles
 * para toda la aplicación. En este caso, se crea un bean de RestTemplate,
 * que es el cliente HTTP usado para realizar llamadas REST a otros servicios.
 */
@Configuration
public class AppConfig {

    /**
     * Define y expone un bean de tipo RestTemplate.
     *
     * RestTemplate permite hacer peticiones HTTP como GET, POST,
     * PUT, DELETE, etc., hacia otros microservicios o APIs externas.
     * Al declararlo como Bean, Spring lo gestiona y lo coloca en el
     * contenedor para que pueda ser inyectado en otras clases con
     * @Autowired o mediante constructor.
     *
     * @return una instancia nueva y configurada de RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package cz.net21.ttulka;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsulController {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private DiscoveryClient discoveryClient;

    public Optional<URI> serviceUrl() {
        List<URI> urls = discoveryClient.getInstances("web")
               .stream()
               .map(si -> si.getUri())
               .collect(Collectors.toList());
        
        System.out.println("DISCOVERED URLS: " + urls);
        
        // load balancing
        Collections.shuffle(urls);

        return !urls.isEmpty() ? Optional.of(urls.get(0)) : Optional.empty();
    }

    @GetMapping("/discoveryClient")
    public String discoveryClient() throws RestClientException,
            ServiceUnavailableException {
        URI service = serviceUrl()
                .map(s -> s.resolve("/"))
                .orElseThrow(ServiceUnavailableException::new);

        System.out.println("SERVICE URL: " + service);

        return restTemplate.getForEntity(service, String.class)
                .getBody();
    }
}
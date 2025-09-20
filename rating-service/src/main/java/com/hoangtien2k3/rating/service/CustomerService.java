package com.hoangtien2k3.rating.service;

import com.hoangtien2k3.rating.config.ServiceUrlConfig;
import com.hoangtien2k3.rating.viewmodel.CustomerVm;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

@Service
@RequiredArgsConstructor
public class CustomerService extends AbstractCircuitBreakFallbackHandler {

    private final RestTemplate restTemplate;
    private final ServiceUrlConfig serviceUrlConfig;

//    @Retry(name = "restApi")
    @CircuitBreaker()
    public CustomerVm getCustomer() {
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        final URI url = UriComponentsBuilder
                .fromHttpUrl(serviceUrlConfig.customer())
                .path("/storefront/customer/profile")
                .buildAndExpand()
                .toUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, CustomerVm.class).getBody();
    }

    @Override
    protected CustomerVm handleFallback(Throwable throwable) throws Throwable {
        return null;
    }
}

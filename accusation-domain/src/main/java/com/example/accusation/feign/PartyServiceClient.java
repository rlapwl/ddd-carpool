package com.example.accusation.feign;

import com.example.accusation.feign.dto.PartyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "party", url = "${api.url.party}")
public interface PartyServiceClient {

    @GetMapping(path = "/{id}")
    PartyResponse getParty(@PathVariable long id);

}

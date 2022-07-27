package com.mungta.accusation.feign;

import com.mungta.accusation.feign.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user", url = "${api.url.user}")
public interface UserServiceClient {

    @GetMapping(path = "")
    List<UserResponse> getUserList(@RequestParam List<String> memberIds);

}

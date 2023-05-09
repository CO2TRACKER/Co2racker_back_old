package com.example.tanso.boot.utils;

import com.example.tanso.boot.exception.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StringUtils {
    /**
     * 주소를 전달받아 "구" 에 해당하는 문자열을 반환한다.
     */
    public String getGuFromAddress(String address) {
        List<String> addressSplit = List.of(address.split(" "));
        return addressSplit.stream().filter(
                eachAddress -> eachAddress.endsWith("구")
        ).findFirst().orElseThrow(() -> new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "해당하는 \"구\"가 존재하지 않습니다."));
    }
}

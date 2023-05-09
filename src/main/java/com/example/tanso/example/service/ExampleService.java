package com.example.tanso.example.service;

import com.example.tanso.example.domain.model.Example;
import com.example.tanso.example.domain.repository.ExampleRepository;
import com.example.tanso.example.dto.request.ExampleSaveRequestDto;
import com.example.tanso.example.dto.response.ExampleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository exampleRepository;

    public ExampleResponseDto createExample(ExampleSaveRequestDto requestDto) throws Exception {
        Example exampleEntity = exampleRepository.save(requestDto.toEntity());
        return ExampleResponseDto.builder().entity(exampleEntity).build();
    }

    public List<ExampleResponseDto> findAllExamples() throws Exception {
        return exampleRepository.findAll().stream().map(exampleEntity -> ExampleResponseDto.builder()
                        .entity(exampleEntity)
                        .build())
                .collect(Collectors.toList());
    }
}

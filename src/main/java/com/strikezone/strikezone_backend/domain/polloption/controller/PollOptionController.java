package com.strikezone.strikezone_backend.domain.polloption.controller;

import com.strikezone.strikezone_backend.domain.polloption.dto.request.controller.PollOptionCreateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.service.PollOptionCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.response.PollOptionResponseDto;
import com.strikezone.strikezone_backend.domain.polloption.service.PollOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polloptions")
@RequiredArgsConstructor
public class PollOptionController {
}

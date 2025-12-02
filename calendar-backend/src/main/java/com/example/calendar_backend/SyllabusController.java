package com.example.calendar_backend;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // allow the React dev server
public class SyllabusController {

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
    return Map.of("syllabusId", UUID.randomUUID().toString());
  }

  @GetMapping("/plan/{id}")
  public PlanResponse getPlan(@PathVariable String id) {
    return new PlanResponse(List.of(new Assignment("HW1", LocalDate.now(), 120)));
  }

  @GetMapping("/hello")
  public String hello() {
    return "Backend works";
  }
}

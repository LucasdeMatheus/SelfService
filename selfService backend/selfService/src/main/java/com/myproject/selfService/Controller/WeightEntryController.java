package com.myproject.selfService.Controller;

import com.myproject.selfService.Service.WeightEntryService;
import com.myproject.selfService.domain.WeightDTO;
import com.myproject.selfService.domain.WeightEntry;
import com.myproject.selfService.domain.WeightEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class WeightEntryController {

    @Autowired
    private WeightEntryService weightEntryService;


    @PostMapping("/calculate-weight")
    public ResponseEntity<WeightEntryDTO> calculateWeight(@RequestBody WeightDTO weightEntryDTO){
        return weightEntryService.calculateWeight(weightEntryDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<WeightEntry> create(@RequestBody WeightDTO weightEntryDTO){
        return weightEntryService.create(weightEntryDTO);
    }
}

package com.myproject.selfService.Service;

import com.myproject.selfService.domain.WeightDTO;
import com.myproject.selfService.domain.WeightEntry;
import com.myproject.selfService.domain.WeightEntryDTO;
import com.myproject.selfService.domain.WeightEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeightEntryService {

    @Autowired
    private WeightEntryRepository weightEntryRepository;


    public ResponseEntity<WeightEntryDTO> calculateWeight(WeightDTO weightDTO) {
        return ResponseEntity.ok(new WeightEntryDTO(weightDTO.weight(),weightDTO.weight() * 10));
    }



    public ResponseEntity<WeightEntry> create(WeightDTO weightDTO) {
        WeightEntry weightEntry = weightEntryRepository.save(new WeightEntry(weightDTO.weight(),weightDTO.weight() * 10, LocalDateTime.now()));

        return ResponseEntity.ok(weightEntry);
    }


}

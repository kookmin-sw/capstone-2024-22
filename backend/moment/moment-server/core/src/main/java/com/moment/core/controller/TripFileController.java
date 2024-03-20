package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.domain.trip.Trip;
import com.moment.core.dto.response.TripFileResponseDTO;
import com.moment.core.service.TripFileService;
import com.moment.core.service.TripService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/core/tripfile")
public class TripFileController {
    private final TripFileService tripFileService;
    private final TripService tripService;

    // 해당 여행의 모든 파일 가져오기
    @GetMapping("/{tripId}")
    public ResponseEntity<APIResponse<TripFileResponseDTO.GetAllTripFile>> getTripFiles(
            @PathVariable Long tripId,
            @RequestParam Long userId) {
        TripFileResponseDTO.GetAllTripFile allTripFile = tripFileService.getTripFiles(userId, tripId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS,allTripFile));
    }

    // untitled 가져오기
    @GetMapping("/untitled")
    public ResponseEntity<APIResponse<TripFileResponseDTO.GetAllTripFile>> getUntitledTripFiles(
            @RequestParam Long userId) {
        Trip untitledTrip = tripService.getUntitledTripById(userId);
        TripFileResponseDTO.GetAllTripFile allTripFile = tripFileService.getTripFiles(userId, untitledTrip.getId());
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS,allTripFile));
    }
}

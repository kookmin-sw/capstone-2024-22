package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.dto.request.ReceiptRequestDTO;
import com.moment.core.service.ReceiptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/core/receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    // 생성한 영수증 개수 반환 API
    @GetMapping("/count")
    public ResponseEntity<APIResponse<ReceiptRequestDTO.getReceiptCount>> getReceiptCount(
            @RequestHeader Long userId
    ) {
        long count = receiptService.getReceiptCount(userId);
        ReceiptRequestDTO.getReceiptCount response = ReceiptRequestDTO.getReceiptCount.builder()
                .count(count)
                .build();
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, response));
    }

    // 영수증 다 가져오기(페이징?)
    @GetMapping("/all")
    public ResponseEntity<APIResponse<Page<ReceiptRequestDTO.getReceiptAll>>> getAllReceipt(
            @RequestHeader Long userId,
            Pageable page
    ) {
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, receiptService.getAllReceipt(userId, page)));
    }

    // 영수증 여러개 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse> deleteReceipts(
            @RequestHeader Long userId,
            @RequestBody ReceiptRequestDTO.deleteReceipts deleteReceipts
    ) {

        receiptService.deleteReceipts(userId, deleteReceipts);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS));
    }

    // 영수증 생성
    @PostMapping()
    public ResponseEntity<APIResponse> createReceipt(
            @RequestHeader Long userId,
            @RequestBody ReceiptRequestDTO.createReceipt createReceipt
    ) {
        receiptService.createReceipt(userId, createReceipt);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS));
    }

    // 영수증 수정
    @PutMapping()
    public ResponseEntity<APIResponse> updateReceipt(
            @RequestHeader Long userId,
            @RequestBody ReceiptRequestDTO.updateReceipt updateReceipt
    ) {
        receiptService.validateUserWithReceipt(userId, updateReceipt.getId());
        receiptService.updateReceipt(userId, updateReceipt);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }
}

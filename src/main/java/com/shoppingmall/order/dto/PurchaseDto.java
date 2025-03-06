package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {

    private List<Long> purchaseId;

    private String userId;

    private LocalDateTime createAt; // 주문 생성 시간


}

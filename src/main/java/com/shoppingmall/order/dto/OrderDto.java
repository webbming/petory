package com.shoppingmall.order.dto;


import com.shoppingmall.order.domain.PurchaseProduct;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

private List<PurchaseProduct> products;

}

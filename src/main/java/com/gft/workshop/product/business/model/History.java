package com.gft.workshop.product.business.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class History {

    private String userId;
    private Long productId;
    private LocalDateTime timestamp;

}

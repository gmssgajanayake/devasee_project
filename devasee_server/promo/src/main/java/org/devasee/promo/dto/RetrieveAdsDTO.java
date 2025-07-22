package org.devasee.promo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveAdsDTO {
    private String id;
    private String title;
    private String description;
    private String imgUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}

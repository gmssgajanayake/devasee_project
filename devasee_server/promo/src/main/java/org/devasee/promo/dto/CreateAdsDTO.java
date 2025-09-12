package org.devasee.promo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdsDTO {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}

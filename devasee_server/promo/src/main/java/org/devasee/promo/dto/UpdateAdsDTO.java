package org.devasee.promo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO used when updating an existing advertisement
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdsDTO {
    private String id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}

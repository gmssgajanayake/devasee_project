package org.devasee.promo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO returned when deleting an advertisement
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAdsDTO {
    private String id;
    private String message;
}

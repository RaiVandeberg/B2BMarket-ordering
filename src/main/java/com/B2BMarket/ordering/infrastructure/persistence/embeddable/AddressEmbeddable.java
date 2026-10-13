package com.B2BMarket.ordering.infrastructure.persistence.embeddable;

import com.B2BMarket.ordering.domain.model.valueObject.ZipCode;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressEmbeddable {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;

}

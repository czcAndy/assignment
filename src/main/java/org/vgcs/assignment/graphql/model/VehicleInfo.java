package org.vgcs.assignment.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInfo {
    @Id
    private String id;
    private String msidn;
    private String engineStatus;
    private String fleet;
    private String brand;
    private String countryOfOperation;
    private String chassisNumber;
    private String chassisSeries;

}

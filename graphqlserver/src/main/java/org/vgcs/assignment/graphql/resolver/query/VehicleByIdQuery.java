package org.vgcs.assignment.graphql.resolver.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import org.vgcs.assignment.persistance.model.VehicleComplete;

@Component
public class VehicleByIdQuery implements GraphQLQueryResolver {

    public VehicleComplete query(String id) {
        VehicleComplete vehicleComplete = new VehicleComplete();
        vehicleComplete.setId(id);
        return vehicleComplete;
    }
}

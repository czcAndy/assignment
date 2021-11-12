package dto;

import model.Service;

import java.util.List;

public record ServiceResponseDTO(String communicationStatus, List<Service> services) {
}

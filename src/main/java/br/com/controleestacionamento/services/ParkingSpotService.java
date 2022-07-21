package br.com.controleestacionamento.services;

import br.com.controleestacionamento.dtos.ParkingSpotDTO;
import br.com.controleestacionamento.models.ParkingSpotModel;
import br.com.controleestacionamento.repositories.ParkingSpotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }
    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
        return this.parkingSpotRepository.save(parkingSpotModel);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingSpotModel> getAll() {
        return this.parkingSpotRepository.findAll();
//        .stream().map(element -> {
//            var dto = new ParkingSpotDTO();
//            BeanUtils.copyProperties(element, dto);
//            return dto;
//        }).collect(Collectors.toList())
    }

    public ParkingSpotDTO getOne(UUID id) {
        var parkingSpotModel = this.getModelId(id);
        var parkingSpotDto = new ParkingSpotDTO();
        BeanUtils.copyProperties(parkingSpotModel, parkingSpotDto);
        return parkingSpotDto;
    }

    private ParkingSpotModel getModelId(UUID id) {
        var parkingSpotOptional = this.parkingSpotRepository.findById(id);
        if(parkingSpotOptional.isEmpty()) {
            throw new IllegalArgumentException("Parking Spot Not found.");
        }
        return parkingSpotOptional.get();
    }
    @Transactional
    public void delete(UUID id) {
        this.parkingSpotRepository.delete(this.getModelId(id));
    }

    public ParkingSpotDTO put(UUID id, ParkingSpotDTO dto) {
        var parkingSpotModel = this.getModelId(id);

        if(!parkingSpotModel.getParkingSpotNumber().equals(dto.getParkingSpotNumber())) {
            throw new IllegalArgumentException("Você não pode trocar a sua vaga de estacionamento.");
        }

        if (!parkingSpotModel.getResponsibleName().equals(dto.getResponsibleName())) {
            throw new IllegalArgumentException("Somente o responsável pela vaga "
                    + parkingSpotModel.getParkingSpotNumber()
                    + ", pode realizar a operação");
        }

        parkingSpotModel.setLicensePlateCar(dto.getLicensePlateCar());
        parkingSpotModel.setModelCar(dto.getModelCar());
        parkingSpotModel.setBrandCar(dto.getBrandCar());
        parkingSpotModel.setColorCar(dto.getColorCar());
        parkingSpotModel.setApartment(dto.getApartment());
        parkingSpotModel.setBlock(dto.getBlock());
        this.parkingSpotRepository.save(parkingSpotModel);
        BeanUtils.copyProperties(parkingSpotModel, dto);
        return dto;

    }
}

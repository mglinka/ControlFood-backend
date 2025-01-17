package pl.lodz.pl.it.mopa.service;

import pl.lodz.pl.it.mopa.entity.PackageType;
import pl.lodz.pl.it.mopa.repository.PackageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageTypeService {

    private final PackageTypeRepository packageTypeRepository;

    public List<PackageType> getAllPackageTypes(){
        return packageTypeRepository.findAll();
    }
}

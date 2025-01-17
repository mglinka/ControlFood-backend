package pl.lodz.pl.it.mopa.service;

import pl.lodz.pl.it.mopa.entity.NutritionalValueGroup;
import pl.lodz.pl.it.mopa.entity.NutritionalValueName;
import pl.lodz.pl.it.mopa.repository.NutritionalValueGroupRepository;
import pl.lodz.pl.it.mopa.repository.NutritionalValueNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutritionalValueService {
    private final NutritionalValueNameRepository nutritionalValueNameRepository;
    private final NutritionalValueGroupRepository nutritionalValueGroupRepository;

    public List<NutritionalValueName> getAllNutritionalValueNames() {
        return nutritionalValueNameRepository.findAll();
    }

    public List<NutritionalValueGroup> getAllNutritionalValueGroupNames() {
        return nutritionalValueGroupRepository.findAll();
    }
}

package pl.lodz.pl.it.mopa.service;

import pl.lodz.pl.it.mopa.entity.Unit;
import pl.lodz.pl.it.mopa.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}

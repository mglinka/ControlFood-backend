package pl.lodz.pl.it.mopa.service;

import pl.lodz.pl.it.mopa.entity.Category;
import pl.lodz.pl.it.mopa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories (){
        return categoryRepository.findAll();
    }
}

//package com.project.dto.converter;
//
//import com.project.dto.allergyProfile.CreateAllergyProfileDTO;
//import com.project.dto.account.GetAccountDTO;
//import com.project.entity.Account;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class AllergyProfileDTOConverter {
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    public AllergyProfile toAllergyProfile(CreateAllergyProfileDTO createAllergyProfileDTO) {
//        AllergyProfile allergyProfile = modelMapper.map(createAllergyProfileDTO, AllergyProfile.class);
//        return allergyProfile;
//
//
//    }
//    public GetAccountDTO toAccountDto(Account account) {
//
//        GetAccountDTO getAccountDTO = modelMapper.map(account, GetAccountDTO.class);
//        return getAccountDTO;
//    }
//
//
//    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
//        return accounts.stream().map(this::toAccountDto).toList();
//    }
//
//}

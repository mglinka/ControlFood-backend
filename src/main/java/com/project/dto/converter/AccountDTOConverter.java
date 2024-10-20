package com.project.dto.converter;

import com.project.dto.CreateAccountDTO;
import com.project.dto.account.GetAccountDTO;
import com.project.dto.account.GetAccountPersonalDTO;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Account toAccount(CreateAccountDTO createAccountDTO) {
        Account account = modelMapper.map(createAccountDTO, Account.class);
        return account;


    }

    public Account toAccount(UpdateAccountDataDTO updateAccountDataDTO) {
        Account account = modelMapper.map(updateAccountDataDTO, Account.class);
        return account;


    }

    public GetAccountPersonalDTO toAccountPersonalDTO (Account account){
        GetAccountPersonalDTO getAccountPersonalDTO = modelMapper.map(account, GetAccountPersonalDTO.class);
        return getAccountPersonalDTO;
    }

    public GetAccountDTO toAccountDto(Account account) {

        GetAccountDTO getAccountDTO = modelMapper.map(account, GetAccountDTO.class);
        return getAccountDTO;
    }


    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }


}

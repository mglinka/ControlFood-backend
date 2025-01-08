package com.project.dto.converter;

import com.project.dto.account.AccountDTO;
import com.project.dto.account.GetAccountDTO;
import com.project.dto.account.RoleDTO;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.entity.Role;
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

    public RoleDTO toRoleDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public List<RoleDTO> roleDtoList(List<Role> roles) {
        return roles.stream().map(this::toRoleDTO).toList();
    }



    public Account toAccount(UpdateAccountDataDTO updateAccountDataDTO) {
        return modelMapper.map(updateAccountDataDTO, Account.class);


    }

    public AccountDTO toAccountPersonalDTO (Account account){
        return modelMapper.map(account, AccountDTO.class);
    }

    public GetAccountDTO toAccountDto(Account account) {
        GetAccountDTO getAccountDTO = modelMapper.map(account, GetAccountDTO.class);

        getAccountDTO.setEnabled(account.getEnabled());
        getAccountDTO.setRole(account.getRole().getRoleName());

        return getAccountDTO;
    }


    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }


}

package pl.lodz.pl.it.dto.converter;

import pl.lodz.pl.it.dto.account.AccountDTO;
import pl.lodz.pl.it.dto.account.CreateAccountDTO;
import pl.lodz.pl.it.dto.account.GetAccountDTO;
import pl.lodz.pl.it.dto.account.RoleDTO;
import pl.lodz.pl.it.dto.update.UpdateAccountDataDTO;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.Role;
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

    public Account toAccount (CreateAccountDTO createAccountDTO){
        return modelMapper.map(createAccountDTO, Account.class);
    }


    public Account toAccount(UpdateAccountDataDTO updateAccountDataDTO) {
        return modelMapper.map(updateAccountDataDTO, Account.class);


    }

    public AccountDTO toAccountPersonalDTO (Account account){
        return modelMapper.map(account, AccountDTO.class);
    }

    public GetAccountDTO toAccountDto(Account account) {
        if (account.getRole() == null || account.getEnabled() == null) {
            return null;
        }
        GetAccountDTO getAccountDTO = modelMapper.map(account, GetAccountDTO.class);

        System.out.println("Nie wime"+ account.getRole());
        getAccountDTO.setEnabled(account.getEnabled());
        getAccountDTO.setRole(account.getRole().getRoleName());

        return getAccountDTO;
    }


    public List<GetAccountDTO> accountDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountDto).toList();
    }


}

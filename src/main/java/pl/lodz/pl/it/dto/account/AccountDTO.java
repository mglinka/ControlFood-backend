package pl.lodz.pl.it.dto.account;

import pl.lodz.pl.it.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountDTO {

    private UUID id;

    private String email;

    private Role role;

    private String firstName;

    private String lastName;

    private Boolean enabled;
    private Long version;

}

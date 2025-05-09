package pl.lodz.pl.it.dto.account;

import lombok.Data;

import java.util.UUID;

@Data
public class GetAccountDTO {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean enabled;
    private Long version;
}

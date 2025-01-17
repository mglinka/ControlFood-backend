package pl.lodz.pl.it.dto.account;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleDTO {
    private String name;
    private UUID id;
}

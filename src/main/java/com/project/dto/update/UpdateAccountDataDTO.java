package com.project.dto.update;

import com.project.utils.messages.ExceptionMessages;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAccountDataDTO {

    @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
    String firstName;
    @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
    String lastName;
    @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
    Integer gender;
}

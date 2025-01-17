package pl.lodz.pl.it.mok.controller;

import pl.lodz.pl.it.dto.converter.AccountDTOConverter;
import pl.lodz.pl.it.dto.password.RequestChangePassword;
import pl.lodz.pl.it.dto.update.UpdateAccountDataDTO;
import pl.lodz.pl.it.mok.service.MeService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Validated
@RestController
@RequestMapping("api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final MeService meService;
    private final AccountDTOConverter accountDTOConverter;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SPECIALIST', 'ROLE_USER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody RequestChangePassword request,
            Principal connectedUser){
        meService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SPECIALIST', 'ROLE_USER')")
    @PutMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO, @RequestHeader("If-Match") String eTag) {
        try {
            System.out.println("etag"+ eTag);
            meService.updateInfo(updateAccountDataDTO, eTag);
            return ResponseEntity.ok("Dane zostały zaktualizowane");
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Dane zostały zmienione przez innego użytkownika");
        }
//        catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił nieoczekiwany błąd - ll");
//        }
    }



}
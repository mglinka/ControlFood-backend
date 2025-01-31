package pl.lodz.pl.it.mopa.service;



import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.allergy.*;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.mok.repository.allergy.AllergenRepository;
import pl.lodz.pl.it.mok.repository.allergy.AllergyProfileRepository;
import pl.lodz.pl.it.mok.repository.allergy.AllergyProfileSchemaRepository;
import pl.lodz.pl.it.mok.repository.allergy.ProfileAllergenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.pl.it.mopa.dto.AllergenIntensityDTO;
import pl.lodz.pl.it.mopa.dto.AssignProfileDTO;
import pl.lodz.pl.it.mopa.dto.CreateAllergyProfileDTO;
import pl.lodz.pl.it.mopa.dto.UpdateAllergyProfileDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllergyProfileService {

    @PersistenceContext
    EntityManager entityManager;
    private final AllergyProfileRepository allergyProfileRepository;
    private final AccountRepository accountRepository;
    private final AllergenRepository allergenRepository;
    private final ProfileAllergenRepository profileAllergenRepository;
    private final AllergyProfileSchemaRepository allergyProfileSchemaRepository;

    public List<AllergyProfile> getAllAllergyProfile(){

        return allergyProfileRepository.findAll();
    }



    @Transactional
    public AllergyProfile createProfile(CreateAllergyProfileDTO createAllergyProfileDTO) {
        Account account = accountRepository.findById(UUID.fromString(createAllergyProfileDTO.getAccountId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (account.getAllergyProfile() != null) {
            throw new RuntimeException("Account already has an allergy profile");
        }

        AllergyProfile allergyProfile = new AllergyProfile();
        allergyProfile.setAccount(account);

        allergyProfileRepository.save(allergyProfile);
        account.setAllergyProfile(allergyProfile);
        accountRepository.save(account);

        for (AllergenIntensityDTO allergenDTO : createAllergyProfileDTO.getAllergens()) {
            Allergen allergen = allergenRepository.findById(UUID.fromString(allergenDTO.getAllergen_id()))
                    .orElseThrow(() -> new RuntimeException("Allergen not found"));

            ProfileAllergenId profileAllergenId = new ProfileAllergenId();
            profileAllergenId.setAllergenId(allergen.getAllergen_id());
            profileAllergenId.setProfileId(allergyProfile.getProfile_id());

            ProfileAllergen existingProfileAllergen = profileAllergenRepository.findById(profileAllergenId).orElse(null);

            if (existingProfileAllergen != null) {
                existingProfileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergenRepository.save(existingProfileAllergen);
            } else {
                ProfileAllergen profileAllergen = new ProfileAllergen();
                profileAllergen.setId(profileAllergenId);
                profileAllergen.setAllergen(allergen);
                profileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergen.setAllergyProfile(allergyProfile);

                profileAllergenRepository.save(profileAllergen);
            }
        }

        return allergyProfile;
    }


    @Transactional
    public AllergyProfile updateAllergyProfile(UUID profileId, UpdateAllergyProfileDTO updateAllergyProfileDTO) {
        AllergyProfile allergyProfile = allergyProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy profile not found"));

        List<AllergenIntensityDTO> allergens = updateAllergyProfileDTO.getAllergens();
        if (allergens == null || allergens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allergens list cannot be null or empty");
        }

        Set<UUID> providedAllergenIds = allergens.stream()
                .map(dto -> UUID.fromString(dto.getAllergen_id()))
                .collect(Collectors.toSet());

        List<ProfileAllergen> existingProfileAllergens = profileAllergenRepository.findById_ProfileId(profileId);

        for (ProfileAllergen profileAllergen : existingProfileAllergens) {
            UUID allergenId = profileAllergen.getAllergen() != null ? profileAllergen.getAllergen().getAllergen_id() : null;


            if (allergenId != null && !providedAllergenIds.contains(allergenId)) {
                ProfileAllergenId profileAllergenId = new ProfileAllergenId(profileId, allergenId);

                Optional<ProfileAllergen> toDeleteOptional = profileAllergenRepository.findById(profileAllergenId);

                if (toDeleteOptional.isPresent()) {
                    ProfileAllergen toDelete = toDeleteOptional.get();
                    profileAllergenRepository.deleteProfileAllergen(profileId, allergenId);
                }
            } else {
                System.out.println("Allergen ID " + allergenId + " is still in the provided list, skipping deletion.");
            }
        }

        entityManager.flush();

        for (AllergenIntensityDTO allergenDTO : allergens) {
            String allergenIdStr = allergenDTO.getAllergen_id();
            if (allergenIdStr == null || allergenIdStr.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allergen ID cannot be null or empty");
            }

            UUID allergenId = UUID.fromString(allergenIdStr);
            Allergen allergen = allergenRepository.findById(allergenId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergen not found"));

            ProfileAllergenId profileAllergenId = new ProfileAllergenId(profileId, allergenId);
            ProfileAllergen profileAllergen = profileAllergenRepository.findById(profileAllergenId)
                    .orElseGet(() -> {
                        ProfileAllergen newProfileAllergen = new ProfileAllergen();
                        newProfileAllergen.setId(profileAllergenId);
                        newProfileAllergen.setAllergyProfile(allergyProfile);
                        newProfileAllergen.setAllergen(allergen);
                        return newProfileAllergen;
                    });

            profileAllergen.setIntensity(allergenDTO.getIntensity());
            profileAllergenRepository.save(profileAllergen);
        }

        entityManager.flush();

        allergyProfile.setProfileAllergens(profileAllergenRepository.findById_ProfileId(profileId));

        return allergyProfile;
    }








    public AllergyProfile getAllergyProfileById(UUID id) {


        return allergyProfileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }


    public AllergyProfile getAllergyProfileByAccountId(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        System.out.println("Service");
        AllergyProfile allergyProfile = account.getAllergyProfile();
        if (allergyProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy Profile not found for this account");
        }

        UUID profile_id = allergyProfile.getProfile_id();
        return allergyProfileRepository.findById(profile_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy profile not found"));
    }

    @Transactional
    public AllergyProfile assignAllergyProfile(AssignProfileDTO dto) {
        System.out.println("DTO received: " + dto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Account account)) {
            throw new IllegalStateException("Authenticated principal is not an instance of Account");
        }

        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("Account not found with ID: " + account.getId());
        }

        Account persistedAccount = optionalAccount.get();

        if (persistedAccount.getAllergyProfile() != null) {
            throw new RuntimeException("Account already has an allergy profile.");
        }

        AllergyProfile allergyProfile = new AllergyProfile();
        allergyProfile.setAccount(persistedAccount);

        List<AllergyProfileSchema> schemas = dto.getSchema_ids().stream()
                .map(schemaId -> allergyProfileSchemaRepository.findById(schemaId)
                        .orElseThrow(() -> new IllegalArgumentException("Schema not found with ID: " + schemaId)))
                .collect(Collectors.toList());

        List<Allergen> allergens = schemas.stream()
                .flatMap(schema -> schema.getAllergens().stream())
                .map(allergen -> allergenRepository.findById(allergen.getAllergen_id())
                        .orElseThrow(() -> new IllegalArgumentException("Allergen not found with ID: " + allergen.getAllergen_id())))
                .distinct()
                .collect(Collectors.toList());

        List<ProfileAllergen> profileAllergens = new ArrayList<>();

        allergyProfile = allergyProfileRepository.saveAndFlush(allergyProfile);  // Save profile first to get profile_id

        for (Allergen allergen : allergens) {
            ProfileAllergen profileAllergen = new ProfileAllergen();
            ProfileAllergenId profileAllergenId = new ProfileAllergenId();
            profileAllergenId.setAllergenId(allergen.getAllergen_id());
            profileAllergenId.setProfileId(allergyProfile.getProfile_id());

            profileAllergen.setId(profileAllergenId);
            profileAllergen.setIntensity(dto.getIntensity());
            profileAllergen.setAllergen(allergen);
            profileAllergen.setAllergyProfile(allergyProfile);

            profileAllergens.add(profileAllergen);
        }

        allergyProfile.setProfileAllergens(profileAllergens);

        AllergyProfile savedProfile = allergyProfileRepository.save(allergyProfile);

        persistedAccount.setAllergyProfile(savedProfile);
        accountRepository.save(persistedAccount);

        System.out.println("Profile successfully assigned with ID: " + savedProfile.getProfile_id());

        return savedProfile;
    }






}

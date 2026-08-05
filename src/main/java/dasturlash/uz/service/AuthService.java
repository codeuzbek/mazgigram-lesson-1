package dasturlash.uz.service;

import dasturlash.uz.dto.AppResponse;
import dasturlash.uz.dto.auth.AuthDTO;
import dasturlash.uz.dto.auth.RegistrationDTO;
import dasturlash.uz.dto.profile.ProfileDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.AppLanguage;
import dasturlash.uz.enums.ProfileRole;
import dasturlash.uz.enums.ProfileStatus;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ResourceBundleServe bundleServe;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private AttachService attachService;

    public AppResponse<String> registration(RegistrationDTO dto, AppLanguage lang) {

        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            ProfileEntity profile = optional.get();
            if (profile.getProfileStatus().equals(ProfileStatus.IN_REGISTRATION)) {
                profileRepository.delete(profile);
                // send sms/email
            } else {
                log.warn("Profile already exists with name {}", dto.getEmail());
                throw new AppBadException("Bunday email mavjut");
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        entity.setProfileStatus(ProfileStatus.IN_REGISTRATION);
        entity.setVisible(true);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileRole(ProfileRole.ROLE_USER);
        // save
        profileRepository.save(entity);
        // send email
        emailSendingService.sendRegistrationEmail(dto.getEmail(), entity.getId(), lang);
        return new AppResponse<>("Email-ga tastiqlash kodi jo'natildi.");
    }

    public String registrationEmailVerification(String token, AppLanguage lang) {
        try {
            String profileId = JwtUtil.decodeRegVerToken(token);
            ProfileEntity profile = profileService.getById(profileId);
            if (profile.getProfileStatus().equals(ProfileStatus.IN_REGISTRATION)) {
                profileRepository.changeStatus(profileId, ProfileStatus.ACTIVE);
                return "Tastiqlash tugadi mazgi";
            }
        } catch (JwtException e) {

        }
        log.warn("Registration email verification failed {}", token);
        throw new AppBadException("Tastiqlashda xatolik bo'ldi");
    }

    public ProfileDTO login(AuthDTO dto) {
        //  check
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isEmpty()) {
            log.warn("Username or password wrong {} ", dto.getEmail());
            throw new AppBadException("Username or password wrong");
        }
        ProfileEntity profile = optional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), profile.getPassword())) {
            throw new AppBadException("Username or password wrong");
        }
        if (!profile.getProfileStatus().equals(ProfileStatus.ACTIVE)) {
            log.warn("Wrong status: {} ", dto.getEmail());
            throw new AppBadException("Wrong status");
        }
        // response
        return getLogInResponse(profile);
    }

    public ProfileDTO getLogInResponse(ProfileEntity profile) {
        ProfileDTO response = new ProfileDTO();
        response.setId(profile.getId());
        response.setName(profile.getName());
        response.setSurname(profile.getEmail());
        response.setEmail(profile.getEmail());
        response.setRole(profile.getProfileRole());
        response.setJwt(JwtUtil.encode(profile.getEmail(), profile.getId(), response.getRole())); // jwt
        response.setPhoto(attachService.attachDTO(profile.getPhotoId()));
        return response;
    }
}






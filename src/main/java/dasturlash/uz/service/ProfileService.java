package dasturlash.uz.service;

import dasturlash.uz.dto.AppResponse;
import dasturlash.uz.dto.profile.ProfileDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.AppLanguage;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBundleServe bundleServe;

    public AppResponse<String> updatePhoto(String photoId, AppLanguage lang) {
        String profileId = SpringSecurityUtil.getCurrentUserId();
        ProfileEntity profile = getById(profileId);
        profileRepository.updatePhoto(profileId, photoId);

        if (profile.getPhotoId() != null && !profile.getPhotoId().equals(photoId)) {
            attachService.delete(profile.getPhotoId()); // delete old image
        }
        return new AppResponse<>(bundleServe.getMessage("profile.photo.update.success", lang));
    }

    public ProfileEntity getById(String id) {
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
            log.error("Profile not found ID: {}", id);
            throw new AppBadException("Profile not found");
        });
    }

    public ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        return dto;
    }


}


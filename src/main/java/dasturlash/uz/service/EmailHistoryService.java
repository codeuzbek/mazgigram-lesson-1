package dasturlash.uz.service;

import dasturlash.uz.entity.EmailHistoryEntity;
import dasturlash.uz.enums.AppLanguage;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.EmailHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private ResourceBundleServe bundleServe;

    public void create(String email, String code) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setCode(code);
        entity.setAttemptCount(0);
        entity.setCreatedDate(LocalDateTime.now());
        emailHistoryRepository.save(entity);
    }

    public Long getEmailCount(String email) {
        LocalDateTime now = LocalDateTime.now();
        return emailHistoryRepository.countByEmailAndCreatedDateBetween(email, now.minusMinutes(1), now);
    }

    public void check(String email, String code, AppLanguage lang) {
        // find last sms by phoneNumber
        Optional<EmailHistoryEntity> optional = emailHistoryRepository.findTop1ByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty()) {
            log.warn("No email history found for email: {}", email);
            throw new AppBadException(bundleServe.getMessage("verification.failed", lang));
        }
        EmailHistoryEntity entity = optional.get();
        // attempt count
        if (entity.getAttemptCount() >= 3) {
            log.warn("Attempt count reached: {}", email);
            throw new AppBadException(bundleServe.getMessage("verification.failed", lang));
        }
        // check code
        if (!entity.getCode().equals(code)) {
            emailHistoryRepository.updateAttemptCount(entity.getId());
            log.warn("Wrong code: {}", email);// update attempt count
            throw new AppBadException(bundleServe.getMessage("verification.failed", lang));
        }
        // check time
        LocalDateTime expDate = entity.getCreatedDate().plusMinutes(2);
        if (LocalDateTime.now().isAfter(expDate)) { // not valid
            throw new AppBadException(bundleServe.getMessage("verification.failed", lang));
        }
    }
}

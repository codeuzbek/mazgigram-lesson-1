package dasturlash.uz.repository;

import dasturlash.uz.entity.EmailHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, String> {

    // select count(*) from sms_history where email = ? and created_date between ? and ?
    Long countByEmailAndCreatedDateBetween(String email, LocalDateTime from, LocalDateTime to);

    // select * from sms_history where email = ? order by created_date desc limit 1
    Optional<EmailHistoryEntity> findTop1ByEmailOrderByCreatedDateDesc(String email);

    @Modifying
    @Transactional
    @Query("update EmailHistoryEntity set attemptCount = coalesce(attemptCount,0)  + 1 where  id = ?1")
    void updateAttemptCount(String id);
}

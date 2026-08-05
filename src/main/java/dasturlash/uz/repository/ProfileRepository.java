package dasturlash.uz.repository;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndVisibleTrue(String username);

    Optional<ProfileEntity> findByIdAndVisibleTrue(String id);

    @Modifying
    @Transactional
    @Query("update ProfileEntity  set photoId =?2 where id = ?1 ")
    void updatePhoto(String id, String photoId);

    @Modifying
    @Transactional
    @Query("update ProfileEntity  set profileStatus =?2 where id = ?1 ")
    void changeStatus(String id, ProfileStatus status);

    @Query("From ProfileEntity as p where  p.profileStatus = 'ACTIVE' and p.visible = true ")
    List<ProfileEntity> activeProfileList();
}










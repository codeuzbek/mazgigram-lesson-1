package dasturlash.uz.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.attach.AttachDTO;
import dasturlash.uz.enums.ProfileRole;
import dasturlash.uz.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private ProfileRole role;
    private String jwt;
    private AttachDTO photo;
    private ProfileStatus status;
    private LocalDateTime createdDate;
    private Long postCount;
}

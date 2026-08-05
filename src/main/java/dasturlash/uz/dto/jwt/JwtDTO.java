package dasturlash.uz.dto.jwt;

import dasturlash.uz.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private String username;
    private String id;
    private ProfileRole role;
}

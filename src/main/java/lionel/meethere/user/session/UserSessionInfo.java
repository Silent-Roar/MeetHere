package lionel.meethere.user.session;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserSessionInfo {

    private Integer id;

    private String username;

    private Integer admin;
}

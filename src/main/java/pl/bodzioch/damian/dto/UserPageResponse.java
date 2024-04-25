package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record UserPageResponse(

        List<UserListData> users,
        Long totalUsers

) implements Serializable {
}

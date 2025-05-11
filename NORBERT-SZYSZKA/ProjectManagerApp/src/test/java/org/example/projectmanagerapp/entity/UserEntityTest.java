package org.example.projectmanagerapp.entity;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.*;

class UserEntityTest {
    @Test
    void gettersAndSetters() {
        User u = new User();
        u.setId(5L);
        u.setUsername("john");
        var projs = new HashSet<Project>();
        u.setUser_projects(projs);

        assertThat(u.getId()).isEqualTo(5L);
        assertThat(u.getUsername()).isEqualTo("john");
        assertThat(u.getUser_projects()).isSameAs(projs);
    }
}

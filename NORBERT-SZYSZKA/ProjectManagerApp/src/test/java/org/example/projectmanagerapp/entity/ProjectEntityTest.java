package org.example.projectmanagerapp.entity;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.*;

class ProjectEntityTest {
    @Test
    void gettersAndSetters() {
        Project p = new Project();
        p.setId(10L);
        p.setName("X");
        var users = new HashSet<User>();
        var tasks = new HashSet<Task>();
        p.setUsers(users);
        p.setTasks(tasks);

        assertThat(p.getId()).isEqualTo(10L);
        assertThat(p.getName()).isEqualTo("X");
        assertThat(p.getUsers()).isSameAs(users);
        assertThat(p.getTasks()).isSameAs(tasks);
    }
}

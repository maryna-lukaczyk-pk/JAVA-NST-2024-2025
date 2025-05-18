package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult; // Upewnij się, że ten import jest
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            // Aby potencjalnie współdzielić kontener z ProjectControllerIT, użyj tej samej nazwy bazy danych:
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true); // Pozostawiamy withReuse(true)

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.show_sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }

    @BeforeEach
    void setUp() {
        // Czyścimy repozytorium użytkowników przed każdym testem.
        // Jeśli Users mają relacje, które również powinny być czyszczone kaskadowo lub ręcznie,
        // trzeba by to tutaj uwzględnić. Dla prostej encji Users na razie wystarczy.
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        // 1. Przygotuj obiekt użytkownika do wysłania
        Users newUser = new Users();
        newUser.setUsername("testuser123");
        // ID zostanie nadane przez bazę danych

        // 2. Wykonaj żądanie POST do /api/users/create
        // Kontroler UserController zwraca bezpośrednio obiekt Users i status 200 OK domyślnie
        MvcResult mvcResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk()) // Oczekujemy 200 OK (lub 201 Created, jeśli kontroler by tak zwracał)
                .andExpect(jsonPath("$.id", is(notNullValue()))) // Sprawdź, czy ID zostało nadane
                .andExpect(jsonPath("$.username", is("testuser123"))) // Sprawdź, czy username się zgadza
                .andReturn();

        // 3. Opcjonalna weryfikacja: sprawdź, czy użytkownik faktycznie istnieje w bazie danych przez GET
        String responseBody = mvcResult.getResponse().getContentAsString();
        Users createdUser = objectMapper.readValue(responseBody, Users.class);

        mockMvc.perform(get("/api/users/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser123")));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // 1. Najpierw utwórz użytkownika, którego będziemy odczytywać
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoOdczytu");

        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk()) // Zgodnie z implementacją createUser
                .andReturn();

        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();

        // 2. Wykonaj żądanie GET, aby pobrać użytkownika po ID
        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Oczekujemy 200 OK
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.username", is("userDoOdczytu")));
    }

    @Test
    void shouldHandleGetNonExistentUserById() throws Exception {
        // Sprawdzenie zachowania dla nieistniejącego ID użytkownika
        Long nonExistentUserId = 9999L;
        // Twój UserController.getUserById zwraca userService.getUserById(id).orElse(null);
        // Domyślnie Spring MVC, gdy metoda kontrolera zwraca null dla typu obiektowego,
        // może zwrócić 200 OK z pustym ciałem.
        // Jeśli chcesz mieć pewność statusu 404, kontroler powinien zwracać ResponseEntity.notFound().build().
        // Przetestujmy obecne zachowanie. Spodziewamy się, że jsonPath dla nieistniejących pól zawiedzie,
        // lub możemy sprawdzić, czy ciało odpowiedzi jest puste lub null, jeśli kontroler zwraca null.

        // Zmodyfikujmy test, aby sprawdzić, czy odpowiedź jest pusta lub pola nie istnieją.
        // Jeśli Spring jednak jakoś mapuje to na 404 (np. przez konfigurację), ten test by tego nie wykrył.
        // Najpierw zobaczmy, co się stanie, jeśli oczekujemy 200 OK i pustego ciała.
        // Lepszym podejściem byłoby, gdyby kontroler zwracał 404.

        // Na razie załóżmy, że chcemy przetestować, czy kontroler ZWRACA coś co nie jest błędem 500,
        // a następnie zweryfikujemy jego zawartość, która powinna być "pusta" w kontekście danych użytkownika.
        // Idealnie, ten endpoint powinien zwracać 404. Jeśli test zawiedzie na 404, to znaczy, że jest lepiej niż myślałem.
        // Jeśli zawiedzie na 200 OK, to znaczy, że musimy dostosować asercje lub kontroler.
        MvcResult result = mockMvc.perform(get("/api/users/" + nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Spróbujmy najpierw z OK
                .andReturn();

        // Sprawdź, czy ciało odpowiedzi jest puste, co by sugerowało 'null' zwrócony przez kontroler
        String content = result.getResponse().getContentAsString();
        // assertThat(content).isEmpty(); // Jeśli używasz AssertJ
        assertTrue(content.isEmpty(), "Response body should be empty for non-existent user if controller returns null and status is OK");

        // Jeśli ten test zawiedzie, ponieważ dostajesz 404 Not Found, to świetnie! Zmień wtedy .isOk() na .isNotFound().
        // Jeśli test przechodzi z 200 OK i pustym ciałem, to jest to obecne zachowanie Twojego API.
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // 1. Utwórz kilku użytkowników
        Users user1 = new Users();
        user1.setUsername("userAlpha");
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk());

        Users user2 = new Users();
        user2.setUsername("userBeta");
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk());

        // 2. Wykonaj żądanie GET, aby pobrać wszystkich użytkowników
        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Dzięki @BeforeEach z userRepository.deleteAll(), powinniśmy mieć dokładnie 2 użytkowników
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("userAlpha", "userBeta")));
    }

    @Test
    void shouldUpdateExistingUser() throws Exception {
        // 1. Najpierw utwórz użytkownika, którego będziemy aktualizować
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoAktualizacji");

        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();

        // 2. Przygotuj zaktualizowane dane użytkownika
        Users updatedUserDetails = new Users();
        updatedUserDetails.setUsername("zaktualizowanyUser");
        // ID jest ustawiane w kontrolerze: user.setId(id);

        // 3. Wykonaj żądanie PUT, aby zaktualizować użytkownika
        // Twój UserController.updateUser zwraca obiekt Users, więc domyślnie status będzie 200 OK
        mockMvc.perform(put("/api/users/update/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.username", is("zaktualizowanyUser")));

        // 4. Opcjonalna weryfikacja: Pobierz użytkownika i sprawdź, czy zmiany zostały trwale zapisane
        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("zaktualizowanyUser")));
    }

    @Test
    void shouldHandleUpdateNonExistentUser() throws Exception {
        Long nonExistentUserId = 9999L;
        Users updatedUserDetails = new Users();
        updatedUserDetails.setUsername("probaAktualizacjiNieistniejacego");

        // Twój UserService.updateUser zwraca null, jeśli użytkownik nie istnieje.
        // UserController następnie zwraca ten null.
        // Podobnie jak przy GET nieistniejącego użytkownika, spodziewamy się 200 OK z pustym ciałem.
        // Jeśli chcesz status 404, kontroler/serwis powinien to inaczej obsłużyć.

        MvcResult result = mockMvc.perform(put("/api/users/update/" + nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDetails)))
                .andExpect(status().isOk()) // Oczekujemy 200 OK, jeśli kontroler zwraca null
                .andReturn();

        // Sprawdź, czy ciało odpowiedzi jest puste
        String content = result.getResponse().getContentAsString();
        assertTrue(content.isEmpty(), "Response body should be empty when trying to update non-existent user if controller returns null");

        // Podobnie jak poprzednio: jeśli ten test zawiedzie, bo dostajesz 404 Not Found,
        // to znaczy, że Twoje API zachowuje się lepiej (bardziej standardowo) - zmień wtedy .isOk() na .isNotFound().
    }

    @Test
    void shouldDeleteExistingUser() throws Exception {
        // 1. Najpierw utwórz użytkownika, którego będziemy usuwać
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoUsuniecia");

        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();

        // 2. Wykonaj żądanie DELETE, aby usunąć użytkownika
        // Twój UserController.deleteUser jest void. Spring domyślnie może zwrócić 200 OK lub 204 No Content.
        // 204 No Content jest bardziej standardowe dla DELETE. Zobaczmy, co dostaniemy.
        mockMvc.perform(delete("/api/users/delete/" + userId))
                .andExpect(status().isOk()); // Lub .isNoContent() jeśli tak jest skonfigurowane/działa

        // 3. Weryfikacja: Spróbuj pobrać usuniętego użytkownika
        // Powinniśmy dostać zachowanie zgodne z Twoim testem shouldHandleGetNonExistentUserById()
        // (np. 200 OK z pustym ciałem lub 404 Not Found, jeśli kontroler zostałby poprawiony)
        MvcResult getResult = mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                // Zależnie od tego, jak ustaliłeś zachowanie dla GET nieistniejącego:
                .andExpect(status().isOk()) // Jeśli GET na nieistniejący zwraca 200 OK z pustym ciałem
                .andReturn();
        assertTrue(getResult.getResponse().getContentAsString().isEmpty(), "Response for deleted user should be empty if GET non-existent returns 200 OK empty body");
        // LUB JEŚLI GET na nieistniejący użytkownika zwraca 404:
        // .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleDeleteNonExistentUser() throws Exception {
        Long nonExistentUserId = 9999L;

        // Kiedy userService.deleteUser(id) jest wywoływane dla nieistniejącego ID,
        // userRepository.deleteById(id) rzuci EmptyResultDataAccessException.
        // Domyślnie Spring Boot może mapować ten wyjątek na status 500 Internal Server Error,
        // chyba że masz globalny handler wyjątków, który mapuje go na 404.
        // ProjectController miał try-catch, UserController nie. Zobaczmy co się stanie.
        // Optymalnie byłoby 404.

        mockMvc.perform(delete("/api/users/delete/" + nonExistentUserId))
                // Sprawdźmy, czy dostaniemy 404. Jeśli nie, zobaczymy co jest.
                .andExpect(status().isNotFound());
        // Jeśli dostaniesz 500, musiałbyś dodać obsługę EmptyResultDataAccessException
        // w globalnym handlerze wyjątków i mapować na 404, albo w samym kontrolerze.
    }
}
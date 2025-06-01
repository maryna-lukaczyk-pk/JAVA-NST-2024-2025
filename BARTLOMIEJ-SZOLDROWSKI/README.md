Imie: Bartlomiej

Nazwisko: Szoldrowski

Numer grupy: 4

# System Zarządzania Kinem

## Opis projektu
Aplikacja umożliwia zarządzanie repertuarem kina, rezerwacją miejsc oraz zarządzaniem użytkownikami i pracownikami kina (administratorami). Projekt został wykonany zgodnie z zasadami OOP, SOLID oraz przy użyciu wzorców projektowych.

## Technologie
- **Java 17**
- **Spring Boot** + **Spring Security**
- **Hibernate (JPA)**
- **PostgreSQL**
- **Maven**
- **Docker**
- **Swagger UI**
- **JUnit** + **JaCoCo**
- **Git** (z poprawnym workflow i podpisanymi commitami)

## Wymagania systemowe
- Java JDK 17+
- Docker
- Maven 3.6+

## Role użytkowników

### **User**
- Przeglądanie repertuaru kinowego
- Rezerwacja biletów na seanse
- Zarządzanie własnym kontem użytkownika

### **Administrator**
- Dodawanie i usuwanie seansów filmowych
- Zarządzanie salami kinowymi
- Zarządzanie użytkownikami systemu
- Zarządzanie repertuarem kinowym

## Testowanie
- Pokrycie testami jednostkowymi: **≥ 80%** (raport JaCoCo)
  
![image](https://github.com/user-attachments/assets/c8ef70c2-2f4c-445c-b440-e5f012e2bb86)


- Wykorzystane technologie:
  - **JUnit 5**
  - **Mockito**
- Testowane komponenty:
  - Warstwa serwisów
  - Kontrolery API
  - Walidatory danych

## Baza danych
### Główne technologie
- **PostgreSQL** jako system zarządzania bazą danych
- **Hibernate** jako ORM (mapowanie obiektowo-relacyjne)

### Struktura bazy
Diagram ERD dostępny poniżej:

![image](https://github.com/user-attachments/assets/6483b895-60bb-4f67-8df0-cd4f411eccf5)


## Opis Endpointów

### CinemaHall
| Metoda HTTP | Endpoint                          | Opis                                      | Dostęp dla      |
|-------------|-----------------------------------|-------------------------------------------|-----------------|
| GET         | `/api/cinemahalls`                | Pobierz wszystkie sale                   | USER, ADMIN     |
| POST        | `/api/cinemahalls`                | Utwórz nową salę                         | ADMIN           |
| GET         | `/api/cinemahalls/{id}`           | Pobierz salę po ID                       | USER, ADMIN     |
| PUT         | `/api/cinemahalls/{id}`           | Zaktualizuj salę                         | ADMIN           |
| DELETE      | `/api/cinemahalls/{id}`           | Usuń salę po ID                          | ADMIN           |
| GET         | `/api/cinemahalls/3d`             | Pobierz sale obsługujące 3D              | USER, ADMIN     |
| GET         | `/api/cinemahalls/capacity`       | Pobierz sale o minimalnej pojemności     | USER, ADMIN     |

### Movie
| Metoda HTTP | Endpoint                     | Opis                              | Dostęp dla      |
|-------------|------------------------------|-----------------------------------|-----------------|
| GET         | `/api/movies`                | Pobierz wszystkie filmy          | USER, ADMIN     |
| POST        | `/api/movies`                | Utwórz nowy film                 | ADMIN           |
| PUT         | `/api/movies/{id}`           | Zaktualizuj film                 | ADMIN           |
| DELETE      | `/api/movies/{id}`           | Usuń film                        | ADMIN           |
| GET         | `/api/movies/{id}`           | Pobierz film po ID               | USER, ADMIN     |
| GET         | `/api/movies/search`         | Wyszukaj filmy po tytule         | USER, ADMIN     |
| GET         | `/api/movies/genre/{genre}`  | Pobierz filmy wg gatunku         | USER, ADMIN     |

### Reservation
| Metoda HTTP | Endpoint                                             | Opis                                      | Dostęp dla      |
|-------------|------------------------------------------------------|-------------------------------------------|-----------------|
| GET         | `/api/reservations`                                  | Pobierz wszystkie rezerwacje             | ADMIN           |
| POST        | `/api/reservations`                                  | Utwórz nową rezerwację                   | USER            |
| GET         | `/api/reservations/{id}`                             | Pobierz rezerwację po ID                 | USER, ADMIN     |
| GET         | `/api/reservations/user/{userId}`                    | Pobierz rezerwacje użytkownika           | USER, ADMIN     |
| PUT         | `/api/reservations/{id}/status`                      | Aktualizuj status płatności rezerwacji   | ADMIN           |
| DELETE      | `/api/reservations/{id}`                             | Usuń rezerwację                          | ADMIN           |
| GET         | `/api/reservations/screening/{screeningId}/available-seats` | Pobierz liczbę dostępnych miejsc na seansie | USER, ADMIN |

### Screening
| Metoda HTTP | Endpoint                                       | Opis                                      | Dostęp dla      |
|-------------|------------------------------------------------|-------------------------------------------|-----------------|
| GET         | `/api/screenings`                              | Pobierz wszystkie seanse                 | USER, ADMIN     |
| POST        | `/api/screenings`                              | Utwórz nowy seans                        | ADMIN           |
| GET         | `/api/screenings/{id}`                         | Pobierz seans po ID                      | USER, ADMIN     |
| GET         | `/api/screenings/movie/{movieId}`              | Pobierz seanse dla filmu o danym ID      | USER, ADMIN     |
| GET         | `/api/screenings/between?start=...&end=...`    | Pobierz seanse między datami             | USER, ADMIN     |
| DELETE      | `/api/screenings/{id}`                         | Usuń seans po ID                         | ADMIN           |
| GET         | `/api/screenings/hall/{hallId}/availability`   | Sprawdź dostępność sali kinowej          | ADMIN           |

### Ticket
| Metoda HTTP | Endpoint                                       | Opis                                      | Dostęp dla      |
|-------------|------------------------------------------------|-------------------------------------------|-----------------|
| POST        | `/api/tickets`                                 | Utwórz nowy bilet                        | ADMIN           |
| GET         | `/api/tickets/reservation/{reservationId}`     | Pobierz bilety dla danej rezerwacji      | USER, ADMIN     |
| GET         | `/api/tickets/{id}`                            | Pobierz bilet po ID                      | USER, ADMIN     |
| DELETE      | `/api/tickets/{id}`                            | Usuń bilet po ID                         | ADMIN           |
| GET         | `/api/tickets/reservation/{reservationId}/totalPrice` | Oblicz całkowitą cenę dla rezerwacji | USER, ADMIN     |


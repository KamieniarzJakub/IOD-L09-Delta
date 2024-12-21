# IOD-L09-Delta
* Jakub Kamieniarz 155845 Proxy Product Owner
* Adam Detmer 155976 Scrum Master
* Jakub Buler 155987
* Tomasz Pawłowski 155965
## ScenarioQualityChecker
Dla analityków dokumentujących wymagania funkcjonalne za pomocą scenariuszy nasza aplikacja SQC dostarczy informacji ilościowych oraz umożliwi wykrywanie problemów w wymaganiach funkcjonalnych zapisanych w postaci scenariuszy. Aplikacja będzie dostępna poprzez GUI a także jako zdalne API dzięki czemu można ją zintegrować z istniejącymi narzędziami.

## Product log
https://docs.google.com/spreadsheets/d/1i_asc-qoH2MX9fnkgrB1XPraAk8zYmRW17bshJaDTZo/edit?gid=1934159657#gid=1934159657

## Cel Sprint 1:
<strong>Implementacja serwera REST oraz podstawowych funkcji do analizy scenariusza</strong>
## Cel Sprint 2:
<strong>Implementacja interaktywnego GUI oraz pobierania scenariusza</strong>

### Notacja scenariuszy:
- Scenariusz zawiera nagłówek określający jego tytuł i aktorów (zewnętrznych oraz system)
- Scenariusz składa się z kroków (każdy krok zawiera tekst)
- Kroki mogą zawierać pod-scenariusze (dowolny poziom zagłębień)
- Kroki mogą się zaczynać od słów kluczowych: IF, ELSE, FOR EACH

#### Przykład:
##### Tytuł: Dodanie książki
##### Aktorzy:  Bibliotekarz
##### Aktor systemowy: System

- Bibliotekarz wybiera opcje dodania nowej pozycji książkowej
- Wyświetla się formularz.
- Bibliotekarz podaje dane książki.
- IF: Bibliotekarz pragnie dodać egzemplarze książki
    - Bibliotekarz wybiera opcję definiowania egzemplarzy
    - System prezentuje zdefiniowane egzemplarze
    - FOR EACH egzemplarz:
        - Bibliotekarz wybiera opcję dodania egzemplarza
        - System prosi o podanie danych egzemplarza
        - Bibliotekarz podaje dane egzemplarza i zatwierdza.
        - System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.
- Bibliotekarz zatwierdza dodanie książki.
- System informuje o poprawnym dodaniu książki.

# Retrospektywa Sprint 1 (21.11.2024 - 19.12.2024)

Na początku wystąpiły pewne problemy związane z ustaleniem wspólnego terminu realizacji poszczególnych zadań, jednak po podzieleniu pracy na poszczególne zadania, którę były realizowane w mniejszych grupach udało nam się osiągnąć zamierzone cele.

Co nam przeszkadzało w pracy:

Konieczność wykonywania wielu projektów na raz

Różne grafiki w pracach poszczególnych członków zespołu


### Zacząć:
1. Organizować cotygodniowe krótkie spotkania synchronizacyjne(Owocowe Czwartki).
### Przestać:
1. Przerywać pracę przez nieplanowane spotkania.
2. Zbyt długo rozmawiać o mało istotnych problemach
3. Zostawiać zadania nie będące przypisane do nikogo
### Kontynuować:
1. Wspieranie się nawzajem w rozwiązywaniu trudnych zadań.
2. Regularne aktualizowanie statusów w narzędziu do zarządzania projektami.
3. Nie zostawiać trudych zadań na ostatni tydzień sprintu.





import requests
import json

headers = {
    "Content-Type": "application/json",
}

with open("../InputData/sample_scenario_with_else.json", "r") as f:
    json_if_else = json.load(f)


def test_count_conditional_steps():
    expected = json.loads(
        '["Ilość kroków rozpoczynających się od słowa kluczowego: 3","Ilość decyzji warunkowych: 2"]'
    )
    response = requests.post(
        "http://localhost:8080/count-conditional-steps",
        headers=headers,
        data=json.dumps(json_if_else),
    )

    assert response.json() == expected


def test_parse_scenario():
    expected = json.loads(
        '["Tytuł: Dodanie książki","Aktorzy: Bibliotekarz, Testowy","Aktor systemowy: System","1. Bibliotekarz wybiera opcje dodania nowej pozycji książkowej.","2. Wyświetla się formularz.","3. Bibliotekarz podaje dane książki.","4. IF: Bibliotekarz pragnie dodać egzemplarze książki","\\t4.1. Bibliotekarz wybiera opcję definiowania egzemplarzy.","\\t4.2. System prezentuje zdefiniowane egzemplarze.","\\t4.3. FOR EACH: egzemplarz","\\t\\t4.3.1. Bibliotekarz wybiera opcję dodania egzemplarza.","\\t\\t4.3.2. System prosi o podanie danych egzemplarza.","\\t\\t4.3.3. Bibliotekarz podaje dane egzemplarza i zatwierdza.","\\t\\t4.3.4. System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.","\\t4.4. ELSE: Marek Marek","\\t\\t4.4.1. elo elo","\\t\\t4.4.2. maro maro","5. Bibliotekarz zatwierdza dodanie książki.","6. System informuje o poprawnym dodaniu książki."]'
    )
    response = requests.post(
        "http://localhost:8080/parse-scenario",
        headers=headers,
        data=json.dumps(json_if_else),
    )

    assert response.json() == expected


def test_count_steps():
    expected = json.loads(
        '["Liczba głównych kroków: 6","Liczba wszystkich kroków (w tym podkroków): 16"]'
    )
    response = requests.post(
        "http://localhost:8080/count-steps",
        headers=headers,
        data=json.dumps(json_if_else),
    )

    assert response.json() == expected


def test_steps_without_actors():
    expected = json.loads(
        '["Kroki bez aktora:","2. Wyświetla się formularz.","4.4.1. elo elo","4.4.2. maro maro"]'
    )

    response = requests.post(
        "http://localhost:8080/steps-without-actors",
        headers=headers,
        data=json.dumps(json_if_else),
    )

    assert response.json() == expected


def test_parse_static_scenario():
    expected = json_if_else

    response = requests.get(
        "http://localhost:8080/parse-static-scenario?filePath=InputData/sample_scenario_with_else.json"
    )

    assert response.json() == expected


if __name__ == "__main__":
    print("use: pytest test-api.py")
    test_count_conditional_steps()
    test_parse_scenario()
    test_count_steps()
    test_steps_without_actors()
    test_parse_static_scenario()
    print("All ok")

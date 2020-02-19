[PL]

Projekt na zaliczenie. Aplikacja przedstawia demonstracyjną wersję wirtualnego dziennika
na androida, korzystającą z lokalnie stworzonej bazy danych.

Przez pierwsze pięć sekund po włączeniu aplikacji, ukazuje się ekran powitania.
![img1](https://github.com/ZeroMaster28/Android-WirtualnyDziennikPL/blob/master/Images/1.png |width=50, height=50)
Potem użytkownik loguje się na swoje konto za pomocą unikalnie wygenerowanego klucza z Base64.

Istnieją trzy typy kont: admin, nauczyciel, uczeń.
Każda z ról ma ograniczony zakres dostępnych dla siebie funkcji:
Admin - może tworzyć i usuwać konta oraz przedmioty a także przypisywać nauczycieli do przedmiotów
Nauczyciel - może zapisywać uczniów na przedmioty które prowadzi i stawiać im oceny
Uczeń - Może przeglądąć swoje oceny 


Programowanie aplikacji internetowych - aplikacja dla komisji rekrutacyjnej

- wprowadzanie danych kandydatów - zrobione
- tworzenie komunikatów o np. egzaminach (komisja egzaminacyjna)
- drukowanie raportów

Pomysł na rozwiązanie drukowania raportów:
- Wykorzystanie jaspersoft (dependency jasperreports)
- Wykorzystanie parametru pesel dla stwierdzenie dla kogo drukować raport
- Jeden raport dla danej osoby, jeden raport ogólny

Dodatkowo kiedyś stare opisane objaśnienie kodu:

Objaśnienie projektu:

Candidate.java - model kandydatów

CandidateRepository.java - repozytorium dla kandydatów

CandidateController.java - niepotrzebne, do REST API

CandidateService.java - metody do obsługi kandydata takie jak dodanie, usunięcie, generowanie raportu i wyszukanie

InitService.java - tworzenie użytkowników na start

MainFrame.java - UI, przyciski wywołujące metody, debiloodpornność

application.properties - konfiguracja bazy danych

hibernate.cfg.xml - niepotrzebne

pom.xml - wszystkie zależności i importy

Żeby działało: 

Korzystałem z Intellij IDEA 2019.3, ale na innym raczej też zadziała oraz PostgreSQL

Połączenie z bazą danych trzeba zmienić w plikach application.properties oraz CandidateService.java

Należy też stworzyć bazę danych committeedb w pgAdmin

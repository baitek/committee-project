# Programowanie aplikacji internetowych - aplikacja dla komisji rekrutacyjnej

## Założenia projektowe

- wprowadzanie danych kandydatów - Bartosz Chajdas
- tworzenie komunikatów o rozmowie kwalifikacyjnej - Angelika Krysztoforska
- drukowanie raportów dla wszystkich oraz wybranego studenta - Kacper Duroł

## Proponowane rozwiązania

**Drukowanie raportów**

- Wykorzystanie Jaspersoft (dependency jasperreports jest już dodane)
- Stworzenie dwóch metod:
  - `generateReportAll()` - generuje raport dla wszystkich kandydatów
  - `generateReport(String name, String surname, Integer id)` - generuje raport dla pojedynczego kandydata o nazwie `<imie>.<nazwisko>.pdf`
- Dodanie nowych przycisków `Wygeneruj raport dla wszystkich` oraz `Wygeneruj raport dla zaznaczonego`. Pojedyncze zaznaczanie jest już ustawione.

**Tworzenie komunikatów**

- Dodanie przycisku aktywującym się w momencie zaznaczenia pojedynczego kandydata.
- Przycisk otwierałby nowe okno, które zawiera:
  1. pole z uzupełnionym mailem (nie aktywnym, nie możliwym do edycji). 
  2. pole do napisania treści maila
  3. przycisk wyślij, który wysyłałby maila z daną treścią oraz powiadamiał czy się to udało

package com.committee.ui;

import com.committee.model.Candidate;
import com.committee.service.CandidateServiceImpl;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI()
public class MainFrame extends UI {

    @Autowired
    private CandidateServiceImpl candidateServiceImpl;

    // TODO POPRAWIC WYGLAD UI
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        /**
         * GLOWNE OKNO
         */
        // GLOWNY KONTENER
        VerticalLayout mainRoot = new VerticalLayout();
        mainRoot.setSizeFull();

        // TABELA Z KANDYDATAMI
        Grid<Candidate> candidateGrid = new Grid<>(Candidate.class);
        candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
        candidateGrid.setSizeFull();

        // KONTENER Z PRZYCISKAMI
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setWidth("65%");
        btnLayout.setMargin(false);

        // USUNIECIE KANDYDATA
        Button candidateDelBtn = new Button("Usun kandydata");
        candidateDelBtn.addClickListener(e -> {
            if (candidateGrid.getSelectedItems().isEmpty()) {
                Notification.show("Wybierz kandydata ktorego chcesz usunac", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                // USUNIĘCIE WYBRANEGO KANDYDATA
                Candidate candidate = candidateGrid.getSelectedItems().iterator().next();
                candidateServiceImpl.deleteCandidate(candidate.getId());
                candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
            }
        });

        // GENEROWANIE RAPORTU DLA DANEGO KANDYDATA
        Button candidateGenBtn = new Button("Wygeneruj raport indywidualny");
        candidateGenBtn.addClickListener(e -> {
            if (candidateGrid.getSelectedItems().isEmpty()) {
                Notification.show("Wybierz kandydata ktorego raport chcesz wygenerować", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                // WYWOŁANIE GENEROWANIA RAPORTU DLA DANEGO KANDYDATA
                // TODO GENERACJA RAPORTU DLA DANEGO KANDYDATA
                Candidate candidate = candidateGrid.getSelectedItems().iterator().next();
                String name = candidate.getName();
                String surname = candidate.getSurname();
                Long id = candidate.getId();
                candidateServiceImpl.oneCandidateReport(name, surname, id);
            }
        });

        // GENEROWANIE RAPORTU WSZYSTKICH KANDYDATÓW
        Button candidateGenRap = new Button("Wygeneruj raport zbiorczy");
        candidateGenRap.addClickListener(e -> {
            candidateServiceImpl.allCandidatesReport();
        });


        /**
         * SUBWINDOW
         */

        // OKNO I GŁÓWNY KONTENER
        Window candidateAddWindow = new Window("Dodanie kandydata");
        VerticalLayout subRoot = new VerticalLayout();

        // TEXTFIELDY DO INSERTOW
        TextField nameTf = new TextField("Imie:");
        TextField surnameTf = new TextField("Nazwisko:");
        TextField peselTf = new TextField("Pesel:");
        TextField polishTf = new TextField("Wynik z polskiego (w %):");
        TextField mathTf = new TextField("Wynik z matematyki (w %):");
        TextField englishTf = new TextField("Wynik z angielskiego (w %):");
        TextField emailTf = new TextField("Email: ");

        // DODANIE KANDYDATA
        Button candidateAddBtn = new Button("Dodaj kandydata");
        candidateAddBtn.addClickListener(e -> {

            // PRZYPISANIE WARTOSCI PÓL DO ZMIENNYCH
            String name = nameTf.getValue().trim();
            String surname = surnameTf.getValue().trim();
            String pesel = peselTf.getValue().trim();
            Integer polish = Integer.valueOf(polishTf.getValue());
            Integer math = Integer.valueOf(mathTf.getValue());
            Integer english = Integer.valueOf(englishTf.getValue());
            String email = emailTf.getValue().trim();

            // TODO OBSLUZENIE PRZYPADKU GDY PESEL JEST TEN SAM
            if (!(checkScores(polish, math, english))) { // WYNIKI SPRAWDZENIE
                Notification.show("Bledne wyniki", Notification.Type.HUMANIZED_MESSAGE);
            } else if (pesel.length() != 11) { // PESEL SPRAWDZENIE
                Notification.show("Bledny pesel", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                // DODANIE KANDYDATA
                Candidate candidate = new Candidate();
                candidate.setName(name);
                candidate.setSurname(surname);
                candidate.setPesel(pesel);
                candidate.setPolish(polish);
                candidate.setMath(math);
                candidate.setEnglish(english);
                candidate.setEmail(email);
                candidateServiceImpl.addCandidate(candidate);

                // WYCZYSZCZENIE PÓL
                nameTf.setValue("");
                surnameTf.setValue("");
                peselTf.setValue("");
                polishTf.setValue("");
                mathTf.setValue("");
                englishTf.setValue("");
                emailTf.setValue("");

                // AKTUALIZACJA TABELI
                candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
            }
        });

        // USTAWIENIA OKNA I DODANIE KOMPONENTÓW
        subRoot.addComponents(nameTf, surnameTf, peselTf, polishTf, mathTf, englishTf, emailTf, candidateAddBtn);
        candidateAddWindow.setContent(subRoot);
        candidateAddWindow.center();
        candidateAddWindow.setModal(true);

        // PRZYCISK OTWIERAJACY SUBWINDOW
        Button candidateAddWindowBtn = new Button("Dodaj kandydata");
        candidateAddWindowBtn.addClickListener(e -> {
            addWindow(candidateAddWindow);
        });

        // DODANIE KOMPONENTOW DO KONTENEROW I GLOWNEGO OKNA
        btnLayout.addComponents(candidateAddWindowBtn, candidateDelBtn, candidateGenBtn, candidateGenRap);
        mainRoot.addComponents(candidateGrid, btnLayout);
        setContent(mainRoot);
    }

    // SPRAWDZENIE WYNIKOW
    private boolean checkScores(Integer... values) {
        for (Integer i : values) {
            if (i > 100 || i < 0) {
                return false;
            }
        }
        return true;
    }
}

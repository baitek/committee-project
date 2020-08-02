package com.committee.ui;

import com.committee.model.Candidate;
import com.committee.service.CandidateServiceImpl;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@SpringUI()
public class MainFrame extends UI {

    @Autowired
    private CandidateServiceImpl candidateServiceImpl;

    private String toEmail;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        /**
         * GLÓWNE OKNO
         */
        // GLOWNY KONTENER
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();

        // TABELA Z KANDYDATAMI
        Grid<Candidate> candidateGrid = new Grid<>(Candidate.class);
        candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
        candidateGrid.setSizeFull();

        // KONTENER Z PRZYCISKAMI
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setMargin(false);

        // USUNIECIE KANDYDATA
        Button candidateDelBtn = new Button("Usun kandydata");
        candidateDelBtn.addClickListener(e -> {
            if (candidateGrid.getSelectedItems().isEmpty()) {
                Notification.show("Wybierz kandydata ktorego chcesz usunac", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Candidate candidate = candidateGrid.getSelectedItems().iterator().next();
                candidateServiceImpl.deleteCandidate(candidate.getId());
                candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
            }
        });

        // GENEROWANIE RAPORTU DLA DANEGO KANDYDATA
        Button candidateGenBtn = new Button("Wygeneruj raport indywidualny");
        candidateGenBtn.addClickListener(e -> {
            if (candidateGrid.getSelectedItems().isEmpty()) {
                Notification.show("Wybierz kandydata dla ktorego raport chcesz wygenerować", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Candidate candidate = candidateGrid.getSelectedItems().iterator().next();
                String name = candidate.getName();
                String surname = candidate.getSurname();
                Long id = candidate.getId();
                candidateServiceImpl.oneCandidateReport(name, surname, id);
                Notification.show("Wygenerowano raport dla " + name + " " + surname, Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        // GENEROWANIE RAPORTU WSZYSTKICH KANDYDATÓW
        Button candidatesGenBtn = new Button("Wygeneruj raport zbiorczy");
        candidatesGenBtn.addClickListener(e -> {
            candidateServiceImpl.allCandidatesReport();
            Notification.show("Wygenerowano raport zbiorczy", Notification.Type.HUMANIZED_MESSAGE);
        });

        /**
         * SUBWINDOW ADD CANDIDATE
         */
        Window candidateAddWindow = new Window("Dodanie kandydata");
        VerticalLayout candidateAddLayout = new VerticalLayout();
        candidateAddWindow.setContent(candidateAddLayout);
        candidateAddWindow.center();
        candidateAddWindow.setModal(true);

        // POLA TEKSTOWE DO WYPEŁNIENIA
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
            String name = nameTf.getValue().trim();
            String surname = surnameTf.getValue().trim();
            String pesel = peselTf.getValue().trim();
            Integer polish = Integer.valueOf(polishTf.getValue());
            Integer math = Integer.valueOf(mathTf.getValue());
            Integer english = Integer.valueOf(englishTf.getValue());
            String email = emailTf.getValue().trim();

            if (!(checkScores(polish, math, english))) {
                Notification.show("Bledne wyniki", Notification.Type.HUMANIZED_MESSAGE);
            } else if (pesel.length() != 11) {
                Notification.show("Bledny pesel", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Candidate candidate = new Candidate();
                candidate.setName(name);
                candidate.setSurname(surname);
                candidate.setPesel(pesel);
                candidate.setPolish(polish);
                candidate.setMath(math);
                candidate.setEnglish(english);
                candidate.setEmail(email);
                candidateServiceImpl.addCandidate(candidate);

                nameTf.setValue("");
                surnameTf.setValue("");
                peselTf.setValue("");
                polishTf.setValue("");
                mathTf.setValue("");
                englishTf.setValue("");
                emailTf.setValue("");

                candidateGrid.setItems(candidateServiceImpl.getAllCandidates());
            }
        });

        candidateAddLayout.addComponents(nameTf, surnameTf, peselTf, polishTf, mathTf, englishTf, emailTf, candidateAddBtn);

        Button candidateAddWindowBtn = new Button("Dodaj kandydata");
        candidateAddWindowBtn.addClickListener(e -> {
            addWindow(candidateAddWindow);
        });

        /**
         * SUBWINDOW NOTIFY CANDIDATE
         */
        // OKNO I GŁÓWNY KONTENER
        Window candidateNotifyWindow = new Window("Powiadomienie kandydata");
        VerticalLayout candidateNotifyLayout = new VerticalLayout();
        candidateNotifyWindow.setContent(candidateNotifyLayout);
        candidateNotifyWindow.center();
        candidateNotifyWindow.setModal(true);

        TextField toEmailTf = new TextField("To: ");
        toEmailTf.setEnabled(false);

        TextField subjectTf = new TextField("Tytuł: ");

        TextField textTf = new TextField("Tekst: ");

        Button sendEmailBtn = new Button("Wyślij wiadomość");
        sendEmailBtn.addClickListener(e -> {
            if(subjectTf.getValue().isEmpty()) {
                Notification.show("Proszę uzupełnić temat maila", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                candidateServiceImpl.notifyCandidate(toEmail, subjectTf.getValue(), textTf.getValue());

                Notification.show("Mail został wysłany", Notification.Type.HUMANIZED_MESSAGE);
            }
        });

        candidateNotifyLayout.addComponents(toEmailTf, subjectTf, textTf, sendEmailBtn);

        Button candidateNotifyWindowBtn = new Button("Powiadom kandydata");
        candidateNotifyWindowBtn.addClickListener(e -> {
            if (candidateGrid.getSelectedItems().isEmpty()) {
                Notification.show("Wybierz kandydata którego chcesz powiadomić", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                Candidate candidate = candidateGrid.getSelectedItems().iterator().next();
                toEmail = candidate.getEmail().trim();
                toEmailTf.setValue(toEmail);
                addWindow(candidateNotifyWindow);
            }
        });

        // DODANIE KOMPONENTOW DO KONTENEROW I GLOWNEGO OKNA
        btnLayout.addComponents(candidateAddWindowBtn, candidateDelBtn, candidateGenBtn, candidatesGenBtn, candidateNotifyWindowBtn);
        mainLayout.addComponents(candidateGrid, btnLayout);
        setContent(mainLayout);
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

package com.example.application.views.about;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("О проекте")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        add(new H2("Конфигуратор для сборки ПК"));
        add(new Paragraph("Разработал в качестве курсовой работы студент ЯГТУ группы ЭПИ-21"));

        Div infoOfDevelopers = new Div();
        infoOfDevelopers.setClassName("info-of-developers");
        infoOfDevelopers.setHeight("50px");
        infoOfDevelopers.setText("Кузьменко Илья Максимович");

        Anchor githubPage = new Anchor("https://github.com/eljke", "github.com/eljke");
        githubPage.setClassName("github-page");
        githubPage.setHeight("50px");
        githubPage.setTarget("blank");

        add(infoOfDevelopers, githubPage);

        HorizontalLayout pepeHolder = new HorizontalLayout();
        pepeHolder.setClassName("pepe-holder");

        Image pepe1 = new Image("images/pepe-clown.gif", "clown");
        pepe1.setWidth("250px");

        pepeHolder.add(pepe1);

        add(pepeHolder);

        Footer footer = new Footer();
        footer.addClassName("footer");
        footer.add("2022");

        add(footer);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}

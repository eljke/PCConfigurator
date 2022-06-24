package com.example.application.views.about;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.security.GeneralSecurityException;

@PageTitle("О проекте")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        add(new H2("Конфигуратор для сборки ПК"));

        Anchor developer = new Anchor("https://t.me/eljke_ai", "Разработал @eljke_ai");
        developer.setClassName("developer");
        developer.setTarget("blank");

        add(developer);

        Anchor githubPage = new Anchor("https://github.com/eljke/PCConfigurator", "Репозиторий проекта на GitHub");
        githubPage.setClassName("github-page");
        githubPage.setHeight("50px");
        githubPage.setTarget("blank");

        add(githubPage);

        HorizontalLayout kittenHolder = new HorizontalLayout();
        kittenHolder.setClassName("kitten-holder");

        Image cuteKitten = new Image("images/cuteKitten.gif", "Cute Kitten");
        cuteKitten.setWidth("350px");

        kittenHolder.add(cuteKitten);

        add(kittenHolder);

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

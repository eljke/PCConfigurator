package com.example.application.views.saves;

import com.example.application.views.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@PageTitle("Сохранения")
@Route(value = "saves", layout = MainLayout.class)
public class SavesView extends VerticalLayout {

    public SavesView(){
        setSpacing(false);
        addClassName("saves-view");

        Div savesTitle = new Div();
        savesTitle.addClassName("container");
        savesTitle.setText("Мои сохранения:");
        add(savesTitle);

        Div empty = new Div();
        empty.addClassName("empty");
        empty.setText("К сожалению, здесь пусто ☹️");
        empty.setVisible(true);
        add(empty);

        File docs = new File("src/main/resources/documents");

        int filesNumber = Objects.requireNonNull(docs.list()).length;

        if (filesNumber > 0) {
            empty.setVisible(false);
        }

        List<Path> fileNames = new ArrayList<>();
        try {
            fileNames = Files.walk(Paths.get("src/main/resources/documents"))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e){
            e.printStackTrace();
        }

        for (int i = 1; i <= filesNumber; i++) {

            String currentFileName = String.valueOf(fileNames.get(i-1))
                    .replace("src\\main\\resources\\documents\\", "");

            StreamResource streamResource = new StreamResource(String.format("%s", currentFileName),
                    () -> getClass().getResourceAsStream(String.format("/documents/%s", currentFileName)));

            Anchor anchor = new Anchor(streamResource, String.format("%s) %s", i, currentFileName));
            anchor.addClassName(String.format("%s", currentFileName));
            anchor.getElement().setAttribute("download", String.format("%s", currentFileName));

            add(anchor);
        }

        setSizeFull();
    }
}

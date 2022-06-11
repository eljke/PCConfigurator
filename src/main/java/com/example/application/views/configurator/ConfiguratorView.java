package com.example.application.views.configurator;

import com.example.application.special.Component;
import com.example.application.special.Parser;
import com.example.application.views.MainLayout;
import com.example.application.views.saves.SavesView;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@PageTitle("Конфигуратор")
@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "configurator", layout = MainLayout.class)
public class ConfiguratorView extends Div implements AfterNavigationObserver {

    private final Configuration configuration = new Configuration();

    private final Grid<PCComponent> grid = new Grid<>();

    @Value("${PARSING_SOURCE}")
    private String PARSING_SOURCE;

    private final Button saveScreenButton = new Button();

    private final TextField enterConfigurationName = new TextField();

    private final Button saveEnteredConfigurationName = new Button();

    private final Span totalCostLower = new Span();

    private final Span totalCostUpper = new Span();

    private String curName = "";

    private int modelCardIndex;

    private final Div filtersContainer = new Div();

    private final TextField search = new TextField();

    private final Button searchKey = new Button("ОК");

    private int countForSavingComponents = 0;

    private int totalLowerCost = 0;

    private List<Component> componentsList;

    private final Map<String, String> savedComponents = new HashMap<>();

    private final Map<String, String> sockets = new HashMap<>();

    private final Icon successIcon = new Icon(VaadinIcon.CHECK);

    private final Icon errorIcon = new Icon(VaadinIcon.CLOSE);
    private final Notification notification = new Notification();

    private String notificationMessage = "";

    private static final String FONT = "src\\main\\java\\com\\example\\application\\assets\\fonts\\arial.ttf";

    public ConfiguratorView(){
        addClassName("configurator-view");
        setSizeFull();
        grid.setHeight("95%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCardComponent);

        filtersContainer.addClassName("filtersContainer");

        search.addClassName("search");
        search.getElement().setAttribute("aria-label", "search");
        search.setPlaceholder("Поиск");
        search.setClearButtonVisible(true);
        search.setPrefixComponent(VaadinIcon.SEARCH.create());

        searchKey.addClickShortcut(Key.ENTER);

        MenuBar sortMenuBar = new MenuBar();
        sortMenuBar.setOpenOnHover(true);
        addItems(sortMenuBar);

        filtersContainer.add(sortMenuBar, search, searchKey);

        Div saveScreenButtonContainer = new Div();
        saveScreenButtonContainer.addClassName("saveScreenButtonContainer");
        enterConfigurationName.addClassName("enterConfigurationName");
        enterConfigurationName.setPlaceholder("Название");
        enterConfigurationName.setVisible(false);
        saveEnteredConfigurationName.addClassName("saveEnteredConfigurationName");
        saveEnteredConfigurationName.setText("OK");
        saveEnteredConfigurationName.setVisible(false);

        saveScreenButtonContainer.add(saveScreenButton, enterConfigurationName, saveEnteredConfigurationName);

        Span totalCostTitle1 = new Span("Итоговая стоимость:");
        totalCostTitle1.addClassName("totalCostTitle");

        Span totalCost = new Span();
        totalCost.addClassName("totalCost");

        totalCostLower.addClassName("totalCostLower");
        totalCost.add(totalCostLower);

        totalCostUpper.addClassName("totalCostUpper");
        totalCost.add(totalCostUpper);

        Div savesContainer = new Div();
        savesContainer.addClassName("savesContainer");
        Button getSavesButton = new Button();
        savesContainer.add(saveScreenButtonContainer, getSavesButton);

        getSavesButton.addClassName("getSavesButton");
        getSavesButton.setText("Мои сохранения");
        getSavesButton.addClickListener(clickEvent -> UI.getCurrent().navigate(SavesView.class));

        saveScreenButton.addClassName("saveScreenButton");
        saveScreenButton.setText("Сохранить в PDF");

        saveScreenButton.addClickListener(clickEvent -> {
            enterConfigurationName.setVisible(true);
            saveEnteredConfigurationName.setVisible(true);
            saveScreenButton.setVisible(false);
        });

        saveEnteredConfigurationName.addClickListener(clickEvent -> {

            configuration.incCount();

            Document document = new Document();

            try {

                FileOutputStream fos = new FileOutputStream(
                        String.format("src/main/resources/documents/%s.pdf", enterConfigurationName.getValue()));
                PdfWriter.getInstance(document, fos);
                document.open();
                BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font font = new Font(bf,14,Font.NORMAL);
                com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

                for (int i = 0; i < savedComponents.size() / 6; i++) {

                    com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(savedComponents.get(String.format("image%s", i)));

                    image.scalePercent(20);

                    Font titleFont = new Font(font);
                    titleFont.setStyle(Font.BOLD);
                    titleFont.setSize(16);

                    Paragraph title = new Paragraph(savedComponents.get(String.format("title%s", i)), titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    Chunk imageChunk = new Chunk(image, -5, -10, true);
                    document.add(imageChunk);

                    Chunk nameChunk = new Chunk(savedComponents.get(String.format("name%s", i)), font);
                    nameChunk.setAnchor(savedComponents.get(String.format("url%s", i)));
                    document.add(nameChunk);

                    Paragraph descriptionChunk = new Paragraph(savedComponents.get(String.format("description%s", i)), font);
                    document.add(descriptionChunk);

                    Paragraph priceChunk = new Paragraph(savedComponents.get(String.format("price%s", i)) + "\n\n", font);
                    document.add(priceChunk);

                }

                document.add(list);

                font.setStyle(Font.UNDERLINE | Font.ITALIC);

                Paragraph totalCostTitle = new Paragraph("Итоговая цена:", font);

                document.add(totalCostTitle);

                font.setStyle(Font.ITALIC);

                Paragraph totalCostValue = new Paragraph(totalCostLower.getText()
                        + totalCostUpper.getText(), font);

                document.add(totalCostValue);

                document.close();

            } catch (DocumentException | IOException ex) {
                ex.printStackTrace();
            }

            saveScreenButton.setVisible(true);
            enterConfigurationName.setVisible(false);
            enterConfigurationName.clear();
            saveEnteredConfigurationName.setVisible(false);

        });

        HorizontalLayout gridFooter = new HorizontalLayout();
        gridFooter.setClassName("gridFooter");


        successIcon.setVisible(false);
        successIcon.setClassName("successIcon");

        errorIcon.setVisible(false);
        errorIcon.setClassName("errorIcon");

        Span title = new Span("Совместимость: ");
        title.addClassName("compatibilityTitle");

        HorizontalLayout compatibility = new HorizontalLayout();
        compatibility.setClassName("compatibility");

        compatibility.add(title, successIcon, errorIcon);

        HorizontalLayout costHolder = new HorizontalLayout();
        costHolder.addClassNames("costHolder", "border");
        costHolder.add(totalCostTitle1, totalCost);

        gridFooter.add(compatibility, savesContainer, costHolder);

        add(grid);
        add(gridFooter);
    }

    private void addItems(MenuBar menuBar) {

        MenuItem sort = menuBar.addItem("Сортировка");
        SubMenu sortSubMenu = sort.getSubMenu();

        MenuItem popularity = sortSubMenu.addItem("популярность");
        MenuItem price = sortSubMenu.addItem("цена");
        MenuItem name = sortSubMenu.addItem("название");

        MenuItem order = menuBar.addItem("Порядок");
        SubMenu orderSubMenu = order.getSubMenu();

        MenuItem asc = orderSubMenu.addItem("по возрастанию");
        MenuItem desc = orderSubMenu.addItem("по убыванию");
    }

    boolean counter = false;


    private VerticalLayout createCardComponent(PCComponent pcComponent) {
        VerticalLayout cardComponent = new VerticalLayout();
        cardComponent.addClassName("card");
        cardComponent.setSpacing(false);
        cardComponent.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(pcComponent.getImage());

        Div imageHolder = new Div(image);
        imageHolder.addClassName("image");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(pcComponent.getName());
        name.addClassName("name");
        Span needful = new Span(pcComponent.getNeedful());
        needful.addClassName("needful");
        header.add(name, needful);

        Span post = new Span(pcComponent.getPost());
        post.addClassName("post");

        HorizontalLayout shortInfo = new HorizontalLayout();
        shortInfo.addClassName("shortInfo");
        shortInfo.setWidth(cardComponent.getWidth());
        shortInfo.setSpacing(false);
        shortInfo.getThemeList().add("spacing-s");

        VerticalLayout scrollPlace = new VerticalLayout();
        scrollPlace.addClassName("scrollPlace");
        scrollPlace.setSpacing(false);
        scrollPlace.setPadding(false);
        scrollPlace.getThemeList().add("spacing-s");

        VerticalLayout selectMenu = new VerticalLayout();
        selectMenu.addClassName("selectMenu");
        selectMenu.setWidth(shortInfo.getWidth());
        selectMenu.setHeight("800px");
        selectMenu.setSpacing(false);
        selectMenu.getThemeList().add("spacing-s");

        Scroller scroller = new Scroller(new Div(selectMenu));
        scroller.addClassName("scroller");
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);

        final boolean[] parseCounter = {false};

        shortInfo.addClickListener(e -> {

            if (!curName.equals(name.getText())){
                curName = name.getText();
                counter = false;
            }

            curName = name.getText();

            if (!counter) {

                if (!parseCounter[0]){
                    try {
                        Parser parser = new Parser();
                        parser.parse(curName, PARSING_SOURCE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    componentsList = Parser.getComponentsList();

                    for (int i = 0; i < componentsList.size(); i++) {

                        HorizontalLayout modelCard = new HorizontalLayout();
                        modelCard.addClassName("modelCard");
                        modelCard.setWidth(selectMenu.getWidth());
                        modelCard.setSpacing(false);
                        modelCard.getThemeList().add("spacing-s");

                        VerticalLayout modelInfo = new VerticalLayout();
                        modelInfo.setClassName("modelInfo");
                        modelInfo.setSpacing(false);
                        modelInfo.getThemeList().add("spacing-s");

                        HorizontalLayout modelInfoFooter = new HorizontalLayout();
                        modelInfoFooter.addClassName("modelInfoFooter");
                        modelInfoFooter.setSpacing(false);
                        modelInfoFooter.getThemeList().add("spacing-s");

                        Component curComponent = componentsList.get(i);
                        Anchor model = new Anchor(componentsList.get(i).getUrl());
                        model.addClassName(String.format("model%s", i));
                        model.setText(curComponent.getName());
                        model.setTarget("blank");

                        Span price = new Span(curComponent.getPrice());
                        price.addClassName("price");

                        Div shortDescription = new Div();
                        shortDescription.addClassName("shortDescription");
                        shortDescription.setText(curComponent.getDescription());

                        Image componentIMG = new Image();
                        componentIMG.setSrc(curComponent.getImage());
                        componentIMG.addClassName("modelImage");

                        Div modelImageHolder = new Div();
                        modelImageHolder.addClassName("modelImageHolder");
                        modelImageHolder.add(componentIMG);

                        Button addModelButton = new Button();
                        addModelButton.addClassName("addModelButton");
                        addModelButton.setText("Добавить в комплект");

                        Icon success = new Icon(VaadinIcon.CHECK);
                        success.addClassName("successIcon");
                        success.setColor("--lumo-success-color");
                        success.setVisible(false);

                        Icon error = new Icon(VaadinIcon.CLOSE);
                        error.addClassName("errorIcon");
                        error.setColor("--lumo-error-color");
                        error.setVisible(false);

                        Div buttonAndStatus = new Div();
                        buttonAndStatus.addClassName("buttonAndStatus");
                        buttonAndStatus.add(addModelButton, success, error);

                        Button removeModelButton = new Button();
                        removeModelButton.addClassName("removeModelButton");
                        removeModelButton.setText("Удалить из комплекта");

                        addModelButton.addClickListener(event -> {
                            modelCardIndex = selectMenu.indexOf(modelCard);
                            success.setVisible(false);
                            error.setVisible(false);
                            cardComponent.remove(shortInfo, scrollPlace);
                            buttonAndStatus.removeAll();
                            buttonAndStatus.add(removeModelButton, success, error);
                            modelInfoFooter.add(price, buttonAndStatus);
                            modelInfo.add(model, shortDescription, modelInfoFooter);
                            modelCard.add(modelImageHolder, modelInfo);
                            cardComponent.add(modelCard);

                            if (Objects.equals(pcComponent.getName(), "Процессор") ||
                                    Objects.equals(pcComponent.getName(), "Материнская плата") ||
                                    Objects.equals(pcComponent.getName(), "Корпус") ||
                                    Objects.equals(pcComponent.getName(), "Блок питания") ||
                                    Objects.equals(pcComponent.getName(), "Видеокарта") ||
                                    Objects.equals(pcComponent.getName(), "Оперативная память")){

                                String[] paramFinderArray;
                                paramFinderArray = shortDescription.getText().split("\n");

                                for (String s : paramFinderArray) {

                                    if (s.contains("Socket")) {
                                        String socket = s.replaceAll("Socket: ", "");
                                        sockets.put(pcComponent.getName() + " сокет", socket);
                                    } else if (s.contains("Форм-фактор")) {
                                        String formfactor = s.replaceAll("Форм-фактор: ", "");
                                        sockets.put(pcComponent.getName() + " форм-фактор", formfactor);
                                    } else if (s.contains("Установка платы")) {
                                        String slot = s.replaceAll("Установка платы: ", "");
                                        sockets.put(pcComponent.getName() + " установка платы", slot);
                                    } else if (s.contains("TDP")) {
                                        String tdp = s.replaceAll("TDP: ", "");
                                        sockets.put(pcComponent.getName() + " TDP", tdp);
                                    } else if (s.contains("Мощность")) {
                                        String power = s.replaceAll("Мощность: ", "");
                                        sockets.put(pcComponent.getName() + " мощность", power);
                                    } else if (s.contains("потребление")) {
                                        String lastSymbols = s.substring(s.length() - 6);
                                        System.out.println(lastSymbols);
                                        sockets.put(pcComponent.getName() + " потребление", lastSymbols);

                                    } else if (s.contains("Тип")) {
                                        String type = s.replaceAll("Тип: ", "");
                                        sockets.put(pcComponent.getName() + " тип", type);
                                    } else if (s.contains("Слоты памяти:")) {
                                        String type = s.replaceAll("2 х |4 х |Слоты памяти: |, |\\d+ МГц", "");
                                        sockets.put(pcComponent.getName() + " тип памяти", type);
                                    }

                                }

                                if (sockets.size() > 0) {
                                    System.out.println(Objects.equals(sockets.get("Корпус установка платы"),
                                            sockets.get("Материнская плата форм-фактор")));

                            /*  1) Сокет процессора и материнской платы
                                2) Форм-фактор корпуса и материнской платы
                                3) Мощность БП и видеокарты
                            */
                                    try {

                                        notification.removeAll();
                                        successIcon.setVisible(true);
                                        errorIcon.setVisible(false);

                                        if (!(Objects.equals(sockets.get("Процессор сокет"),
                                                sockets.get("Материнская плата сокет"))) &&
                                                sockets.containsKey("Процессор сокет") &&
                                                sockets.containsKey("Материнская плата сокет")) {
                                            setIncompatibilityMessage("⚠ Сокеты процессора и мат. платы не совпадают!");
                                        }

                                        if (!(Integer.parseInt(sockets.get("Процессор TDP").replaceAll(" Вт", "")) +
                                                Integer.parseInt(sockets.get("Видеокарта потребление").replaceAll("Вт", "")
                                                        .replaceAll(" ", "")) <=
                                                Integer.parseInt(sockets.get("Блок питания мощность").replaceAll(" Вт", ""))) &&
                                                sockets.containsKey("Процессор TDP") &&
                                                sockets.containsKey("Видеокарта потребление") &&
                                                sockets.containsKey("Блок питания мощность")){
                                            setIncompatibilityMessage("⚠ Мощности блока питания не хватает!");
                                        }

                                    } catch (NullPointerException ignored) {}

                                }
                            }

                            savedComponents.put(String.format("image%s", countForSavingComponents), curComponent.getImage());
                            savedComponents.put(String.format("name%s", countForSavingComponents), curComponent.getName());
                            savedComponents.put(String.format("description%s", countForSavingComponents), curComponent.getDescription());
                            savedComponents.put(String.format("price%s", countForSavingComponents), curComponent.getPrice());
                            savedComponents.put(String.format("url%s", countForSavingComponents), curComponent.getUrl());
                            savedComponents.put(String.format("title%s", countForSavingComponents), pcComponent.getName());

                            String lowerRange = "";

                            if (price.getText().contains("до")) {
                                Pattern pattern = Pattern.compile("от ([\\d ]+)");
                                Matcher matcher = pattern.matcher(price.getText());

                                if (matcher.find()) {
                                    lowerRange = matcher.group(1).replaceAll(" ", "");
                                }

                                totalLowerCost += Integer.parseInt(lowerRange);

                                totalCostLower.setText("от " + totalLowerCost + " тг");
                            }
                            else{
                                totalLowerCost += Integer.parseInt(price.getText().replaceAll("от ", "")
                                        .replaceAll(" ", "").replaceAll("тг.", ""));

                                totalCostLower.setText("от " + totalLowerCost + " тг");
                            }

                            countForSavingComponents += 1;

                        });

                        removeModelButton.addClickListener( event -> {
                            selectMenu.addComponentAsFirst(filtersContainer);
                            success.setVisible(false);
                            error.setVisible(false);
                            buttonAndStatus.removeAll();
                            buttonAndStatus.add(addModelButton, success, error);
                            cardComponent.remove(modelCard);
                            cardComponent.add(shortInfo, scrollPlace);
                            selectMenu.addComponentAtIndex(modelCardIndex, modelCard);

                            savedComponents.remove(String.format("image%s", countForSavingComponents), curComponent.getImage());
                            savedComponents.remove(String.format("name%s", countForSavingComponents), curComponent.getName());
                            savedComponents.remove(String.format("description%s", countForSavingComponents), curComponent.getDescription());
                            savedComponents.remove(String.format("price%s", countForSavingComponents), curComponent.getPrice());
                            savedComponents.remove(String.format("url%s", countForSavingComponents), curComponent.getUrl());
                            savedComponents.remove(String.format("title%s", countForSavingComponents), pcComponent.getName());

                            String lowerRange = "";

                            if (price.getText().contains("до")) {
                                Pattern pattern = Pattern.compile("от ([0-9 ]+)");
                                Matcher matcher = pattern.matcher(price.getText());

                                if (matcher.find()) {
                                    lowerRange = matcher.group(1).replaceAll(" ", "");
                                }

                                totalLowerCost -= Integer.parseInt(lowerRange);
                            }
                            else{
                                totalLowerCost -= Integer.parseInt(price.getText().replaceAll("от ", "")
                                        .replaceAll(" ", "").replaceAll("тг.", ""));
                            }

                            totalCostLower.setText("от " + totalLowerCost);

                            countForSavingComponents -= 1;

                        });

                        modelInfoFooter.add(price, buttonAndStatus);
                        modelInfo.add(model, shortDescription, modelInfoFooter);
                        modelCard.add(modelImageHolder, modelInfo);
                        selectMenu.add(modelCard);

                        searchKey.addClickListener(event -> {
                            String searchValue = search.getValue().toLowerCase();
                            String[] searchValueArray = searchValue.split(" ");
                            for (int j = 1; j < selectMenu.getComponentCount(); j++){
                                HorizontalLayout currentComponent = (HorizontalLayout) selectMenu.getComponentAt(j);
                                String currentComponentName = currentComponent.getElement().getChild(1).getChild(0)
                                        .getText().toLowerCase();
                                String[] currentComponentNameArray = currentComponentName.split(" ");

                                if (searchValue.isEmpty()){
                                    currentComponent.setVisible(true);
                                }

                                currentComponent.setVisible(containsAll(searchValueArray, currentComponentNameArray));
                            }
                        });
                    }

                    Parser.deleteComponentsList();
                    parseCounter[0] = true;
                }

                scrollPlace.add(scroller);
                counter = true;
            }

            else {
                scrollPlace.remove(scroller);
                counter = false;
            }

            selectMenu.addComponentAsFirst(filtersContainer);

        });

        description.add(header, post);
        shortInfo.add(imageHolder,description);
        cardComponent.add(shortInfo, scrollPlace);
        return cardComponent;
    }

    private void setIncompatibilityMessage(String notificationMsg) {
        notificationMessage = "";
        notificationMessage += notificationMsg;
        successIcon.setVisible(false);
        errorIcon.setVisible(true);
        errorIcon.addClickListener(iconClickEvent -> {
            notification.setText(notificationMessage);
            notification.setDuration(5000);
            notification.open();
        });
    }

    public String getCurName() {
        return curName;
    }

    static List<PCComponent> pcComponents = new ArrayList<>();

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        pcComponents = Arrays.asList(
                createPCComponent("images/processor.png",
                        "Процессор",
                        "*Обязательно",
                        "Компонент, отвечающий за математические вычисления и обработку команд"),
                createPCComponent("images/motherboard.png",
                        "Материнская плата",
                        "*Обязательно",
                        "Это основная плата компьютера, на которой расположены слоты и разъемы для подключения комплектующих ПК"),
                createPCComponent("images/case.png",
                        "Корпус",
                        "*Обязательно",
                        "Это базовая несущая конструкция, которая предназначена для последующего наполнения аппаратным обеспечением"),
                createPCComponent("images/graphics card.png",
                        "Видеокарта",
                        "",
                        "Компонент, отвечающий за обработку видеоданных и вывод на монитор изображения."),
                createPCComponent("images/cpu cooler.png",
                        "Охлаждение процессора",
                        "*Обязательно",
                        "Компонент, отвечающий за пассивное воздушное/водяное охлаждение центрального процессора"),
                createPCComponent("images/ram.png",
                        "Оперативная память",
                        "*Обязательно",
                        "Память с произвольным доступом для временного хранения данных, обеспечивая работу программного обеспечения ПК"),
                createPCComponent("images/hdd.png",
                        "HDD",
                        "*Обязательно",
                        "Это запоминающее устройство для хранения ваших файлов и данных в течение длительного времени"),
                createPCComponent("images/hdd.png",
                        "SSD",
                        "*Обязательно",
                        "Это более быстрое запоминающее устройство для хранения ваших файлов и данных в течение длительного времени"),
                createPCComponent("images/power supply.png",
                        "Блок питания",
                        "*Обязательно",
                        "Источник электропитания, позволяющий обеспечить электроэнергией компоненты ПК"),
                createPCComponent("images/sound card.png",
                        "Звуковая карта",
                        "",
                        "Дополнительное оборудование ПК, позволяющее обрабатывать звук")
        );

        grid.setItems(pcComponents);
    }

    private static PCComponent createPCComponent(String image, String name, String needful, String post ) {
        PCComponent pcComponent = new PCComponent();
        pcComponent.setImage(image);
        pcComponent.setName(name);
        pcComponent.setNeedful(needful);
        pcComponent.setPost(post);

        return pcComponent;
    }

    private static boolean containsAll(String[] substr, String[] str) {
        String converted = Arrays.toString(str);
        for (String s : substr) {
            if (!converted.contains(s)) {
                return false;
            }
        }
        return true;
    }



}
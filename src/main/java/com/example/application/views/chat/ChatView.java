package com.example.application.views.chat;

import com.example.application.special.Bot;
import com.example.application.special.Encryption;
import com.example.application.database.controller.MessagesHistoryController;
import com.example.application.database.controller.PasswordSecretkeysController;
import com.example.application.database.controller.UserloginsController;
import com.example.application.database.model.MessagesHistory;
import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.model.Userlogins;
import com.example.application.views.MainLayout;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@PageTitle("Чат")
@Route(value = "chat", layout = MainLayout.class)
public class ChatView extends VerticalLayout {

    @Value("${BOT_TOKEN}")
    private String BOT_TOKEN;
    @Value("${CHAT_ID}")
    private String CHAT_ID;
    private String userName;
    private String password;
    private final Icon checkIconForUsername;
    private final Icon checkIconForEmail;
    private final Icon checkIconForPassword;
    private final Span passwordStrengthText;

    private final UserInfo userInfo = new UserInfo(UUID.randomUUID().toString());
    private final CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#Общий");

    private final Span messageMenu = new Span();

    private final Button sendButton = new Button("Отправить");

    private final TextField sendField = new TextField();

    public ChatView() {
        addClassName("chat-view");
        setSpacing(false);
        sendButton.addClassName("send-button");
        sendField.addClassName("send-field");
        sendField.setPlaceholder("Сообщение");

        messageMenu.add(sendField, sendButton);

        sendButton.setEnabled(false);

        Tab generalTab = new Tab("#Общий");
        generalTab.addClassName("general-tab");
        Tab supportTab = new Tab("#Поддержка");
        supportTab.addClassName("support-tab");
        Tabs tabs = new Tabs(generalTab, supportTab);
        tabs.setWidthFull();

        list.setSubmitter(activationContext -> {
            sendButton.setEnabled(true);
            Registration registration = sendButton.addClickListener(
                    event -> {
                        activationContext.appendMessage(sendField.getValue());
                        if (supportTab.isSelected()) {
                            try {
                                Bot bot = new Bot(BOT_TOKEN);
                                bot.sendMessage(CHAT_ID, userInfo.getName(), sendField.getValue(), userInfo.getId());

                                MessagesHistory messagesHistory = new MessagesHistory(userInfo.getName(), sendField.getValue());
                                MessagesHistoryController.insert(messagesHistory);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (generalTab.isSelected()) {
                            MessagesHistory messagesHistory = new MessagesHistory(userInfo.getName(), sendField.getValue());
                            try {
                                MessagesHistoryController.insert(messagesHistory);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            } finally {
                                sendField.clear();
                            }
                        }
                    });
            return () -> {
                registration.remove();
                sendButton.setEnabled(false);
            };
        });

        sendButton.addClickShortcut(Key.ENTER);

        list.setWidthFull();
        list.addClassNames("chat-view-message-list");

        messageMenu.addClassNames("message-menu");
        messageMenu.setWidthFull();
        messageMenu.setVisible(false);

        Div userInfoInputContainer = new Div();
        userInfoInputContainer.addClassName("user-info-input-container");

        Div userInfoInputContainerLogin = new Div();
        userInfoInputContainerLogin.addClassName("user-info-input-container-login");
        userInfoInputContainerLogin.setVisible(false);

        Span registrationTitle = new Span("Регистрация");
        registrationTitle.addClassName("registration-title");

        Span loginTitle = new Span("Вход");
        loginTitle.addClassName("login-title");

        Button confirmInfoInput = new Button("Зарегистрироваться");
        confirmInfoInput.addClassName("confirm-info-input");
        confirmInfoInput.addClickShortcut(Key.ENTER);

        Span alreadyHaveAnAccount = new Span("Уже зарегистрированы? Войти");
        alreadyHaveAnAccount.addClassName("already-have-an-account");

        Button confirmInfoInputLogin = new Button("Войти");
        confirmInfoInputLogin.addClassName("confirm-info-input-login");

        alreadyHaveAnAccount.addClickListener(spanClickEvent -> {
            userInfoInputContainer.setVisible(false);
            userInfoInputContainerLogin.setVisible(true);
        });

        TextField inputUserName = new TextField("Имя пользователя");
        inputUserName.addClassName("input-user-name");
        inputUserName.setMaxLength(16);
        inputUserName.setHelperText("От 4 до 16 символов");
        inputUserName.setRequired(true);
        inputUserName.setClearButtonVisible(true);
        inputUserName.setWidth("250px");

        TextField inputUserNameLogin = new TextField("Имя пользователя");
        inputUserNameLogin.addClassName("input-user-name-login");
        inputUserNameLogin.setMaxLength(16);
        inputUserNameLogin.setClearButtonVisible(true);
        inputUserNameLogin.setWidth("250px");

        EmailField inputEmail = new EmailField("Почта");
        inputEmail.addClassName("input-email");
        inputEmail.setRequiredIndicatorVisible(true);
        inputEmail.setClearButtonVisible(true);
        inputEmail.setWidth("250px");

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Пароль");
        passwordField.addClassName("input-password");
        passwordField.setRequired(true);
        passwordField.setClearButtonVisible(true);
        passwordField.setRevealButtonVisible(true);
        passwordField.setWidth("250px");

        PasswordField passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setLabel("Пароль");
        passwordFieldLogin.addClassName("input-password-login");
        passwordFieldLogin.setClearButtonVisible(true);
        passwordFieldLogin.setRevealButtonVisible(true);
        passwordFieldLogin.setWidth("250px");

        checkIconForUsername = VaadinIcon.CHECK.create();
        checkIconForUsername.setColor("#233348");

        checkIconForEmail = VaadinIcon.CHECK.create();
        checkIconForEmail.setColor("#233348");

        checkIconForPassword = VaadinIcon.CHECK.create();
        checkIconForPassword.setColor("#233348");

        Span checkIconHolderForUsername = new Span();
        checkIconHolderForUsername.addClassName("check-icon");
        checkIconHolderForUsername.add(checkIconForUsername);

        Span checkIconHolderForEmail = new Span();
        checkIconHolderForEmail.addClassName("check-icon");
        checkIconHolderForEmail.add(checkIconForEmail);

        Span checkIconHolderForPassword = new Span();
        checkIconHolderForPassword.addClassName("check-icon");
        checkIconHolderForPassword.add(checkIconForPassword);

        Div passAndItsCheck = new Div();
        passAndItsCheck.addClassName("pass-and-check");
        passAndItsCheck.add(passwordField, checkIconHolderForPassword);

        Div usernameAndItsCheck = new Div();
        usernameAndItsCheck.addClassName("username-and-check");
        usernameAndItsCheck.add(inputUserName, checkIconHolderForUsername);

        Div emailAndItsCheck = new Div();
        emailAndItsCheck.addClassName("email-and-check");
        emailAndItsCheck.add(inputEmail, checkIconHolderForEmail);

        Div passwordStrength = new Div();
        passwordStrength.setWidth("270px");
        passwordStrengthText = new Span();
        passwordStrengthText.addClassName("password-strength");
        passwordStrength.add(new Text("Надёжность пароля: "), passwordStrengthText);
        passwordField.setHelperComponent(passwordStrength);

        confirmInfoInput.addClickListener(e -> {

            if (!Objects.equals(checkIconForUsername.getColor(), "green")){
                inputUserName.setPlaceholder("Неверный формат!");
            }
            else {
                userName = inputUserName.getValue();
            }

            if (!Objects.equals(checkIconForEmail.getColor(), "green")){
                inputEmail.setPlaceholder("Неверный формат!");
            }

            if (!Objects.equals(checkIconForPassword.getColor(), "green")){
                passwordField.setPlaceholder("Недостаточно надёжный!");
            } else {
                userInfo.setName(userName);
            }

            if (Objects.equals(checkIconForUsername.getColor(), "green") &&
                    Objects.equals(checkIconForEmail.getColor(), "green") &&
                    Objects.equals(checkIconForPassword.getColor(), "green")){

                String name = inputUserName.getValue();
                String email = inputEmail.getValue();
                String password = passwordField.getValue();

                try {
                    Encryption.encryptPass(password);
                    String encryptedPassword = Encryption.getEncryptedPassword();
                    String encodedKey = Encryption.getEncodedSecretKey();
                    SecretKeySpec key = Encryption.getSecretKey();

                    Userlogins userlogins = new Userlogins(name, email, encryptedPassword);
                    PasswordSecretkeys passwordSecretkeys = new PasswordSecretkeys(name, encryptedPassword, encodedKey,
                            String.valueOf(key), Encryption.getRandom());
                    UserloginsController.insert(userlogins);
                    PasswordSecretkeysController.insert(passwordSecretkeys);

                    messageMenu.setVisible(true);
                    userInfoInputContainer.setVisible(false);
                    userInfoInputContainerLogin.setVisible(false);

                } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                    createNotification("Имя пользователя или email заняты!", 7000);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } else {
                createNotification("Как минимум одно поле имеет неверный формат!", 7000);
            }

            userInfo.setImage(inputEmail.getValue());

        });

        confirmInfoInputLogin.addClickListener(e -> {

            userName = inputUserNameLogin.getValue();
            password = passwordFieldLogin.getValue();
            try {
                String encodedsecretkey = PasswordSecretkeysController.selectEncodedSecretKeyByName(userName);

                byte[] decodedKey = Base64.getDecoder().decode(encodedsecretkey);

                SecretKeySpec secretkey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

                password = UserloginsController.selectEncryptedPassword(userName).getPassword();

                password = Encryption.decrypt(password, secretkey);

            } catch (GeneralSecurityException generalSecurityException) {
                generalSecurityException.printStackTrace();
            }

            userInfo.setName(userName);

            boolean check = UserloginsController.validate(userName);
            if (check && Objects.equals(passwordFieldLogin.getValue(), password)) {
                userName = inputUserNameLogin.getValue();
                userInfo.setName(userName);
                System.out.println("Successfully!");
                registrationTitle.setVisible(false);
                inputUserName.setVisible(false);
                inputEmail.setVisible(false);
                passwordField.setVisible(false);
                confirmInfoInput.setVisible(false);
                loginTitle.setVisible(false);
                inputUserNameLogin.setVisible(false);
                passwordFieldLogin.setVisible(false);
                confirmInfoInputLogin.setVisible(false);
                checkIconForUsername.setColor("#233348");
                checkIconForEmail.setColor("#233348");
                checkIconForPassword.setColor("#233348");
                messageMenu.setVisible(true);
            }
            else {
                createNotification("Неправильное имя пользователя или пароль!", 7000);
            }

            userInfo.setImage(inputEmail.getValue());
        });

        userInfoInputContainer.add(registrationTitle, usernameAndItsCheck, emailAndItsCheck,
                passAndItsCheck, alreadyHaveAnAccount,confirmInfoInput);

        userInfoInputContainerLogin.add(loginTitle, inputUserNameLogin, passwordFieldLogin, confirmInfoInputLogin);

        add(tabs, list, userInfoInputContainer, userInfoInputContainerLogin, messageMenu);
        setSizeFull();
        expand(list);

        inputUserName.setValueChangeMode(ValueChangeMode.EAGER);
        inputUserName.addValueChangeListener(e -> usernameUpdateHelper(inputUserName));

        inputEmail.setValueChangeMode(ValueChangeMode.EAGER);
        inputEmail.addValueChangeListener(e -> emailUpdateHelper(inputEmail));

        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField.addValueChangeListener(e -> {
            String password = e.getValue();
            passUpdateHelper(password);
        });

        passUpdateHelper("");

        tabs.addSelectedChangeListener(event -> {
            String channelName = event.getSelectedTab().getLabel();
            list.setTopic("chat/" + channelName);
        });

    }

    private void passUpdateHelper(String password) {

        if ( (password.length() > 15) || (reliableLevel(password) == 3 && password.length() > 10) ){
            passwordStrengthText.setText("супер мега надёжный");
            passwordStrengthText.getStyle().set("color", "var(--lumo-success-color)");
            checkIconForPassword.setColor("green");
        } else if ( (password.length()) > 12 || (reliableLevel(password) == 2 && password.length() > 8) ) {
            passwordStrengthText.setText("очень надёжный");
            passwordStrengthText.getStyle().set("color", "var(--lumo-success-color)");
            checkIconForPassword.setColor("green");
        } else if ( (password.length() > 8) || (reliableLevel(password) == 1 && password.length() > 6) ){
            passwordStrengthText.setText("надёжный");
            passwordStrengthText.getStyle().set("color", "var(--lumo-success-color)");
            checkIconForPassword.setColor("green");
        } else if (password.length() > 5) {
            passwordStrengthText.setText("ненадёжный");
            passwordStrengthText.getStyle().set("color", "#e7c200");
            checkIconForPassword.setColor("#233348");
        } else {
            passwordStrengthText.setText("совсем не надёжный");
            passwordStrengthText.getStyle().set("color", "var(--lumo-error-color)");
            checkIconForPassword.setColor("#233348");
        }
    }

    private void usernameUpdateHelper(TextField username) {
        if (username.getValue().length() > 3) {
            checkIconForUsername.setColor("green");
        }
    }

    private void emailUpdateHelper(EmailField email) {
        if (!email.isInvalid()) {
            checkIconForEmail.setColor("green");
        }
    }

    private static int reliableLevel(String password) {

        int relyLevel = 0;

        Pattern pattern = Pattern.compile("(\\W)^(а-я | А-Я)|(_)");
        Matcher matcher = pattern.matcher(password);
        long matches = matcher.results().count();

        if (matches == 1){
            relyLevel = 1;
        } else if (matches == 2){
            relyLevel = 2;
        }
        else if (matches > 2){
            relyLevel = 3;
        }

        return relyLevel;
    }

    private void createNotification(String message, int duration) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(duration);

        Div text = new Div(new Text(message));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);

        notification.add(layout);
        notification.open();

    }
}
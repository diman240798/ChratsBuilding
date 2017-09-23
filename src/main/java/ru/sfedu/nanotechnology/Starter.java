package ru.sfedu.nanotechnology;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.sfedu.nanotechnology.controller.ErrorController;
import ru.sfedu.nanotechnology.service.FXMLLoaderService;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Starter extends Application implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger(Starter.class);

    @Override
    public void run(String... args) {
        // something to call prior to the real application starts?
    }

    private static String[] savedArgs;

    // locally stored Spring Boot application context
    private ConfigurableApplicationContext applicationContext;

    // we need to override the FX init process for Spring Boot
    @Override
    public void init() throws Exception {

        // set Thread name
        Thread.currentThread().setName("main");

        // LOG.debug("Init JavaFX application");
        applicationContext = SpringApplication.run(getClass(), savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    // ... and close our context on stop of the FX part
    @Override
    public void stop() throws Exception {
        // LOG.debug("Stop JavaFX application");
        super.stop();
        applicationContext.close();
    }

    protected static void launchApp(Class<? extends Starter> appClass, String[] args) {
        Starter.savedArgs = args;
        Application.launch(appClass, args);
    }

    @Autowired
    private FXMLLoaderService fxmlLoaderService;

    @Override
    public void start(Stage primaryStage) {

        Thread.setDefaultUncaughtExceptionHandler(Starter::showError);

        // set Thread name
        Thread.currentThread().setName("main-ui");

        try {
            final Locale locale = Locale.getDefault();
            final ResourceBundle bundle = ResourceBundle
                    .getBundle("ru.sfedu.nanotechnology.Locale", locale);

            primaryStage.setTitle(bundle.getString("title"));
            primaryStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("diagram_16.png")));

            FXMLLoader loader = fxmlLoaderService.getLoader(
                    getClass().getResource("Main.fxml"), bundle);

            Pane root = loader.load();

            Scene scene = new Scene(root, 640, 480);

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(windowEvent -> {

                LOG.debug("tear down JavaFX application");
                // orderly shut down FX
                Platform.exit();

                // But: there might still be a daemon thread left over from OkHttp (some async dispatcher)
                // so assume everything is fine and call System.exit(0)
                System.exit(0);
            });

            primaryStage.show();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {

        // SpringApplication.run(SampleSimpleApplication.class, args);
        savedArgs = args;
        Application.launch(Starter.class, args);
    }

    private static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in " + t);
        }
    }

    private static void showErrorDialog(Throwable e) {
        StringWriter errorMsg = new StringWriter();
        e.getCause().getCause().printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(Starter.class.getClassLoader()
                .getResource("Error.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg
                    .toString());
            dialog.setScene(new Scene(root, 250, 400));
            dialog.show();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}

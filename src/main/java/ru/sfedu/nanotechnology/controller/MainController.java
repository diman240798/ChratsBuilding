package ru.sfedu.nanotechnology.controller;

import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ru.sfedu.nanotechnology.controller.tab.*;
import javafx.fxml.FXML;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.sfedu.nanotechnology.service.FXMLLoaderService;

@Component
@Scope("prototype")
public class MainController implements Disposable, Initializable {
    private static final Logger LOG = LogManager.getLogger(MainController.class);
    @Autowired
    private FXMLLoaderService fxmlLoaderService;
    @Autowired
    private ConfigurableApplicationContext context;

    @FXML
    private ResourceBundle resourceBundle;

    @FXML
    public void initialize() {
        LOG.info("The Context: " + context);
        LOG.info("FXMLLoaderService: " + fxmlLoaderService);

//		generalController.init(this);
//    resultsController.init(this);
//		settingsController.init(this);
    }

    @FXML
    public void handleMenuFileExit(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void dispose() {
        LOG.info(this.getClass().getSimpleName() + ": dispose");
    }

    @PreDestroy
    public void preDestroy() {
        dispose();
    }

    ////////////////////////////////////////
    /**
     * To print function from written coef
     */
    @FXML
    private TextField tfX2;
    @FXML
    private TextField tfX1;
    @FXML
    private TextField tfFreeCoef;
    @FXML
    private TextField tfFrom;
    @FXML
    private TextField tfTo;
    @FXML
    private TextField tfStep;
    @FXML
    private LineChart<Double, Double> graphic;

    public void PrintFuction(ActionEvent actionEvent) {

        graphic.getData().clear();
        XYChart.Series series = new XYChart.Series();

        double begin, end;

        try {
            begin = Double.parseDouble(tfFrom.getText());
            end = Double.parseDouble(tfTo.getText());
        } catch (Exception e) {
            LOG.info("Wrong Period Data");
            return;
        }

        double x2 = 0, x1 = 0, freeCoef = 0, step = 1;

        try {
            if (!tfX2.getText().isEmpty()) {
                x2 = Double.parseDouble(tfX2.getText());
            }
            if (!tfX1.getText().isEmpty()) {
                x1 = Double.parseDouble(tfX1.getText());
            }
            if (!tfFreeCoef.getText().isEmpty()) {
                freeCoef = Double.parseDouble(tfFreeCoef.getText());
            }
            if (!tfStep.getText().isEmpty()) {
                step = Double.parseDouble(tfStep.getText());
            }

            if (tfX2.getText().isEmpty() && tfX1.getText().isEmpty() && tfFreeCoef.getText().isEmpty()) {
                throw new Exception();
            }

        } catch (Exception e) {
            LOG.info("Wrong Fuction Data");
            return;
        }


        for (double i = begin; i <= end; i += step) {
            series.getData().add(new XYChart.Data(i, x2 * i * i + x1 * i + freeCoef));
        }

        graphic.getData().add(series);
    }
    //////////////////////////////////////
    /**
     * To print function from TextField
     */
    @FXML
    private TextField lbFunction;
    @Autowired
    RegexFunc regexFunc;

    public void ScanAndPrintFunction(ActionEvent event) {

        graphic.getData().clear();
        XYChart.Series series = new XYChart.Series();
        String function = lbFunction.getText();

        double begin, end;

        try {
            begin = Double.parseDouble(tfFrom.getText());
            end = Double.parseDouble(tfTo.getText());
        } catch (Exception e) {
            LOG.info("Wrong Period Data");
            return;
        }
        double step = 1;
        try {
            step = Double.parseDouble(tfStep.getText());
        } catch (Exception e) {}


        HashMap<Double, Double> hashMap_Coef_Power = regexFunc.checkXinSomeDegr(function);

        double coef, power;
        // Consists of coefs
        double[] coefs = new double[hashMap_Coef_Power.size()];
        // Consists of powers
        double[] powers = new double[hashMap_Coef_Power.size()];

        int j=0;
        for (Map.Entry<Double,Double> entry : hashMap_Coef_Power.entrySet()) {
            coefs[j] = entry.getKey();
            powers[j] = entry.getValue();
            j+=1;
        }

        double coefBeforeX1 = regexFunc.checkXinFirstDegr(function);
        double freeCoef = regexFunc.checkFreeCoef(function);


        // And Finally Print Fuction
        double result1 = 0;
        for (double i = begin; i <= end; i += step) {


            for (j=0; j < coefs.length; j++) {
                result1+= coefs[j] * Math.pow(i, powers[j]);
            }



            series.getData().add(new XYChart.Data(i, result1 + coefBeforeX1 * i + freeCoef));
            result1= 0;


        }
        graphic.getData().add(series);
    }



    /////////////////////////////////////
    /**
     * Everything that is Written bellow
     * is Localization Staff
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }

    @FXML
    private ChoiceBox langChoice;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuFileExit;
    @FXML
    private Tab tabGeneral;
    @FXML
    private Label writeFunc;
    @FXML
    private Label enterPeriod;
    @FXML
    private Label from;
    @FXML
    private Label to;
    @FXML
    private Button btPrint;
    @FXML
    private Label lbStep;
    @FXML
    private Tab tabOutput;
    @FXML
    private Tab tabSettings;
    @FXML
    private Label lbLang;


    public void chageLanguage(ActionEvent actionEvent) {
            loadLang(langChoice.getValue().toString());
    }

    private void loadLang(String lang) {
        // Check that lang is not empty
        if (lang.isEmpty())
            return;

        // Create Localization
        Locale locale = new Locale(lang);

        // Get needed Bundle
        resourceBundle = ResourceBundle.getBundle("ru.sfedu.nanotechnology.Locale", locale);
        enterPeriod.setText(resourceBundle.getString("enter.period"));
        menuFile.setText(resourceBundle.getString("menu.File"));
        menuFileExit.setText(resourceBundle.getString("menu.FileExit"));
        tabGeneral.setText(resourceBundle.getString("tab.General"));
        tabOutput.setText(resourceBundle.getString("tab.Output"));
        tabSettings.setText(resourceBundle.getString("tab.Settings"));
        writeFunc.setText(resourceBundle.getString("write.function"));
        from.setText(resourceBundle.getString("lb.from"));
        to.setText(resourceBundle.getString("lb.to"));
        graphic.setTitle(resourceBundle.getString("title.Graph"));
        btPrint.setText(resourceBundle.getString("bt.print"));
        lbStep.setText(resourceBundle.getString("lb.step"));
        lbLang.setText(resourceBundle.getString("lb.language"));

    }
}


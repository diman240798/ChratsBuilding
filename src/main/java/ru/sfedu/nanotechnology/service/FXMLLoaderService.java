package ru.sfedu.nanotechnology.service;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

public interface FXMLLoaderService {
	
	FXMLLoader getLoader();
	
	FXMLLoader getLoader(URL location);
	
	FXMLLoader getLoader(URL location, ResourceBundle resourceBundle);
}

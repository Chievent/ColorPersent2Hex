/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colorpersent2hex;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author zhaoshe
 */
public class ColorPercent2Hex extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        Scene scene = new Scene(grid, 300, 180);
                
        Label percentLabel = new Label("Percent(%)");
        grid.add(percentLabel, 0, 1);
        
        TextField percentField = new TextField();
        // only numbers
        percentField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
                percentField.setText(newValue);
            }
            
            if (newValue.length() > 3) {
                newValue = newValue.substring(0, 3);
                percentField.setText(newValue);
            }
        });
        grid.add(percentField, 1, 1);
        
        Button translateBtn = new Button("Translate&Copy");
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(translateBtn);
        grid.add(hbBtn, 1, 2);
        
        Label resultLabel = new Label();
      
        grid.add(resultLabel, 0, 2);
        
        translateBtn.setOnAction((ActionEvent e) -> {
            String percentStr = percentField.getText();
            if (percentStr.length() > 2 && !"100".equals(percentStr)) {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Wrong Percent");
                alert.setContentText("Ooops, there was an wrong percent! \nONLY 0~100 is Accepted");

                alert.showAndWait();
                System.out.println("Invalid Percent! Must be between 0 and 100");
                return;
            }
            
            Integer percent = Integer.valueOf(percentField.getText());
            int dec = (int) (percent * 255 / 100 + 0.5f);
            StringBuilder builder = new StringBuilder(2);
            builder.append(byte2Char((byte) (dec / 16)));
            builder.append(byte2Char((byte) (dec % 16)));
            String result = builder.toString();
            
            resultLabel.setText(result);
            
            Clipboard clip = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(result);
            clip.setContent(content);
        });
        
        primaryStage.setTitle("ColorPercent2Hex");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private char byte2Char(byte dec) {
        if (dec >= 0 && dec <= 15) {
            if (dec <= 9) {
                return (char) ('0' + dec);
            } else {
                return (char) ('a' + dec - 10);
            }
        }
        return 'x';
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

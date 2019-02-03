package rsa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class MathAttackGraph extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Math Attack");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Key Size(bits)");
        yAxis.setLabel("Crack Steps");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Math Attack");
        XYChart.Series series = new XYChart.Series();
        series.setName("Math Attack");

        for (int size = 32; size <= 64; ++size) {
            System.out.println("Key Size: " + size);

            RSA rsa = new RSA(size);
            MathAttack mathAttack = new MathAttack(rsa.publicKey, rsa.modulus);

            series.getData().add(new XYChart.Data(size, mathAttack.nSteps));
        }

        Scene scene  = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package rsa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.Random;


public class EncryptionGraph extends Application {
    private final static int nRuns = 500000;

    @Override
    public void start(Stage stage) {
        Random random = new Random();

        stage.setTitle("RSA");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Key Size(bits)");
        yAxis.setLabel("Encryption Time(ms)");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("RSA");
        XYChart.Series series = new XYChart.Series();
        series.setName("RSA");

        for (int size = 32; size <= 2048; size *= 2) {
            BigInteger message = new BigInteger(size - 1, random);
            RSA rsa = new RSA(size);

            long start = System.nanoTime();
            for(int run = 0; run < nRuns; ++run) {
                rsa.encrypt(message);
            }
            long end = System.nanoTime();
            long duration = (end - start) / nRuns;

            series.getData().add(new XYChart.Data(size, duration));
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

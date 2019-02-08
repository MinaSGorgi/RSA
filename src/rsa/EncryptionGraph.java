package rsa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.util.Random;


public class EncryptionGraph extends Application {
    private final static int NUM_RUNS = 10;
    private final static int NANO_TO_MICRO = (int) Math.pow(10, 3);

    @Override
    public void start(Stage stage) {
        Random random = new Random();

        stage.setTitle("RSA");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Key Size(bits)");
        yAxis.setLabel("Encryption Time(micro second)");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("RSA");
        XYChart.Series series = new XYChart.Series();
        series.setName("RSA");

        ThreadMXBean thread = ManagementFactory.getThreadMXBean();
        for (int size = 4; size <= 2048; size *= 2) {
            System.out.println("Key Size: " + size);

            BigInteger message = new BigInteger(size - 1, random);
            RSA rsa = new RSA(size);

            long start = thread.getCurrentThreadCpuTime();
            for(int run = 0; run < NUM_RUNS; ++run) {
                rsa.encrypt(message);
            }
            long end = thread.getCurrentThreadCpuTime();
            long duration = (end - start) / NUM_RUNS;

            series.getData().add(new XYChart.Data(size, duration / NANO_TO_MICRO));
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

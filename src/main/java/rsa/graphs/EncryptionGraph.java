package graphs;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.util.Random;

import core.RSA;

import javax.imageio.ImageIO;


public class EncryptionGraph extends Application {
    private final int NUM_RUNS = 1000;
    private final int NANO_TO_MICRO = (int) Math.pow(10, 3);

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

            BigInteger message = new BigInteger(size / 2, random);
            RSA rsa = new RSA(size);

            long start = thread.getCurrentThreadCpuTime();
            for(int run = 0; run < NUM_RUNS; ++run) {
                try {
                    rsa.encrypt(message);
                } catch (Exception e) {
                    
                }
            }
            long end = thread.getCurrentThreadCpuTime();
            long duration = (end - start) / NUM_RUNS;

            series.getData().add(new XYChart.Data(size, duration / NANO_TO_MICRO));
        }

        Scene scene  = new Scene(lineChart, 800, 600);
        lineChart.setAnimated(false);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();

        WritableImage image = scene.snapshot(null);
        File file = new File("Encryption.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(EncryptionGraph.class, args);
    }

}

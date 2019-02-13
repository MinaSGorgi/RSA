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

import attacks.MathAttack;
import core.RSA;

import javax.imageio.ImageIO;


public class MathAttackGraph extends Application {
    private final static int NUM_RUNS = 1000;
    private final static int NANO_TO_MICRO = (int) Math.pow(10, 3);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Math Attack");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Key Size(bits)");
        yAxis.setLabel("Crack Time(micro second)");
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Math Attack");
        XYChart.Series series = new XYChart.Series();
        series.setName("Math Attack");

        ThreadMXBean thread = ManagementFactory.getThreadMXBean();
        for (int size = 32; size <= 80; ++size) {
            System.out.println("Key Size: " + size);

            RSA rsa = new RSA(size);

            long start = thread.getCurrentThreadCpuTime();
            new MathAttack(rsa.publicKey, rsa.modulus);
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
        File file = new File("MathAttack.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

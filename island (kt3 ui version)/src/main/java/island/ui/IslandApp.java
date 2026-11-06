package island.ui;

import island.config.Config;
import island.engine.Simulation;
import island.model.Island;
import island.model.Location;
import island.model.Plant;
import island.model.SpeciesType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class IslandApp extends Application {

    private static Simulation simulation;

    public static void launchApp(Simulation sim, String[] args) {
        simulation = sim;
        launch(IslandApp.class, args);
    }

    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        int cell = Config.CELL_SIZE;
        Canvas canvas = new Canvas(Config.WIDTH * cell, Config.HEIGHT * cell);
        Label stats = new Label();
        stats.setStyle("-fx-font-size: 14px;");

        VBox root = new VBox(8, canvas, stats);
        root.setPadding(new Insets(8));
        stage.setTitle("Остров");
        stage.setScene(new Scene(root));
        stats.setText(buildStats(simulation.getIsland()));
        stage.show();

        simulation.start();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font(cell * 0.85));
        timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> redraw(gc, stats)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        simulation.stop();
    }

    private void redraw(GraphicsContext gc, Label stats) {
        Island island = simulation.getIsland();
        int cell = Config.CELL_SIZE;

        for (int x = 0; x < island.getWidth(); x++) {
            for (int y = 0; y < island.getHeight(); y++) {
                Location loc = island.getLocation(x, y);
                int plants;
                SpeciesType dominant;
                loc.lock();
                try {
                    plants = loc.getPlants();
                    dominant = loc.dominant();
                } finally {
                    loc.unlock();
                }

                gc.setFill(Color.rgb(20, 40 + 140 * plants / Plant.MAX_PER_CELL, 20));
                gc.fillRect(x * cell, y * cell, cell, cell);

                if (dominant != null) {
                    gc.setFill(Color.WHITE);
                    gc.fillText(dominant.getEmoji(), x * cell, (y + 1) * cell - 2);
                }
            }
        }

        stats.setText(buildStats(island));
    }


    private String buildStats(Island island) {
        Map<SpeciesType, Integer> counts = island.countAnimals();
        StringBuilder sb = new StringBuilder();
        sb.append("Такт ").append(simulation.getTick())
                .append("   ").append(Plant.EMOJI).append(' ').append(island.countPlants());
        if (!simulation.isRunning()) {
            sb.append("   (симуляция остановлена)");
        }
        int i = 0;
        for (SpeciesType t : SpeciesType.VALUES) {
            sb.append(i % 5 == 0 ? "\n" : "    ");
            sb.append(t.getEmoji()).append(' ').append(t.getTitle()).append(": ").append(counts.get(t));
            i++;
        }
        return sb.toString();
    }
}
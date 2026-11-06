package island;

import island.engine.Simulation;
import island.ui.IslandApp;

public class Main {

    public static void main(String[] args) {
        IslandApp.launchApp(new Simulation(), args);
    }
}

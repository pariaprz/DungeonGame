package unsw.dungeon;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Timer;
import java.util.TimerTask;

public abstract class PlayerState implements State {
    public final static StringProperty stateName = new SimpleStringProperty("Default");
    public final static IntegerProperty timeRemaining = new SimpleIntegerProperty(0);
    abstract boolean attractsEnemies();
    abstract void interactWithEnemy(Slayable enemy);
    abstract void expireState();

    public void setState() {
        stateName.setValue(getStateName());
    }

    public static void scheduleState(PlayerState state, Timer timerTask, int duration) {
        timeRemaining.setValue(duration);

        timeRemaining.setValue(duration);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(state::expireState);
            }
        };
        timerTask.schedule(task, duration*1000);                   //Time in milliseconds

        timerTask.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                Platform.runLater(() -> timeRemaining.setValue(timeRemaining.get() -1));
            }
        }, 1000, 1000); // Every second, after a 1s delay.
    }

    
}
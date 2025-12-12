package taskmanagement.Models;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Music {
    private final List<File> songs;
    private State playingState = State.STOPPED;

    public enum State {
        PLAYING, STOPPED
    }

    public Music() {
        this.songs = loadSongsFromDirectory();
    }

    private List<File> loadSongsFromDirectory() {
        try {
            URL resource = getClass().getClassLoader().getResource("Music");
            if (resource != null) {
                File directory = new File(resource.getFile());
                File[] files = directory.listFiles();
                return files != null ? new ArrayList<>(Arrays.asList(files)) : new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<File> getSongs() {
        return songs;
    }

    public boolean isPlaying() {
        return playingState == State.PLAYING;
    }

    public void stopPlaying() {
        playingState = State.STOPPED;
    }

    public void startPlaying() {
        playingState = State.PLAYING;
    }
}

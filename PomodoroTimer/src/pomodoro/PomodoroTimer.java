/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pomodoro;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author jon-cameron
 */
public class PomodoroTimer {

    private static final Queue<Integer> queue = new LinkedList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            initQueue(args);
        } catch (NumberFormatException nfe) {
            System.out.println("Your input could not be interpreted as numbers, please try again");
        }
        
        System.out.print("\rStarting Pomodoro timer");
        while (true) {
            Integer totalRestTime = queue.poll();
            System.out.println();
            System.out.print(String.format("\rSetting timer for %d minutes.", totalRestTime / (60 * 1000)));
            System.out.println();
            try {
                Integer remainingRestTime = totalRestTime;
                while (remainingRestTime > 0) {
                    System.out.print(String.format("\rSeconds left: %s", formatAsMinutesSeconds(remainingRestTime)));
                    Thread.sleep(1000);
                    remainingRestTime -= 1000;
                }
                playSound();
                queue.add(totalRestTime);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                System.out.println();
                System.out.print("Timer failed to run, stopping execution: " + e.getMessage());
                return;
            }
        }
    }

    private static void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String gongFile = "sound-effect.wav";
        InputStream in = new FileInputStream(gongFile);

        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(in);

        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);
    }

    private static String formatAsMinutesSeconds(Integer time) {
        Integer minutes = time / (60 * 1000);
        Integer seconds = time % (60 * 1000);
        return String.format("%d:%s", minutes, seconds / 1000 < 10 ? "0" + String.valueOf(seconds / 1000) : String.valueOf(seconds / 1000));
    }

    private static void initQueue(String args[]) throws NumberFormatException {
        for (String arg : args) {
            Integer time = Integer.parseInt(arg);
            queue.add(time * 1000 * 60);
        }
    }

}

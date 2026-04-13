package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public final class UiSoundEffects {

  private static final String CLICKED_SOUND_PATH = "/sound/clicked.wav";
  private static final String SELECTED_SOUND_PATH = "/sound/selected.wav";
  private static final Object HOVER_SOUND_LOCK = new Object();
  private static final Object CLICK_SOUND_LOCK = new Object();

  private static volatile Clip selectedHoverClip;
  private static volatile boolean selectedHoverSoundDisabled;
  private static volatile Clip clickedClip;
  private static volatile boolean clickedSoundDisabled;

  private UiSoundEffects() {
  }

  public static void installHoverSound(Node node) {
    if (node == null) {
      return;
    }

    node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> playSelectedHoverSound());
  }

  public static void installHoverSound(TabPane tabPane) {
    if (tabPane == null) {
      return;
    }

    Set<Node> tabNodes = tabPane.lookupAll(".tab");
    for (Node tabNode : tabNodes) {
      installHoverSound(tabNode);
    }
  }

  public static void installClickSound(Node node) {
    if (node == null) {
      return;
    }

    node.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> playClickedSound());
  }

  public static void playSelectedHoverSound() {
    if (selectedHoverSoundDisabled) {
      return;
    }

    Clip clip = getOrCreateSelectedHoverClip();
    if (clip == null) {
      return;
    }

    synchronized (HOVER_SOUND_LOCK) {
      try {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
      } catch (RuntimeException ignored) {
        selectedHoverSoundDisabled = true;
      }
    }
  }

  public static void playClickedSound() {
    if (clickedSoundDisabled) {
      return;
    }

    Clip clip = getOrCreateClickedClip();
    if (clip == null) {
      return;
    }

    synchronized (CLICK_SOUND_LOCK) {
      try {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
      } catch (RuntimeException ignored) {
        clickedSoundDisabled = true;
      }
    }
  }

  private static Clip getOrCreateSelectedHoverClip() {
    Clip clip = selectedHoverClip;
    if (clip != null || selectedHoverSoundDisabled) {
      return clip;
    }

    synchronized (HOVER_SOUND_LOCK) {
      if (selectedHoverClip != null || selectedHoverSoundDisabled) {
        return selectedHoverClip;
      }

      selectedHoverClip = createClip(SELECTED_SOUND_PATH);
      if (selectedHoverClip == null) {
        selectedHoverSoundDisabled = true;
      }

      return selectedHoverClip;
    }
  }

  private static Clip getOrCreateClickedClip() {
    Clip clip = clickedClip;
    if (clip != null || clickedSoundDisabled) {
      return clip;
    }

    synchronized (CLICK_SOUND_LOCK) {
      if (clickedClip != null || clickedSoundDisabled) {
        return clickedClip;
      }

      clickedClip = createClip(CLICKED_SOUND_PATH);
      if (clickedClip == null) {
        clickedSoundDisabled = true;
      }

      return clickedClip;
    }
  }

  private static Clip createClip(String resourcePath) {
    URL soundResource = UiSoundEffects.class.getResource(resourcePath);
    if (soundResource == null) {
      return null;
    }

    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundResource)) {
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      return clip;
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException ignored) {
      return null;
    }
  }
}
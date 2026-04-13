package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public final class UiSoundEffects {

  private static final String BACKGROUND_SOUND_PATH = "/sound/background.wav";
  private static final String CLICKED_SOUND_PATH = "/sound/clicked.wav";
  private static final String SELECTED_SOUND_PATH = "/sound/selected.wav";
  private static final float BACKGROUND_GAIN_DB = -20.0f;
  private static final float CLICKED_GAIN_DB = -6.0f;
  private static final float SELECTED_GAIN_DB = -6.0f;
  private static final double MIN_MASTER_VOLUME = 0.0;
  private static final double MAX_MASTER_VOLUME = 1.0;
  private static final float MIN_VOLUME_DB = -80.0f;
  private static final Object BACKGROUND_SOUND_LOCK = new Object();
  private static final Object HOVER_SOUND_LOCK = new Object();
  private static final Object CLICK_SOUND_LOCK = new Object();

  private static volatile Clip backgroundClip;
  private static volatile boolean backgroundSoundDisabled;
  private static volatile boolean backgroundMusicEnabled = true;
  private static volatile boolean soundEffectsEnabled = true;
  private static volatile double masterVolume = 1.0;
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

    node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
      if (node.isFocusTraversable()) {
        node.requestFocus();
      }
      playSelectedHoverSound();
    });
  }

  public static void startBackgroundMusic() {
    if (backgroundSoundDisabled || !backgroundMusicEnabled) {
      return;
    }

    Clip clip = getOrCreateBackgroundClip();
    if (clip == null) {
      return;
    }

    synchronized (BACKGROUND_SOUND_LOCK) {
      try {
        if (!clip.isRunning()) {
          clip.setFramePosition(0);
          clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
      } catch (RuntimeException ignored) {
        backgroundSoundDisabled = true;
      }
    }
  }

  public static void stopBackgroundMusic() {
    Clip clip = backgroundClip;
    if (clip == null) {
      return;
    }

    synchronized (BACKGROUND_SOUND_LOCK) {
      try {
        clip.stop();
      } catch (RuntimeException ignored) {
        backgroundSoundDisabled = true;
      }
    }
  }

  public static boolean isBackgroundMusicEnabled() {
    return backgroundMusicEnabled;
  }

  public static boolean isSoundEffectsEnabled() {
    return soundEffectsEnabled;
  }

  public static double getMasterVolume() {
    return masterVolume;
  }

  public static void setMasterVolume(double volume) {
    double clampedVolume = Math.max(MIN_MASTER_VOLUME, Math.min(MAX_MASTER_VOLUME, volume));
    masterVolume = clampedVolume;
    updateLoadedClipGains();
  }

  public static float getMasterVolumeDbOffset() {
    return toMasterVolumeDbOffset(masterVolume);
  }

  public static void setBackgroundMusicEnabled(boolean enabled) {
    backgroundMusicEnabled = enabled;
    if (enabled) {
      startBackgroundMusic();
    } else {
      stopBackgroundMusic();
    }
  }

  public static void setSoundEffectsEnabled(boolean enabled) {
    soundEffectsEnabled = enabled;
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

    node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> playClickedSound());
    node.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
        playClickedSound();
      }
    });
  }

  public static void playSelectedHoverSound() {
    if (!soundEffectsEnabled || selectedHoverSoundDisabled) {
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
    if (!soundEffectsEnabled || clickedSoundDisabled) {
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

      selectedHoverClip = createClip(SELECTED_SOUND_PATH, SELECTED_GAIN_DB);
      if (selectedHoverClip == null) {
        selectedHoverSoundDisabled = true;
      }

      return selectedHoverClip;
    }
  }

  private static Clip getOrCreateBackgroundClip() {
    Clip clip = backgroundClip;
    if (clip != null || backgroundSoundDisabled) {
      return clip;
    }

    synchronized (BACKGROUND_SOUND_LOCK) {
      if (backgroundClip != null || backgroundSoundDisabled) {
        return backgroundClip;
      }

      backgroundClip = createClip(BACKGROUND_SOUND_PATH, BACKGROUND_GAIN_DB);
      if (backgroundClip == null) {
        backgroundSoundDisabled = true;
      }

      return backgroundClip;
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

      clickedClip = createClip(CLICKED_SOUND_PATH, CLICKED_GAIN_DB);
      if (clickedClip == null) {
        clickedSoundDisabled = true;
      }

      return clickedClip;
    }
  }

  private static Clip createClip(String resourcePath, float gainDb) {
    URL soundResource = UiSoundEffects.class.getResource(resourcePath);
    if (soundResource == null) {
      return null;
    }

    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundResource)) {
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      applyGainWithMasterVolume(clip, gainDb);
      return clip;
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException ignored) {
      return null;
    }
  }

  private static void updateLoadedClipGains() {
    synchronized (BACKGROUND_SOUND_LOCK) {
      if (backgroundClip != null) {
        applyGainWithMasterVolume(backgroundClip, BACKGROUND_GAIN_DB);
      }
    }

    synchronized (HOVER_SOUND_LOCK) {
      if (selectedHoverClip != null) {
        applyGainWithMasterVolume(selectedHoverClip, SELECTED_GAIN_DB);
      }
    }

    synchronized (CLICK_SOUND_LOCK) {
      if (clickedClip != null) {
        applyGainWithMasterVolume(clickedClip, CLICKED_GAIN_DB);
      }
    }
  }

  private static void applyGainWithMasterVolume(Clip clip, float baseGainDb) {
    applyGain(clip, baseGainDb + toMasterVolumeDbOffset(masterVolume));
  }

  private static float toMasterVolumeDbOffset(double volume) {
    if (volume <= 0.0) {
      return MIN_VOLUME_DB;
    }

    return (float) (20.0 * Math.log10(volume));
  }

  private static void applyGain(Clip clip, float gainDb) {
    if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
      return;
    }

    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    float clamped = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gainDb));
    gainControl.setValue(clamped);
  }
}
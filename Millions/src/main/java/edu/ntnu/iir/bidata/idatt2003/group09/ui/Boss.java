package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Boss extends StackPane {

  private static final String IDLE_PATH = "/images/boss/blinking.gif";
  private static final String TALKING_PATH = "/images/boss/talking.gif";
  private static final String HAIR_PATH = "/images/boss/hair.gif";
  private static final String TALKING_SOUND_PATH = "/sound/talking.wav";
  private static final float TALKING_GAIN_DB = -4.0f;
  private static final Duration TALKING_FALLBACK_DURATION = Duration.seconds(2);
  private static final int DEFAULT_TALKING_LOOPS = 2;

  private final Image idleImage;
  private final Image talkingImage;
  private final Image hairImage;
  private final Duration talkingCycleDuration;
  private final ImageView imageView;
  private final ChatBubble chatBubble;
  private final PauseTransition talkingToIdleTransition;
  private final Clip talkingSoundClip;
  private boolean talkingSoundEnabled;
  private int talkingLoops;

  public Boss(String initialText, String fontFamily, double imageSize) {
    this.idleImage = loadImage(IDLE_PATH, imageSize);
    this.talkingImage = loadImage(TALKING_PATH, imageSize);
    this.hairImage = loadImage(HAIR_PATH, imageSize);
    this.talkingCycleDuration = loadGifCycleDuration(TALKING_PATH);

    this.imageView = new ImageView(idleImage);
    this.imageView.setPreserveRatio(true);
    this.imageView.setSmooth(false);
    this.imageView.setCache(false);

    this.chatBubble = new ChatBubble(initialText, fontFamily);
    this.talkingLoops = DEFAULT_TALKING_LOOPS;
    this.talkingSoundClip = createTalkingSoundClip();
    this.talkingSoundEnabled = this.talkingSoundClip != null;
    this.talkingToIdleTransition = new PauseTransition(talkingCycleDuration.multiply(this.talkingLoops));
    this.talkingToIdleTransition.setOnFinished(event -> setIdle());

    chatBubble.setManaged(false);
    chatBubble.setTranslateX(185);
    chatBubble.setTranslateY(30);

    setAlignment(Pos.BOTTOM_LEFT);
    setPickOnBounds(false);
    getChildren().addAll(imageView, chatBubble);
    StackPane.setAlignment(imageView, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(chatBubble, Pos.BOTTOM_LEFT);
  }

  private Image loadImage(String path, double imageSize) {
    InputStream imageStream = getClass().getResourceAsStream(path);
    if (imageStream == null) {
      return null;
    }
    return new Image(imageStream, imageSize, imageSize, true, false);
  }

  private Duration loadGifCycleDuration(String gifPath) {
    try (InputStream inputStream = getClass().getResourceAsStream(gifPath)) {
      if (inputStream == null) {
        return TALKING_FALLBACK_DURATION;
      }

      Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
      if (!readers.hasNext()) {
        return TALKING_FALLBACK_DURATION;
      }

      ImageReader reader = readers.next();
      try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
        reader.setInput(imageInputStream, false);
        int frameCount = reader.getNumImages(true);
        long totalDelayMs = 0;

        for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
          Node root = reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
          totalDelayMs += readFrameDelayMs(root);
        }

        if (totalDelayMs > 0) {
          return Duration.millis(totalDelayMs);
        }
      } finally {
        reader.dispose();
      }
    } catch (IOException ignored) {
      return TALKING_FALLBACK_DURATION;
    }

    return TALKING_FALLBACK_DURATION;
  }

  private Clip createTalkingSoundClip() {
    URL soundResource = getClass().getResource(TALKING_SOUND_PATH);
    if (soundResource == null) {
      return null;
    }

    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundResource)) {
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      applyGain(clip, TALKING_GAIN_DB);
      return clip;
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException ignored) {
      return null;
    }
  }

  private void applyGain(Clip clip, float gainDb) {
    if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
      return;
    }

    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    float clamped = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gainDb));
    gainControl.setValue(clamped);
  }

  private long readFrameDelayMs(Node root) {
    if (root == null) {
      return 0;
    }

    NodeList children = root.getChildNodes();
    for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
      Node child = children.item(childIndex);
      if (!"GraphicControlExtension".equals(child.getNodeName())) {
        continue;
      }

      NamedNodeMap attributes = child.getAttributes();
      if (attributes == null) {
        return 0;
      }

      Node delayTimeNode = attributes.getNamedItem("delayTime");
      if (delayTimeNode == null) {
        return 0;
      }

      try {
        int hundredths = Integer.parseInt(delayTimeNode.getNodeValue());
        return hundredths * 10L;
      } catch (NumberFormatException ignored) {
        return 0;
      }
    }

    return 0;
  }

  public void setIdle() {
    stopTalkingSound();
    setImage(idleImage);
  }

  public void setTalking() {
    setImage(talkingImage != null ? talkingImage : idleImage);
  }

  public void setHair() {
    stopTalkingSound();
    setImage(hairImage != null ? hairImage : idleImage);
  }

  private void playTalkingSound() {
    if (!talkingSoundEnabled || talkingSoundClip == null) {
      return;
    }

    try {
      talkingSoundClip.stop();
      talkingSoundClip.setFramePosition(0);
      talkingSoundClip.start();
    } catch (RuntimeException ignored) {
      talkingSoundEnabled = false;
    }
  }

  private void stopTalkingSound() {
    if (!talkingSoundEnabled || talkingSoundClip == null) {
      return;
    }

    try {
      talkingSoundClip.stop();
      talkingSoundClip.setFramePosition(0);
    } catch (RuntimeException ignored) {
      talkingSoundEnabled = false;
    }
  }

  private void setImage(Image image) {
    if (image != null) {
      imageView.setImage(image);
    }
  }

  public void updateTalkingBubble(String text) {
    updateTalkingBubble(text, talkingLoops);
  }

  public void updateTalkingBubble(String text, int loops) {
    setTalking();
    playTalkingSound();
    chatBubble.setText(text);
    int effectiveLoops = Math.max(1, loops);
    talkingToIdleTransition.setDuration(talkingCycleDuration.multiply(effectiveLoops));
    talkingToIdleTransition.stop();
    talkingToIdleTransition.playFromStart();
  }

  public void setTalkingLoops(int loops) {
    this.talkingLoops = Math.max(1, loops);
  }

  public int getTalkingLoops() {
    return talkingLoops;
  }

  public ChatBubble getChatBubble() {
    return chatBubble;
  }

  public ImageView getImageView() {
    return imageView;
  }
}

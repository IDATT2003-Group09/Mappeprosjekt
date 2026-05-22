
package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

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

import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;

/**
 * Representerer sjefsfiguren som vises i brukergrensesnittet.
 * Hånderer snakkeboblen hans også.
 */
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

  public Boss(String initialText, double imageSize) {
    this.idleImage = loadImage(IDLE_PATH, imageSize);
    this.talkingImage = loadImage(TALKING_PATH, imageSize);
    this.hairImage = loadImage(HAIR_PATH, imageSize);
    this.talkingCycleDuration = loadGifCycleDuration(TALKING_PATH);

    this.imageView = new ImageView(idleImage);
    this.imageView.setPreserveRatio(true);
    this.imageView.setSmooth(false);
    this.imageView.setCache(false);

    this.chatBubble = new ChatBubble(initialText);
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

  /**
   * Lager og initialiserer en ny `Boss`-komponent.
   *
   * @param initialText tekst som vises i pratboblen ved opprettelse
   * @param imageSize ønsket størrelse (bredde/ høyde) for boss-bildet
   */

  private Image loadImage(String path, double imageSize) {
    InputStream imageStream = getClass().getResourceAsStream(path);
    if (imageStream == null) {
      return null;
    }
    return new Image(imageStream, imageSize, imageSize, true, false);
  }

  /**
   * Laster et bilde fra ressurser og skalerer det til angitt størrelse.
   *
   * @param path ressursbanen til bildet
   * @param imageSize målstørrelse i piksler
   * @return `Image` eller `null` hvis ressursen ikke finnes
   */

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

  /**
   * Leser total varighet (syklus) for en GIF ved å summere rammeforsinkelser.
   * Hvis GIF ikke kan leses, returneres en fallback-varighet.
   *
   * @param gifPath ressursbanen til GIF-filen
   * @return varigheten av GIF-syklusen som en `Duration`
   */

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

  /**
   * Forsøker å opprette og konfigurere en `Clip` for tale-lyd.
   * Returnerer `null` hvis lydressursen ikke er tilgjengelig eller opprettelse feiler.
   *
   * @return ferdig konfigurert `Clip` eller `null`
   */

  private void applyGain(Clip clip, float gainDb) {
    if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
      return;
    }

    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    float clamped = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), gainDb));
    gainControl.setValue(clamped);
  }

  /**
   * Justerer gain (volum) på en `Clip` dersom kontrollen er støttet.
   *
   * @param clip lydklippet å justere
   * @param gainDb ønsket gain i desibel
   */

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

  /**
   * Leser delay-tiden (i millisekunder) fra GIF-metadata for en ramme.
   *
   * @param root rot-nodeen fra billedmetadata
   * @return delay i millisekunder, eller 0 hvis ikke tilgjengelig
   */

  public void setIdle() {
    stopTalkingSound();
    setImage(idleImage);
  }

  /**
   * Setter bossen til idle-tilstand (stopp lyd og vis idle-bilde).
   */

  public void setTalking() {
    setImage(talkingImage != null ? talkingImage : idleImage);
  }

  /**
   * Setter bossens bilde til talking-animasjonen (eller idle hvis ikke tilgjengelig).
   */

  public void setHair() {
    stopTalkingSound();
    setImage(hairImage != null ? hairImage : idleImage);
  }

  /**
   * Veksler til hair-animasjonen og stopper eventuell tale-lyd.
   */

  private void playTalkingSound() {
    if (!UiSoundEffects.isSoundEffectsEnabled() || !talkingSoundEnabled || talkingSoundClip == null) {
      return;
    }

    try {
      applyGain(talkingSoundClip, TALKING_GAIN_DB + UiSoundEffects.getCombinedSoundEffectsVolumeDbOffset());
      talkingSoundClip.stop();
      talkingSoundClip.setFramePosition(0);
      talkingSoundClip.start();
    } catch (RuntimeException ignored) {
      talkingSoundEnabled = false;
    }
  }

  /**
   * Starter tale-lyd dersom lydeffekter er aktivert og klippet er tilgjengelig.
   * Deaktiverer tale-lyd ved runtime-feil.
   */

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

  /**
   * Stopper tale-lyd og tilbakestiller avspillingsposisjon.
   * Deaktiverer tale-lyd ved runtime-feil.
   */

  private void setImage(Image image) {
    if (image != null) {
      imageView.setImage(image);
    }
  }

  /**
   * Setter det viste bildet til `image` hvis det ikke er `null`.
   *
   * @param image bildet som skal vises
   */

  public void updateTalkingBubble(String text) {
    updateTalkingBubble(text, talkingLoops);
  }

  /**
   * Oppdaterer pratboblen med ny tekst og starter talking-animasjon i standardantall løkker.
   *
   * @param text teksten som skal vises
   */

  public void updateTalkingBubble(String text, int loops) {
    setTalking();
    playTalkingSound();
    chatBubble.setText(text);
    int effectiveLoops = Math.max(1, loops);
    talkingToIdleTransition.setDuration(talkingCycleDuration.multiply(effectiveLoops));
    talkingToIdleTransition.stop();
    talkingToIdleTransition.playFromStart();
  }

  /**
   * Oppdaterer pratboblen med ny tekst og angir hvor mange ganger talking-animasjonen skal loopes.
   * Starter tale-lyd og sørger for at animasjonen går tilbake til idle etterpå.
   *
   * @param text teksten som skal vises
   * @param loops antall ganger animasjonen skal loopes (minst 1)
   */

  public void setTalkingLoops(int loops) {
    this.talkingLoops = Math.max(1, loops);
  }

  /**
   * Setter hvor mange ganger talking-animasjonen skal loope når pratboblen oppdateres.
   *
   * @param loops antall løkker (minst 1)
   */

  public int getTalkingLoops() {
    return talkingLoops;
  }

  /**
   * Henter antall konfigurerte talking-løkker.
   *
   * @return antall løkker
   */

  public ChatBubble getChatBubble() {
    return chatBubble;
  }

  /**
   * Henter referansen til pratboblen som viser tekster fra bossen.
   *
   * @return `ChatBubble`-instansen
   */

  public ImageView getImageView() {
    return imageView;
  }

  /**
   * Returnerer `ImageView`-komponenten som viser boss-bildet.
   *
   * @return `ImageView` for bossen
   */

  public void visibleChatBubble(boolean value) {
    chatBubble.setVisible(value);
  }

  /**
   * Viser eller skjuler pratboblen.
   *
   * @param value `true` for å vise, `false` for å skjule
   */

  public void visibleBoss(boolean value) {
    imageView.setVisible(value);
    getChildren().stream()
      .filter(n -> n != chatBubble && n != imageView)
      .forEach(n -> n.setVisible(value));
  }

  /**
   * Vis/skjul selve boss-figuren (bilde og eventuelle tilleggselementer).
   *
   * @param value `true` for synlig, `false` for skjult
   */
  /**
   * Plays the hair.gif animation, then returns to idle after the animation duration.
   * If the duration cannot be determined, defaults to 2 seconds.
   */
  public void playHairAnimation() {
    Image freshHairImage = loadImage(HAIR_PATH, imageView.getFitWidth() > 0 ? imageView.getFitWidth() : 500);
    imageView.setImage(freshHairImage != null ? freshHairImage : hairImage);
    Duration hairDuration = loadGifCycleDuration(HAIR_PATH);
    PauseTransition hairTransition = new PauseTransition(hairDuration);
    hairTransition.setOnFinished(e -> setIdle());
    hairTransition.play();
  }
}

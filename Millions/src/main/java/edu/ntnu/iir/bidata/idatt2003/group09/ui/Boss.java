package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.InputStream;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Boss extends VBox {

  private static final String IDLE_PATH = "/images/boss/blinking.gif";
  private static final String TALKING_PATH = "/images/boss/talking.gif";
  private static final String HAIR_PATH = "/images/boss/hair.gif";

  private final Image idleImage;
  private final Image talkingImage;
  private final Image hairImage;
  private final ImageView imageView;
  private final ChatBubble chatBubble;

  public Boss(String initialText, String fontFamily, double imageSize) {
    this.idleImage = loadImage(IDLE_PATH, imageSize);
    this.talkingImage = loadImage(TALKING_PATH, imageSize);
    this.hairImage = loadImage(HAIR_PATH, imageSize);

    this.imageView = new ImageView(idleImage);
    this.imageView.setPreserveRatio(true);
    this.imageView.setSmooth(false);
    this.imageView.setCache(false);

    this.chatBubble = new ChatBubble(initialText, fontFamily);

    setAlignment(Pos.TOP_LEFT);
    setSpacing(10);
    setPickOnBounds(false);
    getChildren().addAll(chatBubble, imageView);
  }

  private Image loadImage(String path, double imageSize) {
    InputStream imageStream = getClass().getResourceAsStream(path);
    if (imageStream == null) {
      return null;
    }
    return new Image(imageStream, imageSize, imageSize, true, false);
  }

  public void setIdle() {
    setImage(idleImage);
  }

  public void setTalking() {
    setImage(talkingImage != null ? talkingImage : idleImage);
  }

  public void setHair() {
    setImage(hairImage != null ? hairImage : idleImage);
  }

  private void setImage(Image image) {
    if (image != null) {
      imageView.setImage(image);
    }
  }

  public void updateTalkingBubble(String text) {
    setTalking();
    chatBubble.setText(text);
  }

  public ChatBubble getChatBubble() {
    return chatBubble;
  }

  public ImageView getImageView() {
    return imageView;
  }
}

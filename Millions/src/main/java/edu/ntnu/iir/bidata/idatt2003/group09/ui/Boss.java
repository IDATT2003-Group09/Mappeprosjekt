package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import javafx.scene.image.Image;

public class Boss {

  public static final Image idle = new Image(Boss.class.getResourceAsStream("/images/blinking.png"));
  public static final Image talking = new Image(Boss.class.getResourceAsStream("/images/talking.gif"));
  public static final Image hair = new Image(Boss.class.getResourceAsStream("/images/hair.png"));

  public ChatBubble chatBubble = new ChatBubble();

  public Boss() {
  }



  
}

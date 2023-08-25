import javax.swing.*;
import java.awt.*;

class Main{
  public static void main(String argv[]){
    Model m =new Model();

    View v = new View(m,"anime");
    new Controller(m,v);
  }
}
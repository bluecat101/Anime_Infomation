import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Controller implements ActionListener{
  private Model model;
  private View view;

  public Controller(Model m, View v){
    model = m;
    view = v;
    view.getSearchButton().addActionListener(this);
  }
  public void actionPerformed(ActionEvent e){
    System.out.printf("%s",view.getSearchText());
  }
}

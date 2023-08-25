import javax.swing.*;
import java.awt.*;

class View extends JFrame{
  private Model model;
  public View(Model m, String title){
    this.setTitle(title);
    model = m;
    this.setLayout(new GridLayout(1,2));
    SearchPanel searchPanel = new SearchPanel();
    SearchPanel searchPanel_2 = new SearchPanel();
    this.add(searchPanel);
    this.add(searchPanel_2);
    
    this.setSize(600,400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}
class SearchPanel extends JPanel{
  JButton []btn = new JButton[5];//ボタン配列
	public SearchPanel(){
		JPanel panel = new JPanel();
    JTextField text = new JTextField(10);
    JLabel label = new JLabel("ANIME title");
    panel.setLayout(new GridLayout(2,1));
    panel.add(label);
    panel.add(text);
    this.add(panel);
	}
}
import javax.swing.*;
import java.awt.*;

class View extends JFrame{
  private Model model;
  public View(Model m, String title){
    this.setTitle(title);
    model = m;
    SearchPanel searchPanel = new SearchPanel();
    this.add(searchPanel);
    
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
    panel.add(text);
    this.add(panel, BorderLayout.CENTER);
	}
}
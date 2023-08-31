import javax.swing.*;
import java.awt.*;
// import java.awt.Image;
// import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class View extends JFrame{
  private Model model;
  private SearchPanel searchPanel;
  JPanel testPanel = new JPanel();
    JLabel label = new JLabel();

  public View(Model m, String title){
    this.setTitle(title);
    model = m;
    this.setLayout(new GridLayout(1,2));
    searchPanel = new SearchPanel();
    // SearchPanel searchPanel_2 = new SearchPanel();
    this.add(searchPanel);
    // this.add(searchPanel_2);
    Image image =null;
    // try{
    // image = ImageIO.read(new File("no_image.png"));
    // ImageIcon icon = new ImageIcon(image);
    // JLabel label = new JLabel("aaa");
    // JPanel testPanel = new JPanel();
    testPanel.add(label);
    this.add(testPanel);
    // }catch(Exception error){}

    this.setSize(600,400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  public String getSearchText(){
    return searchPanel.getSearchText();
  }
  public JButton getSearchButton(){
    return searchPanel.getSearchButton();
  }
  public void showImage(Image image){
    label.setIcon(new ImageIcon(image));
  }
  
}
class SearchPanel extends JPanel{
  JButton []btn = new JButton[5];//ボタン配列
  JButton button;
  JTextField text;
	public SearchPanel(){
		JPanel panel = new JPanel();
    text = new JTextField(10);
    JLabel label = new JLabel("ANIME title");
    JLabel dummy = new JLabel("");
    button = new JButton("検索");
    panel.setLayout(new GridLayout(2,2));
    panel.add(label);
    panel.add(dummy);
    panel.add(text);
    panel.add(button);
    this.add(panel);
	}
  public String getSearchText(){
    return text.getText();
  }
  public JButton getSearchButton(){
    return button;
  }
  
}

// class SearchResultPanel extends JPanel {
//   // String title = new String();
//   public SearchResultPanel(String title,String production){
//     // this.title = title;
//     JLabel l_title  = title
//     JLabel l_productipn = production;
//     JButton b_favorite = new JButton();
    
//   }
// }
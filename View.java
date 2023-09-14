import javax.swing.*;
import java.awt.*;
// import java.awt.Image;
// import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Image;
import javax.swing.JScrollPane;
// import java.awt.Container;

// import java.awt.BorderLayout;
// import java.util.ArrayList;
// import java.awt.GridBagLayout;
// import java.awt.GridBagConstraints;
// import java.awt.BorderLayout;

class View extends JFrame{
  private Model model;
  private SearchPanel searchPanel;
  private SearchResultPanel searchResultPanel;
  JLabel testlabel = new JLabel("aaa");
  public View(Model m, String title){
    this.setTitle(title);
    model = m;
    
    this.setLayout(new GridLayout(1,2));
    searchPanel = new SearchPanel(model);
    this.add(searchPanel);

    searchResultPanel = new SearchResultPanel(model);
    // SearchResultPanel.setPreferredSize(new Dimension(200, 100));
    LineBorder border = new LineBorder(Color.BLUE, 2, true);
    searchResultPanel.setBorder(border);
    // searchResultPanel.add(testlabel);
    this.add(searchResultPanel);
    Image image =null;
    // try{
    // image = ImageIO.read(new File("no_image.png"));
    // ImageIcon icon = new ImageIcon(image);
    // JLabel label = new JLabel("aaa");
    // JPanel testPanel = new JPanel();
    // testPanel.add(label);
    // this.add(testPanel);
    // }catch(Exception error){}

    this.setSize(600,400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  public void test(){
    // Container contentPane = getContentPane();
    // getContentPane().removeAll();
    // JLabel testlabel_2 = new JLabel("bbb");
    // this.add(testlabel_2);
    // validate();//更新
    // repaint();
    // System.out.println(contentPane);
  }
  public String getSearchText(){
    return searchPanel.getSearchText();
  }
  public JButton getSearchButton(){
    return searchPanel.getSearchButton();
  }
  public void showImage(Image image){
    // testlabel.setIcon(new ImageIcon(image));
  }

  public void displaySearchList(){
    searchPanel.displaySearchList();
  }
  
  public JButton[] getSearchListLabel(){
    return searchPanel.getSearchListLabel();
  }
  public void displayDetail(String title,Image img){
    searchResultPanel.displayDetail(title,img);
  }
  public JButton getFavoriteButton(){
    return searchResultPanel.getFavoriteButton();
  }
}
class SearchResultPanel extends JPanel{
  JPanel headerPanel = new JPanel();
  JPanel contentsPanel = new JPanel();
  JButton b_favorite;
  Model model;
	public SearchResultPanel(Model m){
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
		model = m;
    // LineBorder border_2 = new LineBorder(Color.YELLOW, 2, true);
    // panel.setBorder(border_2);
    // panel.setLayout(layout);
    this.setLayout(layout);

    LineBorder border = new LineBorder(Color.RED, 2, true);
    headerPanel.setBorder(border);
    gbc.gridx = 0;
    gbc.gridy = 0;
    // gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 0.2d;
    gbc.weightx = 1.0d;
    // gbc.anchor = GridBagConstraints.EAST;
    layout.setConstraints(headerPanel,gbc);
    
    JScrollPane scrollPane = new JScrollPane(contentsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    contentsPanel.setBorder(border);
    // contentsPanel.setBorder(border);
    gbc.gridy = 1;
    // gbc.gridheight = 4;
    gbc.weighty = 1.0d;
    // gbc.fill = GridBagConstraints.BOTH;
    // gbc.fill = GridBagConstraints.NONE;
    // gbc.anchor = GridBagConstraints.CENTER;
    layout.setConstraints(scrollPane,gbc);

    // JButton testlabel = new JButton("test");
    // gbc.gridy = 2;
    // gbc.weighty = 0.2d;
    // // gbc.gridheight = 3;
    // gbc.fill = GridBagConstraints.BOTH;
    // gbc.anchor = GridBagConstraints.SOUTHWEST;
    // layout.setConstraints(testlabel,gbc);

    this.add(headerPanel);
    this.add(scrollPane);
    // this.add(testlabel);
    // System.out.println("before:"+getComponents()[0]);
    

    // for(int i=0;i<getComponents().length;i++){
    //   System.out.println("before:"+getComponents()[i]);
    // }
    // this.add(panel);
    // validate();//更新
    // repaint();
  }
  public void displayDetail(String title,Image img){
    removeComponent();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);
    
    Image image = null;
    image = img;
    ImageIcon icon = new ImageIcon(image);
    JLabel l_image = new JLabel();
    Image resizeIcon = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * 0.1), -1,Image.SCALE_SMOOTH);
    l_image.setIcon(new ImageIcon(resizeIcon));
    gbc.gridx = 1;
    gbc.gridy = 0;
    layout.setConstraints(l_image,gbc);

    
    JLabel l_title = new JLabel(title);
    l_title.setFont(new Font("Century", Font.ITALIC, 21));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.gridheight = 1;
    layout.setConstraints(l_title,gbc);
    

    b_favorite = new JButton("お気に入り");
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    layout.setConstraints(b_favorite,gbc);


    headerPanel.add(l_image);
    headerPanel.add(l_title);
    headerPanel.add(b_favorite);

    contentsPanel.setLayout(layout);
    JLabel head_seiyuu = new JLabel("声優 : " + "キャラクター");
    ArrayList<String[]> seiyuuList = model.getSeiyuuCharacter(title);
    for(int i=0; i<seiyuuList.size()-1 ;i++){
      JLabel seiyuu_character_unit = new JLabel(seiyuuList.get(i+1)[0] + " : "+seiyuuList.get(i+1)[1]);
      gbc.gridx = 0;
      gbc.gridy = i;
      
      layout.setConstraints(seiyuu_character_unit,gbc);
      contentsPanel.add(seiyuu_character_unit);
      // System.out.println(seiyuuList.get(i+1)[0] + " : "+seiyuuList.get(i+1)[1]);
    }
    
    validate();//更新
    repaint();
  }
  public JButton getFavoriteButton(){
    return b_favorite;
  }
  public void removeComponent(){
    headerPanel.removeAll();
    contentsPanel.removeAll();
  }
  
}
class SearchPanel extends JPanel{
  JButton b_search;
  JTextField text;
  JPanel panel = new JPanel();
  GridBagLayout layout = new GridBagLayout();
  GridBagConstraints gbc = new GridBagConstraints();
  JButton[] b_searchSuggestion;
  Model model;
	public SearchPanel(Model m){
		model = m;
    panel.setLayout(layout);

    text = new JTextField(10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    layout.setConstraints(text,gbc);

    JLabel l_title = new JLabel("ANIME title");
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_title,gbc);

    b_search = new JButton("検索");
    gbc.gridx = 1;
    gbc.gridy = 1;
    layout.setConstraints(b_search,gbc);

    panel.add(l_title);
    panel.add(text);
    panel.add(b_search);
    this.add(panel);
	}
  public String getSearchText(){
    return text.getText();
  }
  public JButton getSearchButton(){
    return b_search;
  }
  public void displaySearchList(){
    b_searchSuggestion = new JButton[4];
    gbc.gridx = 0;
    // int count = 0;
    int searchWidth = text.getWidth();
    int searchHeight = text.getHeight();

    Map<String,Integer>  searchSuggestions = model.getTmpSearchSuggestions();
    // String[] searchSuggestions = new String[tmp.size()];
    int count = 0;
    for(String title : searchSuggestions.keySet()) {
        // System.out.println(title)
    // }
    // for(;count<searchSuggestions.size();count++){
      // searchSuggestions[i] = tmp;
      if (count > 3){
        break;
      }
      b_searchSuggestion[count] = new JButton(title);
      gbc.fill = GridBagConstraints.BOTH;
      b_searchSuggestion[count].setBorder(new LineBorder(Color.BLACK, 1, false));
      gbc.gridy = count + 2;
      layout.setConstraints(b_searchSuggestion[count], gbc);
      panel.add(b_searchSuggestion[count]);
      count++;
    }

    
    // // System.out.println(searchWidth);
    // for(String title: searchSuggestions){
      
      
    //   count++;
    // }
    
    if (searchSuggestions.size() > 4){
      JLabel moreSearchList = new JLabel("検索結果をさらに表示");
      // test_2.setBorder(new LineBorder(Color.RED, 2, false));
      gbc.gridy = count + 2;
      layout.setConstraints(moreSearchList, gbc);
      panel.add(moreSearchList);
    }

    validate();//更新
    repaint();

  }
  public JButton[] getSearchListLabel(){
    return b_searchSuggestion;
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
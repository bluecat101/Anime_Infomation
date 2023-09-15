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
// import java.awt.image.BufferedImage;

// import java.awt.Container;

// import java.awt.BorderLayout;
// import java.util.ArrayList;
// import java.awt.GridBagLayout;
// import java.awt.GridBagConstraints;
// import java.awt.BorderLayout;

class View extends JFrame{
  private Model model;
  private SearchPanel searchPanel;
  private FunctionPanel functionPanel;
  private SearchResultPanel searchResultPanel;
  JLabel testlabel = new JLabel("aaa");
  public View(Model m, String title){
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setTitle(title);
    model = m;
    
    LineBorder border;

    this.setLayout(layout);
    gbc.fill = GridBagConstraints.BOTH;
    searchPanel = new SearchPanel(model);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0d;
    gbc.weighty = 1.0d;
    layout.setConstraints(searchPanel,gbc);
    border = new LineBorder(Color.BLUE, 2, true);
    searchPanel.setBorder(border);
    
    functionPanel = new FunctionPanel(model);
    gbc.gridx = 0;
    gbc.gridy = 1;
    layout.setConstraints(functionPanel,gbc);
    border = new LineBorder(Color.YELLOW, 2, true);
    functionPanel.setBorder(border);



    searchResultPanel = new SearchResultPanel(model);
    // SearchResultPanel.setPreferredSize(new Dimension(200, 100));
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.weightx = 2.0d;
    layout.setConstraints(searchResultPanel,gbc);
    border = new LineBorder(Color.BLACK, 2, true);
    searchResultPanel.setBorder(border);
    
    this.add(searchPanel);
    this.add(functionPanel);
    this.add(searchResultPanel);


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
  public JPanel getSearchWindowPanel(){
    return searchPanel.getSearchWindowPanel();
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
  public void displaySearchIndex(){
    searchResultPanel.displaySearchIndex();
  }
  public JPanel getcontentsPanel(){
    return searchResultPanel.getcontentsPanel();
  }
  public JButton getFavoriteIndexButton(){
    return functionPanel.getFavoriteIndexButton();
  }
  public void displayFavoriteIndex(){
    searchResultPanel.displayFavoriteIndex();
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
    this.setLayout(layout);

    LineBorder border = new LineBorder(Color.RED, 2, true);
    headerPanel.setBorder(border);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 0.2d;
    gbc.weightx = 1.0d;
    layout.setConstraints(headerPanel,gbc);
    
    JScrollPane scr_seiyuu = new JScrollPane(contentsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    contentsPanel.setBorder(border);
    gbc.gridy = 1;
    gbc.weighty = 1.0d;
    layout.setConstraints(scr_seiyuu,gbc);


    this.add(headerPanel);
    this.add(scr_seiyuu);
    // validate();//更新
    // repaint();
  }
  public void displayDetail(String title,Image img){
    removeComponent();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);

    
    JLabel l_image = setImage(img, 0.1);

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
  public void displaySearchIndex(){
    removeComponent();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);

    Map<String,Integer>  searchSuggestions = model.getTmpSearchSuggestions();
    String searchText = "";
    for(Map.Entry<String,Integer> searchAnime : searchSuggestions.entrySet()) {
      if(searchAnime.getValue() == -1){
        searchText = searchAnime.getKey();
      }
    }
    JLabel l_searchText = new JLabel("検索内容 : " + searchText);
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_searchText,gbc);
    headerPanel.add(l_searchText);

    contentsPanel.setLayout(layout);
    JLabel l_section = new JLabel("検索結果一覧");
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_section,gbc);
    contentsPanel.add(l_section);
    // ArrayList<String[]> seiyuuList = model.getSeiyuuCharacter(title);
    int i = 1;
    for(Map.Entry<String,Integer> searchAnime : searchSuggestions.entrySet()) {
      if(searchAnime.getValue() == -1){
        continue;
      }
      JPanel p_searchAnime_unit = new JPanel();
      p_searchAnime_unit.setLayout(new BorderLayout());
      JLabel l_title = new JLabel(searchAnime.getKey());
      JLabel l_image = null;
      try{
        l_image = setImage(model.getAnimeImage(searchAnime.getValue()), 0.05);
      }catch(Exception error){}
      l_image.setHorizontalAlignment(JLabel.CENTER);
      p_searchAnime_unit.add(l_title, BorderLayout.NORTH);
      p_searchAnime_unit.add(l_image, BorderLayout.CENTER);

      gbc.gridx = 0;
      gbc.gridy = i;
      layout.setConstraints(p_searchAnime_unit,gbc);
      contentsPanel.add(p_searchAnime_unit);
      i++;
    }
    
    validate();//更新
    repaint();
  }
  private JLabel setImage(Image img, Double size){
    Image image = null;
    image = img;
    ImageIcon icon = new ImageIcon(image);
    JLabel l_image = new JLabel();
    Image resizeIcon = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * size), -1,Image.SCALE_SMOOTH);
    l_image.setIcon(new ImageIcon(resizeIcon));
    return l_image;
  }
  public JPanel getcontentsPanel(){
    return contentsPanel;
  }
  public void displayFavoriteIndex(){
    removeComponent();
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);

    JLabel l_head = new JLabel("お気に入り一覧");
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_head,gbc);
    headerPanel.add(l_head);

    contentsPanel.setLayout(layout);
    int i = 0;
    for(String[] favoriteAnime: model.getFavoriteAnime()){
      JPanel p_favoriteAnime_unit = new JPanel();
      p_favoriteAnime_unit.setLayout(new BorderLayout());
      JLabel l_title = new JLabel(favoriteAnime[0]);
      JLabel l_image = null;
      try{
        Image image = null;
        File file = new File("image/" + favoriteAnime[1] + ".jpg");
        if(file.exists()){
          image = ImageIO.read(new File("image/" + favoriteAnime[1] + ".jpg"));
        }else{
          image = ImageIO.read(new File("image/no_image.png"));
        }
        l_image = setImage(image, 0.07);
      }catch(Exception error){}
      l_image.setHorizontalAlignment(JLabel.CENTER);
      p_favoriteAnime_unit.add(l_title, BorderLayout.NORTH);
      p_favoriteAnime_unit.add(l_image, BorderLayout.CENTER);

      gbc.gridx = 0;
      gbc.gridy = i;
      layout.setConstraints(p_favoriteAnime_unit,gbc);
      contentsPanel.add(p_favoriteAnime_unit);
      i++;
    }

    validate();//更新
    repaint();
  }
}
class SearchPanel extends JPanel{
  JButton b_search;
  JTextField text;
  JPanel p_searchWindow = new JPanel();
  GridBagLayout layout = new GridBagLayout();
  GridBagConstraints gbc = new GridBagConstraints();
  JButton[] b_searchSuggestion;
  Model model;
	public SearchPanel(Model m){
		model = m;
    p_searchWindow.setLayout(layout);

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

    p_searchWindow.add(l_title);
    p_searchWindow.add(text);
    p_searchWindow.add(b_search);
    this.add(p_searchWindow);
	}
  public String getSearchText(){
    return text.getText();
  }
  public JButton getSearchButton(){
    return b_search;
  }
  public JPanel getSearchWindowPanel(){
    return p_searchWindow;
  }

  public void displaySearchList(){
    for(int i = p_searchWindow.getComponentCount()-1; i>= 3;i--){
      p_searchWindow.remove(i);
    }
    b_searchSuggestion = new JButton[4];
    gbc.gridx = 0;
    int searchWidth = text.getWidth();
    int searchHeight = text.getHeight();

    Map<String,Integer>  searchSuggestions = model.getTmpSearchSuggestions();
    
    int count = 0;
    for(String title : searchSuggestions.keySet()) {
      if(searchSuggestions.get(title) == -1){
        continue;
      }
      if (count > 3){
        break;
      }
      b_searchSuggestion[count] = new JButton(title);
      gbc.fill = GridBagConstraints.BOTH;
      b_searchSuggestion[count].setBorder(new LineBorder(Color.BLACK, 1, false));
      gbc.gridy = count + 2;
      layout.setConstraints(b_searchSuggestion[count], gbc);
      p_searchWindow.add(b_searchSuggestion[count]);
      count++;
    }
    // System.out.println(panel.getComponentCount());

    
    
    if (searchSuggestions.size() > 4){
      JButton moreSearchList = new JButton("検索結果をさらに表示");
      // test_2.setBorder(new LineBorder(Color.RED, 2, false));
      gbc.gridy = count + 2;
      layout.setConstraints(moreSearchList, gbc);
      p_searchWindow.add(moreSearchList);
    }

    validate();//更新
    repaint();

  }
  public JButton[] getSearchListLabel(){
    return b_searchSuggestion;
  } 
}
class FunctionPanel extends JPanel{
  Model model;
  JButton b_favoriteIndex;
  public FunctionPanel(Model m){
    model = m;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    b_favoriteIndex = new JButton("お気に入り一覧");
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(b_favoriteIndex,gbc);
    this.add(b_favoriteIndex);
  }
  public JButton getFavoriteIndexButton(){
    return b_favoriteIndex;
  }
}
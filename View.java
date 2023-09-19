import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class View extends JFrame implements WindowListener{
  private Model model;
  private SearchPanel searchPanel;
  private FunctionPanel functionPanel;
  private SearchResultPanel searchResultPanel;
  public View(Model m, String title){ // コンストラクタ
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setTitle(title); // 画面にタイトルをつける
    model = m;
    
    LineBorder border; // 枠線用
    this.setLayout(layout);
    gbc.fill = GridBagConstraints.BOTH;
    searchPanel = new SearchPanel(model); // 検索パネルの追加
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0d;
    gbc.weighty = 1.0d;
    layout.setConstraints(searchPanel,gbc); // layoutに追加
    border = new LineBorder(Color.BLUE, 2, true); // 枠線の追加
    searchPanel.setBorder(border);
    
    functionPanel = new FunctionPanel(model); // 機能パネルの追加
    gbc.gridx = 0;
    gbc.gridy = 1;
    layout.setConstraints(functionPanel,gbc); // layoutに追加
    border = new LineBorder(Color.YELLOW, 2, true); // 枠線の追加
    functionPanel.setBorder(border);



    searchResultPanel = new SearchResultPanel(model); // 検索結果表示パネルの追加
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.weightx = 2.0d;
    layout.setConstraints(searchResultPanel,gbc); // layoutに追加
    border = new LineBorder(Color.BLACK, 2, true); // 枠線の追加
    searchResultPanel.setBorder(border);
    
    this.add(searchPanel);        // このフレームに検索パネルを追加
    this.add(functionPanel);      // このフレームに機能パネルを追加
    this.add(searchResultPanel);  // このフレームに検索結果表示パネルを追加


    this.setSize(600,400);
    this.addWindowListener(this); // windowの反応の付与
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  public void windowOpened(WindowEvent e){
  }

  public void windowClosing(WindowEvent e){
    model.updateDB(); // ウィンドウを閉じるときにデータベースの更新
  }

  public void windowClosed(WindowEvent e){
  }

  public void windowIconified(WindowEvent e){
  }

  public void windowDeiconified(WindowEvent e){
  }

  public void windowActivated(WindowEvent e){
  }

  public void windowDeactivated(WindowEvent e){
  }

  public JPanel getSearchWindowPanel(){ // 検索画面のパネルを返す
    return searchPanel.getSearchWindowPanel();
  }
  public String getSearchText(){ // 検索内容を返す
    return searchPanel.getSearchText();
  }
  public JButton getSearchButton(){ // 検索ボタンを返す
    return searchPanel.getSearchButton();
  }

  public void displaySearchList(){ // 検索結果表示を返す 
    searchPanel.displaySearchList();
  }
  public void displaySearch(){
    searchPanel.displaySearch();
  }
  public void displayDetail(String title,Image img){ // アニメを表示する
    searchResultPanel.displayDetail(title,img);
  }
  public JButton getFavoriteButton(){ // 「お気に入りボタン」を返す
    return searchResultPanel.getFavoriteButton();
  }
  public void displaySearchIndex(){ // 全ての検索結果を表示する
    searchResultPanel.displaySearchIndex();
  }
  public String getDisplayTitle(){ // 表示されているアニメのタイトルを返す
    return searchResultPanel.getDisplayTitle();
  }
  public JPanel getcontentsPanel(){ // 表示されているアニメの内容を返す
    return searchResultPanel.getcontentsPanel();
  }
  public void displayFavoriteIndex(){ // お気に入り一覧を表示する
    searchResultPanel.displayFavoriteIndex();
  }
  public JButton getFavoriteIndexButton(){ // 「お気に入り一覧ボタン」を返す
    return functionPanel.getFavoriteIndexButton();
  }
  public JButton getSearchFunctionButton(){
    return functionPanel.getSearchFunctionButton();
  }
}
class SearchResultPanel extends JPanel{ // 検索結果表示パネル
  JPanel headerPanel = new JPanel(); // ヘッダー部分のパネル
  JPanel contentsPanel = new JPanel(); // 内容部分のパネル
  JButton b_favorite;
  Model model;
	public SearchResultPanel(Model m){ // コンストラクタ
		model = m;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);

    /* ヘッダー */
    LineBorder border = new LineBorder(Color.RED, 2, true); // 枠線の追加
    headerPanel.setBorder(border); 
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 0.2d;
    gbc.weightx = 1.0d;
    layout.setConstraints(headerPanel,gbc);
    
    /* 内容 */
    JScrollPane scr_seiyuu = new JScrollPane(contentsPanel);
    gbc.gridy = 1;
    gbc.weighty = 1.0d;
    layout.setConstraints(scr_seiyuu,gbc);

    this.add(headerPanel); // ヘッダーの追加
    this.add(scr_seiyuu);  // 声優の表示
  }
  public void displayDetail(String title,Image img){ // アニメの詳細表示
    removeComponent(); // すでに表示中のコンテンツを削除
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    /* ヘッダー用 */
    headerPanel.setLayout(layout);
    /* 画像の表示 */
    JLabel l_image = setImage(img, 0.1);
    gbc.gridx = 1;
    gbc.gridy = 0;
    layout.setConstraints(l_image,gbc);
    
    /* タイトルの表示 */
    JLabel l_title = new JLabel(title);
    l_title.setFont(new Font("Century", Font.ITALIC, 21)); // タイトルのサイズ変更
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.gridheight = 1;
    layout.setConstraints(l_title,gbc);
    
    /* お気に入りボタンの表示 */
    b_favorite = new JButton("お気に入り");
    for(String[] favoriteAnime_unit:model.getFavoriteAnime()){
      if(title.equals(favoriteAnime_unit[0])){ // すでにお気に入りなら「お気に入り解除」と表示
        b_favorite = new JButton("お気に入り解除");
      }
    }
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    layout.setConstraints(b_favorite,gbc);


    headerPanel.add(l_image);    // 画像の追加
    headerPanel.add(l_title);    // タイトルの追加
    headerPanel.add(b_favorite); // お気に入りボタンの追加

    /* 表示内容の用 */
    contentsPanel.setLayout(layout);
    /* 声優の表示 */
    JLabel head_seiyuu = new JLabel("声優 : " + "キャラクター");
    ArrayList<String[]> seiyuuList = model.getSeiyuuCharacter(title);
    if (seiyuuList != null){
      for(int i=0; i<seiyuuList.size()-1 ;i++){
        JLabel l_seiyuu_character_unit = new JLabel(seiyuuList.get(i+1)[0] + " : "+seiyuuList.get(i+1)[1]);
        gbc.gridx = 0;
        gbc.gridy = i;
        layout.setConstraints(l_seiyuu_character_unit,gbc); // レイアウトの追加
        contentsPanel.add(l_seiyuu_character_unit);
      }
    }else{ // 声優が見つからなかった場合
      JLabel l_message = new JLabel("声優が見つかりませんでした。");
      gbc.gridx = 0;
      gbc.gridy = 0;
      layout.setConstraints(l_message,gbc); // レイアウトの追加
      contentsPanel.add(l_message);
    }
    validate(); // 更新
    repaint();  // 再描画
  }

  public void displaySearchIndex(){ // 全ての検索結果一覧を表示する
    removeComponent(); // すでに表示中のコンテンツを削除
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);

    Map<String,Integer>  searchSuggestions = model.getTmpSearchSuggestions(); // 全ての検索結果の取得
    String searchText = "";
    for(Map.Entry<String,Integer> searchAnime : searchSuggestions.entrySet()) {
      if(searchAnime.getValue() == -1){
        searchText = searchAnime.getKey(); // 検索内容の取得
      }
    }

    /* 検索内容の文字列用 */
    JLabel l_searchText = new JLabel("検索内容 : " + searchText);
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_searchText,gbc); // レイアウトの追加
    headerPanel.add(l_searchText);

    /* 検索結果のアニメ用 */
    contentsPanel.setLayout(layout);
    JLabel l_section = new JLabel("検索結果一覧"); // 見出し
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_section,gbc); // レイアウトの追加
    contentsPanel.add(l_section);
    int count = 1;
    for(Map.Entry<String,Integer> searchAnime: searchSuggestions.entrySet()) { // 全てのアニメを追加する
      if(searchAnime.getValue() == -1){ // 検索内容用の要素の時に飛ばす
        continue;
      }
      displayIndexUnit(searchAnime.getKey(), model.getAnimeImage(searchAnime.getValue(),false), 0.05, layout, gbc, count);
      count++;
    }
    
    validate(); // 更新
    repaint();  // 再描画
  }

  private JLabel setImage(Image img, Double size){ // 画像をラベルに貼る
    Image image = img; // 初期化
    ImageIcon icon = new ImageIcon(image); // 画像をicon化
    JLabel l_image = new JLabel(); 
    Image resizeIcon = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * size), -1,Image.SCALE_SMOOTH); // 画像のサイズ変更
    l_image.setIcon(new ImageIcon(resizeIcon)); // ラベルに画像を貼る
    return l_image;
  }

  public void displayFavoriteIndex(){ // お気に入り一覧の表示
    removeComponent(); // すでに表示中のコンテンツを削除
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    headerPanel.setLayout(layout);

    /* ヘッダー用 */
    JLabel l_head = new JLabel("お気に入り一覧"); // 見出し
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_head,gbc);
    headerPanel.add(l_head);

    /* お気に入り一覧用 */
    contentsPanel.setLayout(layout); 
    int count = 0;
    for(String[] favoriteAnime: model.getFavoriteAnime()){ // 全てのお気に入り一覧を探索
      displayIndexUnit(favoriteAnime[0], model.getImageByDB(favoriteAnime[1]), 0.07, layout, gbc, count);
      count++;
    }
    validate(); // 更新
    repaint();  // 再描画
  }
  private void displayIndexUnit(String title, Image image, Double size, GridBagLayout layout, GridBagConstraints gbc,int count){
    JPanel p_favoriteAnime_unit = new JPanel(); // タイトルと画像を貼るパネルの追加
    p_favoriteAnime_unit.setLayout(new BorderLayout());
    JLabel l_title = new JLabel(title);
    JLabel l_image = setImage(image, size);
    l_image.setHorizontalAlignment(JLabel.CENTER); // 左右の中心になるように追加
    p_favoriteAnime_unit.add(l_title, BorderLayout.NORTH);
    p_favoriteAnime_unit.add(l_image, BorderLayout.CENTER);
    gbc.gridx = 0;
    gbc.gridy = count;
    layout.setConstraints(p_favoriteAnime_unit,gbc); // レイアウトの追加
    contentsPanel.add(p_favoriteAnime_unit);
  }
  public JButton getFavoriteButton(){ // お気に入りボタンを返す
    return b_favorite;
  }

  public String getDisplayTitle(){ // 表示中のアニメのタイトルを返す
   return ((JLabel)headerPanel.getComponent(1)).getText(); 
  }
  
  public JPanel getcontentsPanel(){ // コンテンツパネルを返す
    return contentsPanel;
  }
  
  public void removeComponent(){ // 検索結果の表示中の内容を消す
    headerPanel.removeAll();
    contentsPanel.removeAll();
  }
}
class SearchPanel extends JPanel{ // 検索画面パネル
  JButton b_search;         // 検索ボタン
  JTextField t_searchText;  // 検索内容の文字列
  JPanel p_searchWindow = new JPanel(); // 検索類用のパネル
  GridBagLayout layout = new GridBagLayout();
  GridBagConstraints gbc = new GridBagConstraints();
  JButton[] b_searchSuggestion; // 検索結果を格納する
  Model model;
	public SearchPanel(Model m){
		model = m;
	}

  public String getSearchText(){ // 検索内容を返す
    return t_searchText.getText();
  }

  public JButton getSearchButton(){ // 検索用のボタンを返す
    return b_search;
  }

  public JPanel getSearchWindowPanel(){ // 検索類のウィンドウパネルを返す
    return p_searchWindow;
  }
  public void displaySearch(){
    
    p_searchWindow.setLayout(layout);
    /* 検索内容用 */
    t_searchText = new JTextField(10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    layout.setConstraints(t_searchText,gbc); // レイアウトの追加
    
    /* 検索用のラベル */
    JLabel l_search = new JLabel("ANIME title");
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(l_search,gbc); // レイアウトの追加

    /* 検索用のボタン */
    b_search = new JButton("検索");
    gbc.gridx = 1;
    gbc.gridy = 1;
    layout.setConstraints(b_search,gbc); // レイアウトの追加

    p_searchWindow.add(l_search);     // ラベルの追加
    p_searchWindow.add(t_searchText); // 検索内容テキストの追加
    p_searchWindow.add(b_search);     // 検索ボタンの追加
    this.add(p_searchWindow);
    validate(); // 更新
    repaint();  // 再描画
  }
  public void displaySearchList(){ // 検索結果の表示
    for(int i = p_searchWindow.getComponentCount()-1; i>= 3; i--){ // すでに表示中の検索結果を削除
      p_searchWindow.remove(i);
    }
    b_searchSuggestion = new JButton[4]; // 検索結果のアニメのタイトルボタンを初期化
    gbc.gridx = 0;
    int searchWidth = t_searchText.getWidth();
    int searchHeight = t_searchText.getHeight();

    Map<String,Integer> searchSuggestions = model.getTmpSearchSuggestions(); // 検索結果を取得
    int count = 0;
    for(String title : searchSuggestions.keySet()) {
      if(searchSuggestions.get(title) == -1){ // 検索内容の部分は飛ばす
        continue;
      }
      if (count > 3){ // 5個以上の表示はしない
        break;
      }
      b_searchSuggestion[count] = new JButton(title);
      b_searchSuggestion[count].setBorder(new LineBorder(Color.BLACK, 1, false)); // 枠線の追加
      gbc.fill = GridBagConstraints.BOTH;
      gbc.gridy = count + 2;
      layout.setConstraints(b_searchSuggestion[count], gbc); // レイアウトの追加
      p_searchWindow.add(b_searchSuggestion[count]);
      count++;
    }
    
    if (searchSuggestions.size() > 4){ // 検索結果が5個以上のとき
      JButton b_moreSearchList = new JButton("検索結果をさらに表示(" + searchSuggestions.size() + "件)"); // 検索結果を全て表示する用のボタンの追加
      gbc.gridy = count + 2;
      layout.setConstraints(b_moreSearchList, gbc); // レイアウトの追加
      p_searchWindow.add(b_moreSearchList);
    }
    validate(); // 更新
    repaint();  // 再描画
  }
}
class FunctionPanel extends JPanel{ // 機能パネル
  Model model;
  JButton b_favoriteIndex = new JButton("お気に入り一覧");; // お気に入り一覧ボタン
  JButton b_search = new JButton("検索機能");; // お気に入り一覧ボタン
  public FunctionPanel(Model m){
    model = m;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(layout);
    /* お気に入り一覧 */
    gbc.gridx = 0;
    gbc.gridy = 0;
    layout.setConstraints(b_favoriteIndex,gbc); // レイアウトの追加

    /* 検索機能 */
    gbc.gridx = 1;
    gbc.gridy = 0;
    layout.setConstraints(b_search,gbc); // レイアウトの追加

    this.add(b_favoriteIndex); // 「お気に入り一覧ボタン」の追加
    this.add(b_search);        // 「検索機能ボタン」の追加
  }
  public JButton getFavoriteIndexButton(){ // 「お気に入り一覧ボタン」を返す
    return b_favoriteIndex;
  }
  public JButton getSearchFunctionButton(){ // 「検索機能ボタン」を返す
    return b_search;
  }
}
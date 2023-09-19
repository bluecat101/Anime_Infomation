import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.Map;


class Controller implements ActionListener, MouseListener{
  private Model model; // modelのオブジェクト
  private View view;  // modelのオブジェクト
  private int status; // 0: 検索前, 1:　検索結果, 2: 検索内容表示, 3:検索結果をさらに表示, 4: お気に入り一覧
  private Map<String,Integer> animeTIDDB;
  public Controller(Model m, View v){
    /* 初期化 */
    model = m;
    view = v;
    status = 0;
    view.getFavoriteIndexButton().addActionListener(this); // 「お気に入り一覧ボタン」への付与
    view.getSearchFunctionButton().addActionListener(this); // 「検索機能ボタン」への付与
  }
  
  public void actionPerformed(ActionEvent e){
    System.out.println(e);
    System.out.println(e.getActionCommand());

    //dアニメストア検索
    if(e.getSource() == view.getSearchButton()){ // 検索ボタンが押されたとき
      status = 1; // 状況の変更
      model.searchDAnimeStore(view.getSearchText()); // 検索予測の取得 [0]:id,[1]:title
      view.displaySearchList(); // 表示
      JPanel p_searchWindow = view.getSearchWindowPanel(); // 予測結果のパネルの取得
      for(int i = p_searchWindow.getComponentCount()-1; i>= 3;i--){
        ((JButton)p_searchWindow.getComponent(i)).addActionListener(this); // 検索予測の「各アニメボタン」への付与
      }
    }else if(model.getTmpSearchSuggestions().containsKey(e.getActionCommand())){ // アニメが押されたとき
      status = 2; // 状況の変更
      System.out.println("click");
      clickedAnime(e.getActionCommand()); // 表示等を行う
      /*
      #### //-----------tidを用いた検索-----------//
      ####    // int tid = animeTIDDB.searchTID(view.getSearchText()); //TIDDB検索,すでに存在する場合??
      ####  // int tid = model.searchTID(e.getActionCommand()); // tid検索
      ####  // System.out.println(tid);
      ####  // String[] animeDetail = model.getAnimeDetailByTID(tid); //詳細取得
      ####  // for(String data: animeDetail){
      ####  // System.out.println(data);
      ####  // }
      #### //----------------------//
      */
      // model.getAnimeDetailByDAS(Integer.parseInt(model.getTmpSearchSuggestions().get(e.getActionCommand())));
    }else if(e.getActionCommand().contains("検索結果をさらに表示")){ // 「検索結果をさらに表示」が押された
      status = 3;                                                 // 状況の変更
      view.displaySearchIndex();                                  // 全ての検索結果を表示
      JPanel contentsPanel = view.getcontentsPanel();             // 検索結果表示パネルを取得
      contentsPanel.addMouseListener(this);                       // パネルへのmouse反応の付与
    }else if(e.getActionCommand() == "お気に入り"){           // 「お気に入り」が押されたとき
      String title = view.getDisplayTitle();                      // 表示されているアニメのタイトルの取得
      model.addFavoriteAnime(title);                              // アニメをお気に入りに追加
      clickedAnime(title,model.getTmpAnimeDetail()[1]);           // 表示の更新
    }else if(e.getActionCommand() == "お気に入り解除"){        // 「お気に入り解除」が押されたとき
      String title = view.getDisplayTitle();                      // 表示されているアニメのタイトルの取得
      model.deleteFavoriteAnime(title);                           // アニメをお気に入りから削除する
      clickedAnime(title,model.getTmpAnimeDetail()[1]);           // 表示の更新
    }else if(e.getActionCommand() == "お気に入り一覧"){         // 「お気に入り一覧」が押されたとき
      status = 4;                                                 // 状況の変更
      view.displayFavoriteIndex();                                // お気に入りを表示する
      JPanel contentsPanel = view.getcontentsPanel();             // お気に入り一覧パネルを取得
      contentsPanel.addMouseListener(this);                       // パネルへのmouse反応の付与
      view.displaySearch();                                       // 検索用のものを表示
      view.getSearchButton().addActionListener(this);             // 「検索ボタン」への付与
    }else if(e.getActionCommand() == "検索機能"){              // 「検索機能」が押されたとき
      status = 0;                                                 // 状況の変更
      view.displaySearch();                                       // 検索用のものを表示
      view.getSearchButton().addActionListener(this);             // 「検索ボタン」への付与
    }
    

  }
  public void mouseClicked(MouseEvent e){ // 検索結果一覧表示とお気に入り一覧表示の場合
    System.out.println(e);
    double x = e.getPoint().getX(); // mouseのx座標の取得
    double y = e.getPoint().getY(); // mouseのx座標の取得
    JPanel contentsPanel = view.getcontentsPanel(); // mouseの有効範囲内のパネルの取得
    if(status == 3){ // 検索結果一覧の場合 
      for (Component searchAnime:contentsPanel.getComponents()){ // 表示されているアニメに対して実行
        if(searchAnime.getX() < x && x < (searchAnime.getX() + searchAnime.getWidth()) && searchAnime.getY() < y && y < searchAnime.getY() + searchAnime.getHeight()){ // 範囲内であるか
          clickedAnime(((JLabel)((JPanel)searchAnime).getComponent(0)).getText()); // 押されたアニメを表示
          view.getcontentsPanel().removeMouseListener(this);   // パネルへのmouse反応の削除
        }
      }
    }else if(status == 4){ // お気に入り一覧の場合
      int i = 0;
      for (Component searchAnime:contentsPanel.getComponents()){ // 表示されているアニメに対して実行
        if(searchAnime.getX() < x && x < (searchAnime.getX() + searchAnime.getWidth()) && searchAnime.getY() < y && y < searchAnime.getY() + searchAnime.getHeight()){ // 範囲内であるか
          String[] animeDetail = model.getFavoriteAnime().get(i); // アニメの詳細を取得
          model.setTmpAnimeDetail(animeDetail); // tmpに押されたアニメをセット
          clickedAnime(animeDetail[0],animeDetail[1]); // 押されたアニメの表示
          view.getcontentsPanel().removeMouseListener(this);   // パネルへのmouse反応の削除
          return;
        }
        i++;
      }
    }
    
    
  }

  public void mouseEntered(MouseEvent e){
  }

  public void mouseExited(MouseEvent e){
  }

  public void mousePressed(MouseEvent e){
  }

  public void mouseReleased(MouseEvent e){
  }

  public void clickedAnime(String title){ // 新しいアニメ(お気に入りに追加されていないアニメ)
    status = 2;
    model.setTmpAnimeDetail(title); // tmpにアニメをセット
    String[] animeDetail = model.getTmpAnimeDetail(); //詳細取得
    view.displayDetail(animeDetail[0],model.getAnimeImage(Integer.parseInt(animeDetail[1]),true));  // 画像を取得して表示
    view.getFavoriteButton().addActionListener(this); // 「お気に入りボタン」への付与
  }

  public void clickedAnime(String title, String id){  // お気に入りに入っている(解除時も有効)
    status = 2;
    view.displayDetail(title,model.getImageByDB(id)); // アニメの詳細表示
    view.getFavoriteButton().addActionListener(this); // 「お気に入りボタン」への付与
  }
}

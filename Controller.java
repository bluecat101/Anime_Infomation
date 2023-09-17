import javax.swing.*;
// import java.awt.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

class Controller implements ActionListener, MouseListener{
  private Model model;
  private View view;
  private int status; // 0: 検索前, 1:　検索結果, 2: 検索内容表示, 3:検索結果をさらに表示, 4: お気に入り一覧
  private Map<String,Integer> animeTIDDB;
  public Controller(Model m, View v){
    model = m;
    view = v;
    status = 0;
    view.getSearchButton().addActionListener(this);
    view.getFavoriteIndexButton().addActionListener(this);
    // animeTIDDB = model.getAnimeTIDDB();
  }
  
  public void actionPerformed(ActionEvent e){
    System.out.println(e);
    System.out.println(e.getActionCommand());

    //dアニメストア検索
    if(e.getSource() == view.getSearchButton()){
      status = 1;
      try{
        ArrayList<String[]> searchSuggestions = model.searchDAnimeStore(URLEncoder.encode(view.getSearchText(), "UTF-8")); //[0]id,[1]title
        
        try{
        // view.showImage(model.getAnimeImage(searchID)); //画像表示
        model.setTmpSearchSuggetions(searchSuggestions,view.getSearchText());
        view.displaySearchList();
        // for (JButton label: view.getSearchListLabel()){
        //   label.addActionListener(this);
        // }
        JPanel p_searchWindow = view.getSearchWindowPanel();
        // System.out.println(((JPanel)searchPanel.getComponent(0)).getComponentCount());
        for(int i = p_searchWindow.getComponentCount()-1; i>= 3;i--){
          // panel.remove(i);
          ((JButton)p_searchWindow.getComponent(i)).addActionListener(this);
          System.out.println(i);
        }
        }catch(Exception error){}
      } catch (UnsupportedEncodingException error) {
      }
    }else if(model.getTmpSearchSuggestions().containsKey(e.getActionCommand())){
      status = 2;
      System.out.println("click");
      clickedAnime(e.getActionCommand());
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
    }else if(e.getActionCommand() == "検索結果をさらに表示"){
      status = 3;
      view.displaySearchIndex();
      JPanel contentsPanel = view.getcontentsPanel();
      contentsPanel.addMouseListener(this);
    }else if(e.getActionCommand() == "お気に入り"){
      String title = view.getDisplayTitle();
      model.addFavoriteAnime(title);
      // view.displayDetail(animeDetail[0],model.getAnimeImage(Integer.parseInt(animeDetail[1])));
      // clickedAnime(title); // 保留　
      clickedAnime(title,model.getTmpAnimeDetail()[1]);
    }else if(e.getActionCommand() == "お気に入り解除"){
      String title = view.getDisplayTitle();
      model.deleteFavoriteAnime(title);
      
      // view.displayDetail(title,model.getImageByDB(model.getTmpAnimeDetail()[1]));
      // view.getFavoriteButton().addActionListener(this);
      clickedAnime(title,model.getTmpAnimeDetail()[1]);
    }else if(e.getActionCommand() == "お気に入り一覧"){
      status = 4;
      view.displayFavoriteIndex();
      JPanel contentsPanel = view.getcontentsPanel();
      contentsPanel.addMouseListener(this);
    }
    

  }
  public void mouseClicked(MouseEvent e){
    System.out.println(e);
    // System.out.println(e.getPoint());
    double x = e.getPoint().getX();
    double y = e.getPoint().getY();
    JPanel contentsPanel = view.getcontentsPanel();
    if(status == 3){  
      for (Component searchAnime:contentsPanel.getComponents()){
        if(searchAnime.getX() < x && x < (searchAnime.getX() + searchAnime.getWidth()) && searchAnime.getY() < y && y < searchAnime.getY() + searchAnime.getHeight()){
          clickedAnime(((JLabel)((JPanel)searchAnime).getComponent(0)).getText());
        }
      }
    }else if(status == 4){
      int i = 0;
      for (Component searchAnime:contentsPanel.getComponents()){
        if(searchAnime.getX() < x && x < (searchAnime.getX() + searchAnime.getWidth()) && searchAnime.getY() < y && y < searchAnime.getY() + searchAnime.getHeight()){
          String[] animeDetail = model.getFavoriteAnime().get(i);
          model.setTmpAnimeDetail(animeDetail);
          clickedAnime(animeDetail[0],animeDetail[1]);
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

  public void clickedAnime(String title){
    model.setTmpAnimeDetail(title);
    String[] animeDetail = model.getTmpAnimeDetail(); //詳細取得
    try{
      view.displayDetail(animeDetail[0],model.getAnimeImage(Integer.parseInt(animeDetail[1])));
      view.getFavoriteButton().addActionListener(this);

    }catch(Exception error){}
  }

  public void clickedAnime(String title, String id){// お気に入りに入っている
    view.displayDetail(title,model.getImageByDB(id));
    view.getFavoriteButton().addActionListener(this);
  }
}

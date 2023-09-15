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
  // private Url url;
  private Map<String,Integer> animeTIDDB;
  public Controller(Model m, View v){
    model = m;
    view = v;
    view.getSearchButton().addActionListener(this);
    view.getFavoriteIndexButton().addActionListener(this);
    // animeTIDDB = model.getAnimeTIDDB();
  }
  
  public void actionPerformed(ActionEvent e){
    System.out.println(e);
    System.out.println(e.getActionCommand());

    //dアニメストア検索
    if(e.getSource() == view.getSearchButton()){
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
      view.displaySearchIndex();
      JPanel contentsPanel = view.getcontentsPanel();
      contentsPanel.addMouseListener(this);
    }else if(e.getActionCommand() == "お気に入り"){
      model.addFavoriteAnime();
    }else if(e.getActionCommand() == "お気に入り一覧"){
      view.displayFavoriteIndex();
    }
    

  }
  public void mouseClicked(MouseEvent e){
    System.out.println(e);
    System.out.println(e.getPoint());
    double x = e.getPoint().getX();
    double y = e.getPoint().getY();
    JPanel contentsPanel = view.getcontentsPanel();
    for (Component searchAnime:contentsPanel.getComponents()){
      if(searchAnime.getX() < x && x < (searchAnime.getX() + searchAnime.getWidth()) && searchAnime.getY() < y && y < searchAnime.getY() + searchAnime.getHeight()){
        // System.out.println(((JLabel)((JPanel)searchAnime).getComponent(0)).getText());
        clickedAnime(((JLabel)((JPanel)searchAnime).getComponent(0)).getText());
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
}

import javax.swing.*;
// import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

class Controller implements ActionListener{
  private Model model;
  private View view;
  // private Url url;
  private Map<String,Integer> animeTIDDB;
  public Controller(Model m, View v){
    model = m;
    view = v;
    view.getSearchButton().addActionListener(this);
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
        model.setTmpSearchSuggetions(searchSuggestions);
        view.displaySearchList();
        for (JButton label: view.getSearchListLabel()){
          label.addActionListener(this);
        }
        }catch(Exception error){}
      } catch (UnsupportedEncodingException error) {
      }
    }else if(model.getTmpSearchSuggestions().containsKey(e.getActionCommand())){
      System.out.println("click");
      
      String[] animeDetail = model.getAnimeDetail(e.getActionCommand()); //詳細取得
        // for(String data: animeDetail){
        //   System.out.println(data);
        // }
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
    }
    

  }
  
}

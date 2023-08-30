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
    System.out.printf("%s",view.getSearchText());
    //// int tid = animeTIDDB.searchTID(view.getSearchText()); //TIDDB検索,すでに存在する場合??
    // int tid = model.searchTID(view.getSearchText()); // tid検索
    // System.out.println(tid);
    // String[] animeDetail = model.getAnimeDetailByTID(tid); //詳細取得

    //dアニメストア検索
    try{
      ArrayList<String> searchSuggestions = model.searchDAnimeStore(URLEncoder.encode(view.getSearchText(), "UTF-8")); // dアニメストア検索
      // view.searchSuggestionsPanel(searchSuggestions);
    } catch (UnsupportedEncodingException error) {

    }
  }
  
}

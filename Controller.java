import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


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
    // int tid = animeTIDDB.searchTID(view.getSearchText());
    int tid = model.searchTID(view.getSearchText());
    System.out.println(tid);
    
    Url url = new Url();
    java.util.List<String> contents = new ArrayList<String>();
    contents = url.connectHttp("https://cal.syoboi.jp/db.php?Command=TitleLookup&TID="+tid);
    for(String list: contents){
      System.out.println(list);
    }
  }
}

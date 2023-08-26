import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;



class Main{
  public static final String STATUS = "develop";
  public static void main(String argv[]){
    Model m =new Model(getAnimeDB());

    View v = new View(m,"anime");
    new Controller(m,v);
  }
  private static java.util.List<String> getAnimeDB(){
    if (STATUS == "develop"){
      Url url = new Url();
      return url.connectHttp("https://cal.syoboi.jp/list");
    }else{
      java.util.List<String> contents = new ArrayList<String>();
      return contents;
    }
    
  }
   
}

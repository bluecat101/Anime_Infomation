import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

class Main{
  public static void main(String argv[]){
    Model m =new Model(getAnimeDB());

    View v = new View(m,"anime");
    new Controller(m,v);
  }
  private static java.util.List<String> getAnimeDB(){
    java.util.List<String> contents = new ArrayList<String>();
    try{
      Url url = new Url();
      contents = url.getHTML("https://cal.syoboi.jp/list");
      // for(String line: contents){
      //   System.out.println(line);
      // }
        // System.out.println(contents.get(1));

    }catch(Exception error){
      error.printStackTrace();
      System.out.println("can't accsecc anime DataBase");
    }
    return contents;
  }  
}



  



class Url {
  public Url(){}
  java.util.List<String> getHTML(String url) throws Exception {
    URL url_ob = new URI(url).toURL();
    try(
      InputStream is = url_ob.openStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr)){
      java.util.List<String> list = new ArrayList<String>();
        String line = null;
        while((line = br.readLine()) != null){
          list.add(line);
        }
        return list;
    }
  }
}

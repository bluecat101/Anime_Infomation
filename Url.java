import java.net.*;
import java.io.*;
import java.util.*;

class Url {
  public Url(){}
  
  public java.util.List<String> connectHttp(String url_ob){
    java.util.List<String> contents = new ArrayList<String>();
      try{
        contents = this.getHTML(url_ob);
      }catch(Exception error){
        error.printStackTrace();
        System.out.println("can't accsecc anime DataBase");
      }
    return contents;
  }

  public java.util.List<String> getHTML(String url) throws Exception {
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

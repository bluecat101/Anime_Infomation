import java.net.URL;
import java.net.URI;
import java.io.*;
import java.util.ArrayList;
import java.awt.Image;
import javax.imageio.ImageIO;

class Url {
  public Url(){ // コンストラクタ
  }
  
  public ArrayList<String> connectHttp(String url_ob){ // htmlにアクセスする
    ArrayList<String> contents = new ArrayList<String>();
      try{
        contents = this.getHTML(url_ob); // htmlの取得
      }catch(Exception error){
        System.out.println(url_ob+"ににアクセスできませんでした。");
      }
    return contents; // htmlの内容を返す
  }

  public ArrayList<String> getHTML(String uri) throws Exception { // htmlの内容を返す
    URL url = new URI(uri).toURL();
    try(
      InputStream is = url.openStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr)){
      ArrayList<String> list = new ArrayList<String>(); // 内容を保存する用のArrayList
      String line = null;
      while((line = br.readLine()) != null){ // 各行の内容を取得
        list.add(line);
      }
      return list;
    }
  }

  public Image getImage(String uri){ // URLから画像を取得
    Image image = null;
    try{
      URL url = new URI(uri).toURL(); 
      image = ImageIO.read(url); // 画像を読み込む
    }catch(Exception error){
      System.out.println("画像を取得できませんでした。");
    }
    return image;
  }
}

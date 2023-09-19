import java.util.*;
import java.util.regex.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

class Model{
  ArrayList<ArrayList<String[]>> seiyuu_character = new ArrayList<>(); // 各アニメのキャラクター(ArrayList<各アニメArraylist[キャラクター,声優]>各アニメArrayListの先頭に[タイトル,id]を入れておく)
  Map<String,ArrayList<String>> seiyuu = new HashMap<>(); //　声優に対するキャラクター(Map<声優,ArrayList<キャラクター>>)
  ArrayList<String[]> favoriteAnime = new ArrayList<>();     // お気に入りのアニメ
  Map<String,Integer> tmpSearchSuggestions = new HashMap<>(); // 現在検索結果の一覧表示(Map<タイトル,id>)最初だけMap<検索内容,-1>
  String[] tmpAnimeDetail; // ]]]]]]
  public Model(){
    readCSVFile(); // 保存された情報の読み込み
  }

  public void searchDAnimeStore(String searchKey) { // 検索ボタンが押されたときに呼び出し
    ArrayList<String[]> searchSuggestions =new ArrayList<String[]>();
    try{
      Url url = new Url();
      List<String> contents = new ArrayList<String>();
      contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/rest/WS000105?length=20&mainKeyVisualSize=2&searchKey=" + URLEncoder.encode(searchKey, "UTF-8") + "&vodTypeList=svod_tvod&_=1693395247779"); // htmlの取得
      ArrayList<String[]> searchResult = getSearchResult(contents, "\"workId\":\"([0-9]+)\",\"workInfo\":\\{\"workTitle\":\"(.*?)\"");
      setTmpSearchSuggetions(searchResult, searchKey); // 検索予測の保存
    }catch (UnsupportedEncodingException error) { // 文字列のEncodeエラー
      System.out.println("文字列をエンコードできませんでした。");
    }
  }
  public void setTmpSearchSuggetions(ArrayList<String[]> searchSuggestions, String searchText){
    resetTmpSearchSuggetions(); // 現在のtmpアニメ情報を初期化
    tmpSearchSuggestions.put(searchText, -1); // 検索内容の保存
    for (String[] data: searchSuggestions){ // 0,1番目の値を逆にして保存
      tmpSearchSuggestions.put(data[1],Integer.parseInt(data[0]));
    }
  }
  public void resetTmpSearchSuggetions(){ // 検索結果予測の削除
    tmpSearchSuggestions.clear();
  }

  public Map<String,Integer> getTmpSearchSuggestions(){ // tmpアニメ情報を返す
    return tmpSearchSuggestions;
  }

  public int getDAnimeID(String title){ // tmp検索予測のタイトルからidを取得
    return tmpSearchSuggestions.get(title);
  }

  public void setTmpAnimeDetail(String title){ // 1つのアニメの情報の一時保存
    tmpAnimeDetail = new String[3]; // [0]:タイトル,[1]:id,[2]:画像のURL
    int id = getDAnimeID(title);
    if(!is_existSeiyuuCharacter(title)){ // すでに登録されていなければ画像のURLを取得
      Url url = new Url();
      List<String> contents = new ArrayList<String>();
      contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/ci_pc?workId=" + id);
      searchAnimeSeiyuuCaracterByDAnimeContents(contents,id,title);
    }
    tmpAnimeDetail[0] = title;
    tmpAnimeDetail[1] = String.valueOf(id);
  }
  public void setTmpAnimeDetail(String[] anime){ // "オーバーロード",すでに情報があるときにtmpアニメ情報にセット
    tmpAnimeDetail = anime;
  }
  public String[] getTmpAnimeDetail(){ // tmpアニメ情報を返す
    return tmpAnimeDetail;
  }
  private boolean is_existSeiyuuCharacter(String title){ // seiyuu_characterにすでにアニメが保存されているかを確認
    for(ArrayList<String[]> seiyuu_character_unit:seiyuu_character){
      if(title.equals(seiyuu_character_unit.get(0)[0])){ // 一致するアニメが存在した。
        return true; 
      }
    }
    return false;
  }

  public void searchAnimeSeiyuuCaracterByDAnimeContents(List<String> contents,int id,String title){ // htmlの内容から声優の取得
    ArrayList<String[]> seiyuu_character_unit = new ArrayList<>();
    seiyuu_character_unit.add(new String[]{title,String.valueOf(id)}); // 初めにタイトルとidを挿入  
    ArrayList<String[]> searchResult = getSearchResult(contents, "(.*?):(.*?)(／|$)", "\\<p\\>\\[キャスト\\]\\<br\\>","\\</p\\>");
    for(String[] result: searchResult){ 
      seiyuu_character_unit.add(result); // 声優-キャラクターを一対一対応で格納
      if(seiyuu.containsKey(result[1])){ // seiyuuに見つけた声優がすでに存在するかを確認
        seiyuu.get(result[1]).add(result[0] + "("+ title +")"); // すでに存在する場合にはキャラクター(+タイトル)のみ追加
      }else{ // 新しい声優の場合
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(result[0] + "("+ title +")");
        seiyuu.put(result[1],tmp); // 声優とキャラクターをセットで追加
      }
    }
    seiyuu_character.add(seiyuu_character_unit); 
  }
  public Image getAnimeImage(int searchID,boolean is_updateTmpAnimeDetailImage){ // アニメidから画像を取得
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/ci?workId=" + searchID); // idからhtmlの内容を取得
    Image image = null;
    try{
      Pattern p_image = Pattern.compile("<meta property=og:image content=\"(.*)\"/>");
      Matcher m_image = p_image.matcher(contents.get(0));
      for(String line: contents){
        m_image = p_image.matcher(line);
        if (m_image.find()){
          image = url.getImage(m_image.group(1)); // 画像のセット
          if(is_updateTmpAnimeDetailImage){
            tmpAnimeDetail[2] = m_image.group(1); // tmpに画像のURLをセット
          }
        }
      }
      if(image == null){ // 画像を見つけられなかった場合にno_imageセット
        image = ImageIO.read(new File("image/no_image.png"));
      }
    }catch(IOException error){
      System.out.println("代替用の画像が見つけることができませんでした。image/no_image.pngがあるかを確認してください。");
    }
    return image;
  }

  public void addFavoriteAnime(String title){ // お気に入りに追加する際に呼び出し
    for(String[] favoriteAnime_unit: favoriteAnime){ // すでにお気に入りかを確認
      if(favoriteAnime_unit[0].equals(title)){ // すでにお気に入りなら何もしない
        return;
      }
    }
    favoriteAnime.add(tmpAnimeDetail); // お気に入りに追加
    try { // 画像の保存
      BufferedImage image = ImageIO.read(new URI(tmpAnimeDetail[2]).toURL()); 
      ImageIO.write(image, "jpeg", new File("image/"+tmpAnimeDetail[1]+".jpg")); // 画像を書き込む
    } catch (Exception e) {
      System.out.println("画像の書き込みに失敗しました。");
    }
  }
  public void deleteFavoriteAnime(String title){ // お気に入り解除
    for(int i = 0;i < favoriteAnime.size();i++){ // 解除するものを探す
      if(title.equals(favoriteAnime.get(i)[0])){
        favoriteAnime.remove(i); // お気に入り解除
      }
    }
  }
  public ArrayList<String[]> getFavoriteAnime(){ // お気に入りのアニメを取得
    return favoriteAnime;
  }
  
  public ArrayList<String[]> getSeiyuuCharacter(String title){ // アニメの声優とキャラクターの取得
    ArrayList<String[]> seiyuuCharacterList = null;
    for(ArrayList<String[]> seiyuu_character_unit:seiyuu_character){
      if(title.equals(seiyuu_character_unit.get(0)[0])){ // 引数のタイトルと等しいものが見つかった時
        seiyuuCharacterList = seiyuu_character_unit;
        break;
      }
    }
    return seiyuuCharacterList; // 見つからなかったらnullを返す
  }
  public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text){ // 文字列から検索する関数(第一引数:文字列, 第二引数:見つける文字列)
    Pattern pattern;
    Matcher match;
    ArrayList<String[]> result = new ArrayList<>();
    pattern = Pattern.compile(pattern_text); // 文字列をPatternオブジェクトへ
    for(String line: text){ // 検索
      match = pattern.matcher(line);
      while(match.find()){
        String[] tmp = new String[match.groupCount()]; // 見つかったらマッチした個数を格納する箱を作る
        for(int i = 0; i < match.groupCount(); i++){
          tmp[i] = match.group(i+1); // 全ての見つかった文字列を入れる
        }
        result.add(tmp);
      }
      
    }
    return result; // 全ての見つけた文字列を返す
  }

  public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text, String start_pattern){// "オーバーロード" (第一引数:文字列, 第二引数:見つける文字列, 第三引数:検索を始める位置)
      Pattern pattern;
      Matcher match;
      ArrayList<String[]> result = new ArrayList<>();
      pattern = Pattern.compile(pattern_text);
      boolean is_start = false; // 始める位置に達しているかどうか
      Pattern p_start = Pattern.compile(start_pattern);
      Matcher m_start;
      for(String line: text){ // 文字列を1行ずつ見る
        if(is_start){ // 検索を始めるフラグが立っているか
          match = pattern.matcher(line);
          while(match.find()){
            String[] tmp = new String[match.groupCount()]; // マッチした文字列の個数を格納する箱を作る
            for(int i = 0; i < match.groupCount(); i++){
              tmp[i] = match.group(i+1); // 全ての見つかった文字列を入れる
            }
            result.add(tmp);
          }
        }else{ // 始めるフラグが立っていない
          m_start = p_start.matcher(line);
          if (m_start.find()){ // 始める文字列があるかを確認
            is_start = true; // 始めるフラグを立てる
          }
        } 
      }
      return result; // 全ての見つけた文字列を返す
    }

    public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text, String start_pattern, String end_pattern){// "オーバーロード" (第一引数:文字列, 第二引数:見つける文字列, 第三引数:検索を始める位置, 第四引数:検索を終える位置)
      Pattern pattern;
      Matcher match;
      ArrayList<String[]> result = new ArrayList<>();
      pattern = Pattern.compile(pattern_text);
      boolean is_start = false; // 検索を始めるフラグ
      Pattern p_start = Pattern.compile(start_pattern);
      Pattern p_end = Pattern.compile(end_pattern);
      Matcher m_start,m_end;
      for(String line: text){
        if(is_start){ // 検索を始めるフラグが立っているか
          m_end = p_end.matcher(line);
          if (m_end.find()){ // 検索を終える文字列が見つかったかどうか
            break;
          }
          match = pattern.matcher(line);
          while(match.find()){
            String[] tmp = new String[match.groupCount()];
            for(int i = 0; i < match.groupCount(); i++){
              tmp[i] = match.group(i+1);
            }
            result.add(tmp);
          }
        }else{ // 検索を始めるフラグが立っていない
          m_start = p_start.matcher(line);
          if (m_start.find()){ // 検索を始める文字列を見つけたか
            is_start = true;
          }
        } 
      }
      return result; // 全ての見つけた文字列を返す
      
    }
  public Image getImageByDB(String id){ // 画像をでーたべーすから取得する
    Image image = null;
    try{
      File file = new File("image/" + id + ".jpg");
      if(file.exists()){ // 画像が存在するか
        image = ImageIO.read(new File("image/" + id + ".jpg"));
      }else{ // 画像が存在しない
        image = ImageIO.read(new File("image/no_image.png")); // no_imageをセット
      }
    }catch(Exception error){
      System.out.println("画像が見つかりませんでした。");
    }
    return image;
  }
  public void updateDB(){ // データベースのアップデート
    writeCSVFile();
  }
  public void writeCSVFile(){ // お気に入り、声優-キャラクター,声優が演じるキャラクター一覧のリストを更新する
    try{
      /* お気に入りの更新 */
      FileWriter csvWriter = null;
      csvWriter = new FileWriter("DB/favoriteAnime.csv");
      for(String[] favoriteAnime_unit:favoriteAnime){
        csvWriter.append(favoriteAnime_unit[0] + "," + favoriteAnime_unit[1] + "," + favoriteAnime_unit[2] + "\n"); // favoriteAnimeを参考に上書き
      } 
      csvWriter.close();

      /* 声優-キャラクターの更新 */
      csvWriter = new FileWriter("DB/seiyuu_character.csv");
      for(ArrayList<String[]> anime_unit:seiyuu_character){
        for(String[] seiyuu_character_unit:anime_unit){ // 型がArrayList<ArrayList<String[]>>であるため2回のループ
          csvWriter.append(seiyuu_character_unit[0] + "," + seiyuu_character_unit[1] + "," ); // seiyuu_characterを参考に上書き
        }
        csvWriter.append("\n");
      } 
      csvWriter.close();

      /* 声優に対するキャラクターの一覧の更新 */
      csvWriter = new FileWriter("DB/seiyuu.csv");
      for(Map.Entry<String,ArrayList<String>> seiyuu_unit: seiyuu.entrySet()) {
        csvWriter.append(seiyuu_unit.getKey() + ","); // MapHash型であるため先頭にKeyの追加
        for(String character: seiyuu_unit.getValue()){
          csvWriter.append(character + ","); // valueの追加
        }
        csvWriter.append("\n");
      } 
      csvWriter.close();
      
    } catch (IOException e) {
      System.out.println("書き込みに失敗しました");
    }
  }
  private void readCSVFile(){ // データベースからファイルの読み込み
    try {
      Path path = null;
      List<String> lines = null;
      /* お気に入りの読み込み */
      path = Paths.get("DB/favoriteAnime.csv");
      lines = Files.readAllLines(path, Charset.forName("UTF-8"));
      for (int i = 0; i < lines.size(); i++) {
        String[] data = lines.get(i).split(","); // ","で分ける
        favoriteAnime.add(data); // 追加
      }

      /* 声優-キャラクターの読み込み */
      path = Paths.get("DB/seiyuu_character.csv");
      lines = Files.readAllLines(path, Charset.forName("UTF-8"));
      for (int i = 0; i < lines.size(); i++) {
        ArrayList<String[]> tmp = new ArrayList<>();
        String[] data = lines.get(i).split(",");// ","で分ける
        for(int j = 0; j< data.length/2; j++){
          tmp.add(new String[]{data[j*2],data[j*2+1]}); 
        }
        seiyuu_character.add(tmp);
      }

      /* 声優に対するキャラクターの一覧の更新 */
      path = Paths.get("DB/seiyuu.csv");
      lines = Files.readAllLines(path, Charset.forName("UTF-8"));
      for (int i = 0; i < lines.size(); i++) {
        ArrayList<String> tmp = new ArrayList<>();
        String[] data = lines.get(i).split(","); // ","で分ける
        for(int j = 1; j<data.length; j++){
          tmp.add(data[j]);
        }
        seiyuu.put(data[0],tmp);
      }
    } catch (IOException e) {
        System.out.println("ファイル読み込みに失敗");
    }
  }
}
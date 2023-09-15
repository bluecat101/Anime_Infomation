import java.util.*;
import java.util.regex.*;
// import javax.imageio.ImageIO;
// import javax.swing.ImageIcon;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
// import javax.imageio.ImageIO;
import java.net.*;


class Model{
  ArrayList<ArrayList<String[]>> seiyuu_character = new ArrayList<>();// 各アニメのキャラクター
  Map<String,ArrayList<String>> seiyuu = new HashMap<>(); //　声優に対するキャラクター
  ArrayList<String[]> favoriteAnime = new ArrayList<>();
  Map<String,Integer> tmpSearchSuggestions = new HashMap<>();
  String[] tmpAnimeDetail;
  public Model(List<String> contents){
    // searchAnimeTID(contents);
  }
  public String[] searchAnimeTitleProductionByContents(List<String> contents){
    String[] title_production = {"",""};
    Pattern p_title = Pattern.compile("<Title>(.*)</Title>");
    Pattern p_production = Pattern.compile(":製作:(.*)$"); //複数ある場合がある
    Matcher m_title;
    Matcher m_production;

    for(String line: contents){
      m_title = p_title.matcher(line);
      m_production = p_production.matcher(line);
      if (title_production[0] == "" && m_title.find()){
        title_production[0] = m_title.group(1);
      }else if(title_production[1] == "" && m_production.find()){
        title_production[1] = m_production.group(1); //複数ある場合がある
      }else if(title_production[0] != "" && title_production[1] != ""){
        break;
      }
    }
    return title_production;
  }
  public void searchAnimeSeiyuuCaracterByContents(List<String> contents){
    ArrayList<String[]> seiyuu_character_unit = new ArrayList<>();
    Pattern pattern = Pattern.compile(":(.*):(.*)$");
    Matcher match;
    boolean is_seiyuu_character = false;
    Pattern p_start = Pattern.compile("\\*キャスト");
    Pattern p_end = Pattern.compile("</Comment>");
    Matcher m_start, m_end;
    for(String line: contents){

      if (is_seiyuu_character){
        m_end = p_end.matcher(line);
        if (m_end.find()){
          is_seiyuu_character = false;
          break;
        }

        match = pattern.matcher(line);
        if(match.find()){
          seiyuu_character_unit.add(new String[] {match.group(1), match.group(2)});
          if(seiyuu.containsKey(match.group((2)))){
            seiyuu.get(match.group(2)).add(match.group(1));
          }else{
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(match.group(1));
            seiyuu.put(match.group(2),tmp);
          }
        }
      }else{
        m_start = p_start.matcher(line);
        if (m_start.find()){
          is_seiyuu_character = true;
        }
      }
    }
    seiyuu_character.add(seiyuu_character_unit);

    // return seiyuu_character;
  }


  public ArrayList<String[]> searchDAnimeStore(String searchKey) {
    ArrayList<String[]> searchSuggestions =new ArrayList<String[]>();
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/rest/WS000105?length=20&mainKeyVisualSize=2&searchKey=" + searchKey + "&vodTypeList=svod_tvod&_=1693395247779");
    // Pattern p_title = Pattern.compile("\"workId\":\"([0-9]+)\",\"workInfo\":\\{\"workTitle\":\"(.*?)\"");
    // Matcher m_title = p_title.matcher(contents.get(0));
    ArrayList<String[]> searchResult = getSearchResult(contents, "\"workId\":\"([0-9]+)\",\"workInfo\":\\{\"workTitle\":\"(.*?)\"",2);
    // while(m_title.find()){
    //   System.out.println(m_title.group(2));
    //   if(checkTitle(m_title.group(2))){
    //     searchSuggestions.add(new String[]{m_title.group(1),m_title.group(2)});
    //   }
    // }
    for(String[] result: searchResult){
      searchSuggestions.add(result);
    }

    
    return searchSuggestions;
  }
  public int getDAnimeID(String title){
    return tmpSearchSuggestions.get(title);
  }

  public void setTmpAnimeDetail(String title){
    tmpAnimeDetail = new String[3];
    int id = getDAnimeID(title);
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/ci_pc?workId=" + id);
    searchAnimeSeiyuuCaracterByDAnimeContents(contents,id,title);
    tmpAnimeDetail[0] = title;
    tmpAnimeDetail[1] = String.valueOf(id);
    // for(Map.Entry<String,ArrayList<String>> data: seiyuu.entrySet()) {
    //   System.out.println(data.getKey());
    //   for(String character: data.getValue()){
    //     System.out.println("    "+character);
    //   }
    // }
    // return tmpAnimeDetail;
  }
  public String[] getTmpAnimeDetail(){
    return tmpAnimeDetail;
  }
  //空:松岡禎丞／白:茅野愛衣
  // public void searchAnimeSeiyuuCaracterByDAnimeContents(List<String> contents){
  //   ArrayList<String[]> seiyuu_character_unit = new ArrayList<>();
  //   Pattern pattern = Pattern.compile("(.*?):(.*?)(／|$)");
  //   Matcher match;
  //   boolean is_seiyuu_character = false;
  //   Pattern p_start = Pattern.compile("\\<p\\>\\[キャスト\\]\\<br\\>");
  //   Matcher m_start;
  //   for(String line: contents){
  //     if (is_seiyuu_character){
  //       match = pattern.matcher(line);
  //       while(match.find()){
  //         seiyuu_character_unit.add(new String[] {match.group(1), match.group(2)});
  //         if(seiyuu.containsKey(match.group((2)))){
  //           seiyuu.get(match.group(2)).add(match.group(1));
  //         }else{
  //           ArrayList<String> tmp = new ArrayList<>();
  //           tmp.add(match.group(1));
  //           seiyuu.put(match.group(2),tmp);
  //         }
  //         System.out.println("(" + match.group(1)+"   "+ match.group(2) + ")"+ match.start()+ "   " + match.end());

  //       }
  //     break;
  //     }else{
  //       m_start = p_start.matcher(line);
  //       if (m_start.find()){
  //         is_seiyuu_character = true;
  //       }
        
  //     }
  //   }
  //   seiyuu_character.add(seiyuu_character_unit);

  //   // return seiyuu_character;
  // }
  public void searchAnimeSeiyuuCaracterByDAnimeContents(List<String> contents,int id,String title){
    ArrayList<String[]> seiyuu_character_unit = new ArrayList<>();
    seiyuu_character_unit.add(new String[]{title,String.valueOf(id)});  
    ArrayList<String[]> searchResult = getSearchResult(contents, "(.*?):(.*?)(／|$)",2,"\\<p\\>\\[キャスト\\]\\<br\\>","\\</p\\>");
    for(String[] result: searchResult){
      seiyuu_character_unit.add(result);
      if(seiyuu.containsKey(result[1])){
        seiyuu.get(result[1]).add(result[0]);
      }else{
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(result[0]);
        seiyuu.put(result[1],tmp);
      }
      // System.out.println("(" + result[0]+"   "+ result[1] + ")");
    }
    seiyuu_character.add(seiyuu_character_unit);

    // return seiyuu_character;
  }
  public Image getAnimeImage(int searchID) throws IOException{
    // System.out.println(searchID);
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    // System.out.println(searchID);
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/ci?workId=" + searchID);

    Pattern p_image = Pattern.compile("<meta property=og:image content=\"(.*)\"/>");
    Matcher m_image = p_image.matcher(contents.get(0));
    Image image = ImageIO.read(new File("image/no_image.png"));

    for(String line: contents){
      m_image = p_image.matcher(line);
      if (m_image.find()){
        try{
          image = url.getImage(m_image.group(1));
          tmpAnimeDetail[2] = m_image.group(1);
        }catch(Exception error){

        }
      }
    }
    return image;
    

  }
  public void resetTmpSearchSuggetions(){
    tmpSearchSuggestions.clear();
  }
  public void setTmpSearchSuggetions(ArrayList<String[]> searchSuggestions, String searchText){
    resetTmpSearchSuggetions();
    tmpSearchSuggestions.put(searchText, -1);
    for (String[] data: searchSuggestions){
      tmpSearchSuggestions.put(data[1],Integer.parseInt(data[0]));
    }
  }
  public Map<String,Integer>  getTmpSearchSuggestions(){
    return tmpSearchSuggestions;
  }

  public void getAnimeDetailByDAS(int id){
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    // System.out.println(searchID);
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/ci_pc?workId="+id);
    
  }


  public void addSeiyuu(List<String> seiyuu){

  }
  public void addCaracter(List<String> character){

  }
  public void addFavoriteAnime(){
    favoriteAnime.add(tmpAnimeDetail);
    
    try {
      BufferedImage image = ImageIO.read(new URI(tmpAnimeDetail[2]).toURL());
      ImageIO.write(image, "jpeg", new File("image/"+tmpAnimeDetail[1]+".jpg"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public ArrayList<String[]> getFavoriteAnime(){
    return favoriteAnime;
  }
  public ArrayList<String[]> getSeiyuuCharacter(String title){
    ArrayList<String[]> seiyuuCharacterList = new ArrayList<>();
    for(ArrayList<String[]> anime:seiyuu_character){
      if(title == anime.get(0)[0]){
        seiyuuCharacterList = anime;
        break;
      }
    }
    return seiyuuCharacterList;
  }


  public void addSeiyuu_FavoriteAnime(List<String> seiyuu,List<String> character){}

    public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text,int pattern_num){
      Pattern pattern;
      Matcher match;
      ArrayList<String[]> result = new ArrayList<>();
      pattern = Pattern.compile(pattern_text);
      for(String line: text){
        match = pattern.matcher(line);
        while(match.find()){
          String[] tmp = new String[pattern_num];
          for(int i = 0; i < pattern_num; i++){
            tmp[i] = match.group(i+1);
          }
          result.add(tmp);
        }
        
      }
      return result;
    }

    public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text,int pattern_num,String start_pattern){
      Pattern pattern;
      Matcher match;
      ArrayList<String[]> result = new ArrayList<>();
      pattern = Pattern.compile(pattern_text);
      boolean is_start = false;
      Pattern p_start = Pattern.compile(start_pattern);
      Matcher m_start;
      for(String line: text){
        if(is_start){
          match = pattern.matcher(line);
          while(match.find()){
            String[] tmp = new String[pattern_num];
            for(int i = 0; i < pattern_num; i++){
              tmp[i] = match.group(i+1);
            }
            result.add(tmp);
          }
        }else{
          m_start = p_start.matcher(line);
          if (m_start.find()){
            is_start = true;
          }
        } 
      }
      return result;  
    }

    public ArrayList<String[]> getSearchResult(List<String> text, String pattern_text,int pattern_num,String start_pattern, String end_pattern){
      Pattern pattern;
      Matcher match;
      ArrayList<String[]> result = new ArrayList<>();
      pattern = Pattern.compile(pattern_text);
      boolean is_start = false;
      Pattern p_start = Pattern.compile(start_pattern);
      Pattern p_end = Pattern.compile(end_pattern);
      Matcher m_start,m_end;
      for(String line: text){
        if(is_start){
          m_end = p_end.matcher(line);
          if (m_end.find()){
            break;
          }
          match = pattern.matcher(line);
          while(match.find()){
            String[] tmp = new String[pattern_num];
            for(int i = 0; i < pattern_num; i++){
              tmp[i] = match.group(i+1);
            }
            result.add(tmp);
          }
          
        }else{
          m_start = p_start.matcher(line);
          if (m_start.find()){
            is_start = true;
          }
        } 
      }
      return result;  
      
    }
  

}

import java.util.*;
import java.util.regex.*;

class Model{
  Map<String,Integer> animeTIDDB = new HashMap<>();
  ArrayList<ArrayList<String[]>> seiyuu_character = new ArrayList<>();
  Map<String,ArrayList<String>> seiyuu = new HashMap<>();
  ArrayList<ArrayList<String>> favoriteAnime = new ArrayList<>();
  public Model(List<String> contents){
    searchAnimeTID(contents);
  }
  void searchAnimeTID(List<String> contents){
    Pattern pattern = Pattern.compile("<a href=\"/tid/(\\d+)\">(.*)</a>");
    Matcher match;
    for(String line: contents){
      match = pattern.matcher(line);
      if (match.find()){
        setAnimeTIDDB(Integer.parseInt(match.group(1)),match.group(2));
      }
    }
  }
  private void setAnimeTIDDB(int tid, String title){
    animeTIDDB.put(title,tid);
  }
  public String[] getAnimeDetailByTID(int tid){
    String[] animeDetail = new String[3];
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    contents = url.connectHttp("https://cal.syoboi.jp/db.php?Command=TitleLookup&TID="+tid);
    String[] title_production = searchAnimeTitleProductionByContents(contents);
    searchAnimeSeiyuuCaracterByContents(contents);
    animeDetail[0] = title_production[0];
    animeDetail[1] = String.valueOf(tid);
    animeDetail[2] = title_production[1];
    // for(Map.Entry<String,ArrayList<String>> data: seiyuu.entrySet()) {
    //   System.out.println(data.getKey());
    //   for(String character: data.getValue()){
    //     System.out.println("    "+character);
    //   }
    // }
    return animeDetail;


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

  public Map<String,Integer> getAnimeTIDDB(){
    return animeTIDDB;
  }
  public int searchTID(String title){
    return animeTIDDB.get(title);
  }

  public ArrayList<String> searchDAnimeStore(String searchKey) {
    ArrayList<String> searchSuggestions =new ArrayList<String>();
    Url url = new Url();
    List<String> contents = new ArrayList<String>();
    contents = url.connectHttp("https://animestore.docomo.ne.jp/animestore/rest/WS000105?length=20&mainKeyVisualSize=2&searchKey=" + searchKey + "&vodTypeList=svod_tvod&_=1693395247779");
    
    Pattern p_title = Pattern.compile("\"workTitle\":\"(.*?)\"");
    Matcher m_title = p_title.matcher(contents.get(0));

    while(m_title.find()){
      // System.out.println(m_title.group(1));
      searchSuggestions.put(m_title.group(1));
    }
    return searchSuggestions;
  }

  public void addSeiyuu(List<String> seiyuu){

  }
  public void addCaracter(List<String> character){

  }
  public void addFavoriteAnime(){

  }
  public void addSeiyuu_FavoriteAnime(List<String> seiyuu,List<String> character){}
}
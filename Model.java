import java.util.*;
import java.util.regex.*;

class Model{
  Map<String,Integer> animeTIDDB = new HashMap<>();
  public Model(java.util.List<String> contents){
    searchAnimeTID(contents);
  }
  void searchAnimeTID(java.util.List<String> contents){
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
  public Map<String,Integer> getAnimeTIDDB(){
    return animeTIDDB;
  }
  public int searchTID(String title){
    return animeTIDDB.get(title);
  }
}
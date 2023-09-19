class Main{
  public static void main(String argv[]){
    Model m =new Model();
    View v = new View(m,"anime"); // 画面タイトルをanimeとする
    new Controller(m,v);
  }
}

// import com.google.api.client.googleapis.json.GoogleJsonResponseException;
// import com.google.api.client.http.HttpRequest;
// import com.google.api.client.http.HttpRequestInitializer;
// import com.google.api.client.http.HttpTransport;
// import com.google.api.client.http.javanet.NetHttpTransport;
// import com.google.api.client.json.JsonFactory;
// import com.google.api.client.json.gson.GsonFactory;
// import com.google.api.services.youtube.YouTube;
// import com.google.api.services.youtube.model.ResourceId;
// import com.google.api.services.youtube.model.SearchListResponse;
// import com.google.api.services.youtube.model.SearchResult;
// import com.google.api.services.youtube.model.Thumbnail;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.util.*;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// public class Api{
//   // String apiKey;
//   /** HTTPトランスポート */
//   private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

//   /** JSONファクトリ */
//   private static final JsonFactory JSON_FACTORY = new GsonFactory();

//   /** HTTPリクエストの初期化クラス */
//   private static final HttpRequestInitializer HTTP_REQUEST_INITIALIZER = new HttpRequestInitializer() {
//     public void initialize(HttpRequest request) throws IOException {}
//   };

//   /** 返される動画の最大数 (50 = ページあたりの上限) */
//   private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
//   Properties properties = new Properties();

//   public Api(){
//     // Path path = Paths.get("apikey.txt");    
// 		// try {
// 		// 	apiKey = Files.readString(path);
// 		// } catch(IOException ex) {
// 		// 	ex.printStackTrace();
// 		// }
//     // Properties properties = new Properties();
//     try {
//         InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
//         properties.load(in);
//     } catch (IOException e) {
//         System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
//                 + " : " + e.getMessage());
//         System.exit(1);
//     }
//   }
// public void searchAPI(String queryTerm){
//  try {
//             /**
//              * 1.Youtubeオブジェクトを生成します。
//              * 　オブジェクトの生成にはHTTPトランスポート、JSONファクトリが必要です。
//              * 　※HTTPリクエストの初期化クラスは必須ではありませんが、今回はサンプルを参考に指定しておきます。
//              *
//              * 2.続いてアプリケーション名を指定しておきます。
//              *   アプリケーション名を指定すると各リクエストのUserAgentヘッダーに使用されます。
//              *   指定していなければ、nullが設定されます。
//              */
//             youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HTTP_REQUEST_INITIALIZER)
//                     .setApplicationName("youtube-cmdline-search-sample").build();

//             List<String> part = new ArrayList<>(Arrays.asList("id", "snippet"));
//             YouTube.Search.List search = youtube.search().list(part);

//             // プロパティファイルに定義したAPIキーを設定する
//             String apiKey = properties.getProperty("youtube.apikey");
//             search.setKey(apiKey);

//             // ユーザーから検索キーワードを受け取り、設定します。
//             // String queryTerm = getInputQuery();

//             search.setQ(queryTerm);  //queryTermを引数とする

//             // 動画のみを検索対象にします
//             // もし、プレイリストやチャンネルも検索対象とする場合は「playlist」、「channel」なども追加します
//             search.setType(new ArrayList<>(Arrays.asList("video")));

//             // 返される情報を必要なフィールドのみに減らします
//             // ※呼び出しをより効率的にする効果があるようです
//             search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");

//             // 検索結果として取得してくる件数を指定します
//             search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

//             // 検索を実行し、結果を取得します。
//             SearchListResponse searchResponse = search.execute();
//             List<SearchResult> searchResultList = searchResponse.getItems();

//             // 検索結果があれば、内容を出力します。
//             if (searchResultList != null) {
//                 prettyPrint(searchResultList.iterator(), queryTerm);
//             }
//         } catch (GoogleJsonResponseException e) {
//             System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
//                     + e.getDetails().getMessage());
//         } catch (IOException e) {
//             System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
//         } catch (Throwable t) {
//             t.printStackTrace();
//         }
// }
// private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

//         if (!iteratorSearchResults.hasNext()) {
//             System.out.println(" There aren't any results for your query.");
//         }

//         while (iteratorSearchResults.hasNext()) {

//             SearchResult singleVideo = iteratorSearchResults.next();
//             ResourceId rId = singleVideo.getId();

//             // APIリソースのタイプがビデオであれば、内容を出力します。
//             if (rId.getKind().equals("youtube#video")) {
//                 Thumbnail thumbnail = (Thumbnail)singleVideo.getSnippet().getThumbnails().get("default");

//                 System.out.println(" Video Id:" + rId.getVideoId());
//                 System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
//                 System.out.println(" Thumbnail: " + thumbnail.getUrl());
//                 System.out.println("\n-------------------------------------------------------------\n");
//             }
//         }
// }
// }
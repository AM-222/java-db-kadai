package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		
		Connection con = null;
        Statement statementSel = null;         //SELECT用
        String getUserId = "1002";             //抽出条件用（sql用にStringで定義）
        PreparedStatement statementIns = null; //INSERT用
        
        //追加データ
        String[][] dataList = {
        		{"1003", "2023-02-08", "昨日の夜は徹夜でした・・","13"},
        		{"1002", "2023-02-08", "お疲れ様です！","12"},
        		{"1003", "2023-02-09", "今日も頑張ります！","18"},
        		{"1001", "2023-02-09", "無理は禁物ですよ！","17"},
        		{"1002", "2023-02-10", "明日から連休ですね！","20"}
        };
        
        try {
            // DBに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:8889/challenge_java",
                "root",
                "root"
            );

            System.out.println("データベース接続成功：" + con);
            
            //
            //SQL準備(データ追加)
            //
            System.out.println("レコード追加を実行します");
            
            String sqlInsert = """
            				   INSERT INTO posts (user_id, posted_at, post_content, likes)
            				   VALUES ('1003', '2023-02-08', '昨日の夜は徹夜でした・・','13')
            				         ,('1002', '2023-02-08', 'お疲れ様です！','12')
            				         ,('1003', '2023-02-09', '今日も頑張ります！','18')
            				         ,('1001', '2023-02-09', '無理は禁物ですよ！','17')
            				         ,('1002', '2023-02-10', '明日から連休ですね！','20')
            				         ;
            				   """;
            	
            statementIns = con.prepareStatement(sqlInsert);
            
            // SQLクエリを実行（DBMSに送信）
            int rowCnt = statementIns.executeUpdate();
            System.out.println(rowCnt + "件のレコードが追加されました");
            
            //
            // SQL準備(抽出条件：user_id=1002)
            //
            statementSel = con.createStatement();
            String sqlSelect = "SELECT * FROM posts WHERE user_id = " + getUserId + ";";
            
            //　データ検索
            ResultSet result = statementSel.executeQuery(sqlSelect);

            //検索結果を抽出表示
            System.out.println("ユーザーIDが" + getUserId + "のレコードを検索しました");
            while(result.next()) {
                Date postedAt = result.getDate("posted_at");
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目：投稿日時=" + postedAt
                                   + "／投稿内容=" + postContent + "／いいね数=" + likes );
            }
            
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( statementIns != null ) {
                try { statementIns.close(); } catch(SQLException ignore) {}
            }
            if( statementSel != null ) {
                try { statementSel.close(); } catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }
	}

}

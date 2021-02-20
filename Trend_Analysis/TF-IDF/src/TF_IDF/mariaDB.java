package TF_IDF;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class mariaDB {
	String driver = "org.mariadb.jdbc.Driver";
	Connection con;
	Connection con1;
	PreparedStatement pstmt;
	ResultSet rs;

	public mariaDB() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/parktest", "root", "1");
			con1 = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/lhb_test1", "root", "1");

			/*
			 * if (con != null) { System.out.println("DB 접속 성공"); }
			 */
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			System.out.println("DB 접속 실패");
			e.printStackTrace();
		}
	}
	
	//웹에 트렌드 결과 넘겨주기 위한 시간, 트윗수, 일치 키워드 수
	//lbh_test1의 trend_result table
public static void tresult(int time, int tweet, int beauty) {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;
		try {
			String sql="update trend_result set time=?,tweet=?,beauty=?";
			pstmt = dbcon.con1.prepareStatement(sql);
			pstmt.setInt(1,time);
			pstmt.setInt(2,tweet);
			pstmt.setInt(3,beauty);
			
			pstmt.executeUpdate();
			
		} catch (Exception se1) {
			se1.printStackTrace();
		}
}
	public static void mariaDB_insert(String ID, String text, String keyword) {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;

		try {
			// stat = dbcon.con.createStatement();
			String sql = "INSERT INTO twitter_set VALUES (?,?,?)";
			pstmt = dbcon.con.prepareStatement(sql);

			pstmt.setString(1, ID);
			pstmt.setString(2, text);
			pstmt.setString(3, keyword);

			pstmt.executeUpdate();

			// pstmt.close();
			// dbcon.con.close();
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public static void mariaDB_select(String keyword) {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;
		ResultSet rs;
		String[] array = keyword.split(",");

		try {
			String sql = "select * from brandName,companyName";
			String sql1 = "insert into same (keyword,samekey) select ?,? from dual  where not exists (select * from same where keyword=?)";
			// String sql2="select distinct * from same";
			pstmt = dbcon.con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				String brandname = rs.getString("brandname");
				String companyname = rs.getString("companyname");
				// String productname=rs.getString("productname");

				for (int i = 0; i < array.length; i++) {

					if (array[i].contains(brandname)) {
						// mariaDB_insert();
						pstmt = dbcon.con.prepareStatement(sql1);
						pstmt.setString(1, keyword);
						pstmt.setString(2, brandname);
						pstmt.setString(3, keyword);
						pstmt.executeUpdate();
					} else if (array[i].contains(companyname)) {
						pstmt = dbcon.con.prepareStatement(sql1);
						pstmt.setString(1, keyword);
						pstmt.setString(2, companyname);
						pstmt.setString(3, keyword);
						pstmt.executeUpdate();
					}
				}
			}
			// pstmt=dbcon.con.prepareStatement(sql2);

		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	//map과 brandname 일치 키워드 추출->return map
	public static HashMap mariaDB_brandname(HashMap map) {
		  mariaDB dbcon = new mariaDB(); 
		  PreparedStatement pstmt; 
		  ResultSet rs; 
		  HashMap<String, Double> samemap = new HashMap<String, Double>(); 
		  samemap=map;

	  Iterator<String> iterator=samemap.keySet().iterator(); 
	  try { 
		  String sql = "select * from brandName"; 
		   
		  pstmt = dbcon.con.prepareStatement(sql);
		  
		  rs = pstmt.executeQuery(); 
		  rs.last();
		  int lastrow=rs.getRow();
		  rs.beforeFirst(); 
		  int row=0;
	  
		  while (iterator.hasNext()) { 
			  
			  String key =(String) iterator.next();
		  
				  while (rs.next()) {
					   
					  row++;
					  String brandname =rs.getString("brandname"); 			  
					  if (key.contentEquals(brandname) ) { 
						  row=0; 
						  break; 
						  }
				  
					  if(row==lastrow) { 
						  row=0;
						  iterator.remove();
						  break; 
					  }
			  
			  
			  } 
			  rs.beforeFirst(); 
			  }
	  
		  //System.out.println(samemap); 
	  } catch (SQLException se1) 
	  {
	  se1.printStackTrace(); 
	  } 
	  return samemap;
	  
	  }
	
	//map과 productname 일치 키워드 추출->return map
public static HashMap mariaDB_productname(HashMap map) {
		  mariaDB dbcon = new mariaDB(); 
		  PreparedStatement pstmt; 
		  ResultSet rs; 
		  HashMap<String, Double> samemap = new HashMap<String, Double>(); 
		  samemap=map;

	  Iterator<String> iterator=samemap.keySet().iterator(); 
	  try { 
		  String sql = "select * from productName";
		  pstmt = dbcon.con.prepareStatement(sql);
		  
		  rs = pstmt.executeQuery(); 
		  rs.last();
		  int lastrow=rs.getRow();
		  rs.beforeFirst(); 
		  int row=0;
		  
		  while (iterator.hasNext()) {  
			  String key =(String) iterator.next();
		  
				  while (rs.next()) {
					  row++;
					  String productname=rs.getString("productname"); 
					  String[] pn=productname.split(" ");
					  int i; 
					  for(i=0; i<pn.length; i++) { 
						  if(key.contentEquals(pn[i])) { 
							  row=0; 
							  i++; 
							  break; 
							  }	  
					  	}  
					  if(row==lastrow && !key.contentEquals(pn[i-1])) { 
						  row=0;
						  iterator.remove();
						  break; 
					  } 
				  } 
			  rs.beforeFirst(); 
			  }
	  
	  //System.out.println(samemap); 
	  } catch (SQLException se1) 
	  {
	  se1.printStackTrace(); 
	  } 
	  return samemap;
	  
	  }
	
//시간대별 tf_idf 저장
	public static void word_serch(Set<String> word, HashMap<String, Double> tf_idf_map) {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;
		ResultSet rs = null;

		try {
			String sql = "select * from tf_idf where keyword = ?";
			
			String sql1 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, rate = ? where keyword = ?";
			String sql2 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, rate = ? where keyword = ?";
			String sql3 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, tfidf4 = ?, rate = ? where keyword = ?";
			String sql4 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, tfidf4 = ?, tfidf5 = ?, rate = ? where keyword = ?";
			String sql_first = "insert into tf_idf(keyword, tfidf1, rate) values(?,?,?)";

			for (String set : word) {
				pstmt = dbcon.con.prepareStatement(sql);
				pstmt.setString(1, (String) set);
				rs = pstmt.executeQuery();
				// rs.isBeforeFirst();
				double rate = 0; // 최종 tfidf 비율 값

				if (rs.isBeforeFirst() == true) {
					// rs = pstmt.executeQuery();
					while (rs.next()) {
						// System.out.println(set);
						// System.out.println(rs.getString("tfidf1"));
						double tfidf1 = (double) rs.getDouble("tfidf1");
						if (rs.getString("tfidf2") != null) {
							// if (rs.getString("tfidf2").isEmpty()==false) {
							// System.out.println(rs.getString("tfidf2"));
							double tfidf2 = (double) rs.getDouble("tfidf2");

							if (tfidf1 == 0 && tfidf2 == 0)
								rate = tf_idf_map.get(set);
							else if (tfidf2 == 0)
								rate = tf_idf.tf_idf_rate(2, tfidf1, 0, tf_idf_map.get(set)); // 비율 계산
							else if (tfidf1 == 0)
								rate = tf_idf.tf_idf_rate(3, 0, tfidf2, tf_idf_map.get(set));
							else
								rate = tf_idf.tf_idf_rate(1, tfidf1, tfidf2, tf_idf_map.get(set)); // 비율 계산

							if (rs.getString("tfidf3") != null) {
								double tfidf3 = (double) rs.getDouble("tfidf3");

								if (rs.getString("tfidf4") != null) {
									double tfidf4 = (double) rs.getDouble("tfidf4");
									// sql4 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, tfidf4 = ?,
									// tfidf5 = ?, rate = ? where keyword = ?";
									pstmt = dbcon.con.prepareStatement(sql4);
									pstmt.setDouble(1, tf_idf_map.get(set));
									pstmt.setDouble(2, tfidf1);
									pstmt.setDouble(3, tfidf2);
									pstmt.setDouble(4, tfidf3);
									pstmt.setDouble(5, tfidf4);
									pstmt.setDouble(6, rate);
									pstmt.setString(7, set);
									pstmt.executeUpdate();
								} else {
									// sql3 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, tfidf4 = ?,
									// rate = ? where keyword = ?";
									pstmt = dbcon.con.prepareStatement(sql3);
									pstmt.setDouble(1, tf_idf_map.get(set));
									pstmt.setDouble(2, tfidf1);
									pstmt.setDouble(3, tfidf2);
									pstmt.setDouble(4, tfidf3);
									pstmt.setDouble(5, rate);
									pstmt.setString(6, set);
									pstmt.executeUpdate();
								}
							} else {
								// sql2 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, rate = ? where
								// keyword = ?";
								pstmt = dbcon.con.prepareStatement(sql2);
								pstmt.setDouble(1, tf_idf_map.get(set));
								pstmt.setDouble(2, tfidf1);
								pstmt.setDouble(3, tfidf2);
								pstmt.setDouble(4, rate);
								pstmt.setString(5, set);
								pstmt.executeUpdate();
							}
						} else {
							// System.out.println("tfidf2 == null");
							// sql1 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, rate = ? where keyword =
							// ?";
							rate = tf_idf.tf_idf_rate(2, tfidf1, 0, tf_idf_map.get(set));
							// 최신 tfidf값으로 업데이트
							pstmt = dbcon.con.prepareStatement(sql1);
							pstmt.setDouble(1, tf_idf_map.get(set));
							pstmt.setDouble(2, tfidf1);
							pstmt.setDouble(3, rate);
							pstmt.setString(4, set);
							pstmt.executeUpdate();
						}
					}
				} else {
					// System.out.println(set + " == null");
					pstmt = dbcon.con.prepareStatement(sql_first);
					pstmt.setString(1, set);
					pstmt.setDouble(2, tf_idf_map.get(set));
					pstmt.setDouble(3, tf_idf_map.get(set));
					rs = pstmt.executeQuery();
				} // System.out.println(set);
			}
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	public static void word_rate_zero(HashMap<String, Double> tf_idf_map) {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;
		ResultSet rs = null;
		try {
			String sql = "select * from tf_idf ";
			String sql1 = "update tf_idf set tfidf1 = ?, tfidf2 = ?, tfidf3 = ?, tfidf4 = ?, tfidf5 =?, rate = ? where keyword = ? ";
			pstmt = dbcon.con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				boolean key = tf_idf_map.containsKey(rs.getString("keyword"));
				if (key != true) {
					double tfidf1 = (double) rs.getDouble("tfidf1");
					double tfidf2 = (double) rs.getDouble("tfidf2");
					double tfidf3 = (double) rs.getDouble("tfidf3");
					double tfidf4 = (double) rs.getDouble("tfidf4");
					pstmt = dbcon.con.prepareStatement(sql1);
					pstmt.setDouble(1, 0.0);
					pstmt.setDouble(2, tfidf1);
					pstmt.setDouble(3, tfidf2);
					pstmt.setDouble(4, tfidf3);
					pstmt.setDouble(5, tfidf4);
					pstmt.setDouble(6, 0.0);
					pstmt.setString(7, rs.getString("keyword"));
					rs = pstmt.executeQuery();
				}
			}
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}

	//rate 높은 순, tfidf1 높은 순 정렬-> 핫토픽 top 10 
	public static void order_by() {
		mariaDB dbcon = new mariaDB();
		PreparedStatement pstmt;
		//Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			String sql = "select * from tf_idf order by rate desc, tfidf1 desc;";
			String sql_create_table = "create table topk(keyword varchar(30) character set utf8, tfidf1 double, tfidf2 double, tfidf3 double, tfidf4 double, tfidf5 double, rate double);";
			String sql_insert="insert into topk values(?,?,?,?,?,?,?)";
			String sql_drop_table ="drop table topk;";
			pstmt = dbcon.con1.prepareStatement(sql_drop_table);
			rs1=pstmt.executeQuery();
			
			pstmt = dbcon.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			pstmt = dbcon.con1.prepareStatement(sql_create_table);
			rs1=pstmt.executeQuery();
			
			int i=0;
			while(rs.next()) {
				i++;
				String keyword=rs.getString("keyword");
				double tfidf1=(double)rs.getDouble("tfidf1");
				double tfidf2=(double)rs.getDouble("tfidf2");
				double tfidf3=(double)rs.getDouble("tfidf3");
				double tfidf4=(double)rs.getDouble("tfidf4");
				double tfidf5=(double)rs.getDouble("tfidf5");
				double rate=(double)rs.getDouble("rate");
				
				pstmt = dbcon.con1.prepareStatement(sql_insert);
				pstmt.setString(1, keyword);
				pstmt.setDouble(2, tfidf1);
				pstmt.setDouble(3, tfidf2);
				pstmt.setDouble(4, tfidf3);
				pstmt.setDouble(5, tfidf4);
				pstmt.setDouble(6, tfidf5);
				pstmt.setDouble(7, rate);
				
				rs1=pstmt.executeQuery();
				
				if(i>=10) {
					break;
				}
			}
		} catch (SQLException se1) {
			se1.printStackTrace();
		}
	}
}

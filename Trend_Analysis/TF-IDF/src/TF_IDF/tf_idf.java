package TF_IDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.util.HashMap;
import java.util.*;
import java.math.*;

public class tf_idf {
	public static void main(String[] args) {
		
		int TIME = 3600000; // 1000 = 1 초   시간 조절
		//int TIME = 60000;
		try {

			// 파일 객체 생성
			final File folder = new File("/home/lhb/ex_data"); // 트위터 파일 있는 주소 

			String temp_name = null;
			File temp_file = null;
			
			
			int i = 2;
			while (true) {
				System.out.println(i);
				if (i == 1) {					
					for (final File fileEntry : folder.listFiles()) {
						tf_idf_start(fileEntry);					
					}
					mariaDB.order_by();
					Thread.sleep(TIME); // 1000 = 1 초   시간 조절
				} 
				else {
				
					temp_file=file_re();
					tf_idf_start(temp_file);
					mariaDB.order_by();
					Thread.sleep(TIME); // 1000 = 1 초   시간 조절
				}
				i++;
				if (i == 100)
					i = 2;
			}
			
		} catch (InterruptedException e) {
		}
	}

	public static void tf_idf_start(File fileEntry) {
		try {

				System.out.println(fileEntry.getName());
				File file = new File("/home/lhb/ex_data/" + fileEntry.getName()); // 트위터 파일 있는 주소 

				// 입력 스트림 생성
				FileReader filereader = new FileReader(file);
				// 입력 버퍼 생성
				BufferedReader bufReader = new BufferedReader(filereader);
				HashMap<String, Double> map = new HashMap<String, Double>();
				String line = "";
				int totalword = 0;
				int linecnt=0;

				HashMap<String, Double> tf_map = new HashMap<String, Double>();
				HashMap<String, Double> idf_map = new HashMap<String, Double>();
				HashMap<String, Double> tf_idf_map = new HashMap<String, Double>();

				while ((line = bufReader.readLine()) != null) {
					linecnt++;
					
					String[] array = line.split(",");

					for (String word : array) {
						totalword++;// =N
						if (!map.containsKey(word))
							map.put(word, (double) 0);

						map.put(word, map.get(word) + 1);
					}
					// n=map.get(word)
					// tf=n/N
				}
				
				map = mariaDB.mariaDB_brandname(map); //파일 내 단어 중 brandname과 일치하는 키워드만 추출
				map = mariaDB.mariaDB_productname(map);//파일 내 단어 중 productname과 일치하는 키워드만 추출

				int beauty=map.size();//화장품 관련 단어 개수

				mariaDB.tresult(1, linecnt, beauty);
				
				 tf_map = tf(map, totalword); 
				 idf_map = idf(map, totalword); 

				 tf_idf_map = tf_idf(tf_map, idf_map); //tf_idf=tf*idf
				
				 mariaDB.word_serch(map.keySet(), tf_idf_map); 
				 mariaDB.word_rate_zero(tf_idf_map); 
				 mariaDB.order_by();

				

				bufReader.close();


		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static HashMap tf(HashMap map, int totalword) {
		HashMap<String, Double> tf_map = new HashMap<String, Double>();
		Set<String> set = map.keySet();
		double tf_score;

		for (String word : set) {
			tf_score = (double) map.get(word) / (double) totalword;
			tf_map.put(word, tf_score);
		}
		return tf_map;
	}

	public static HashMap idf(HashMap map, int totalword) {
		HashMap<String, Double> idf_map = new HashMap<String, Double>();
		Set<String> set = map.keySet();
		double idf_score;

		for (String word : set) {
			idf_score = Math.log10(totalword / ((double) map.get(word)) + 1);
			idf_map.put(word, idf_score);
		}
		return idf_map;
	}

	public static HashMap tf_idf(HashMap tf, HashMap idf) {
		HashMap<String, Double> tf_idf_map = new HashMap<String, Double>();
		Set<String> set = tf.keySet();
		double tf_idf_score;

		for (String word : set) {
			tf_idf_score = (double) tf.get(word) * (double) idf.get(word);
			tf_idf_map.put(word, tf_idf_score);
		}
		return tf_idf_map;
	}

	public static double tf_idf_rate(int num, double tfidf1, double tfidf2, double tf_idf) {//tf_idf1과 tf_idf2 이용하여 rate 구하는 부분
		double rate = 0;  //최종 tfidf 비율 값

		if (num == 1) {
			rate = tf_idf / (0.7 * tfidf1 + 0.3 * tfidf2);
		} else if (num == 2) {
			rate = tf_idf / tfidf1;
		} else if (num == 3)
			rate = tf_idf / (0.7 * tfidf2);

		return rate;
	}
	public static File file_re(){
		final File folder = new File("/home/lhb/ex_data"); // 트위터 파일 있는 주소

		String temp_name = null;
		File temp_file = null;

		for (final File filename : folder.listFiles()) {
			//System.out.println("3435" + filename+"\n");
			
			if (temp_name == null) {
				temp_name = filename.getName();
			} 
			else {
				String[] s1 = temp_name.split("-");
				String[] s2 = filename.getName().split("-");
				String[] s1_2 = s1[1].split(".txt");
				String[] s2_2 = s2[1].split(".txt");
				
				if(Integer.parseInt(s1[0]) == Integer.parseInt(s2[0])) {
					//if (0 > s1_2[0].compareTo(s2_2[0])) {
					if(Integer.parseInt(s1_2[0]) < Integer.parseInt(s2_2[0])) {
						temp_name = filename.getName();
						temp_file = filename;
						//System.out.println("1" + temp_file+"\n");
					}
				}				
				//else if (0 > s1[0].compareTo(s2[0])) {
				else if(Integer.parseInt(s1[0]) < Integer.parseInt(s2[0])){
					temp_name = filename.getName();
					temp_file = filename;
					//System.out.println("2" + temp_file+"\n");
				}
				//System.out.println("34" + s1[0].compareTo(s2[0]) +"\n");
			}
		}
		return temp_file;
	}

}

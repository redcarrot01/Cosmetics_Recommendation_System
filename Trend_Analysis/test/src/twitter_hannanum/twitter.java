package twitter_hannanum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

public class twitter {

	public static void main(String[] args) {

		twitter4j.TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		// Set access
		twitterStream.setOAuthConsumer("CrxDclnmPZZn6JHBfg8N34wLC",
				"gkcdChK26fv1CHevBdHDuppH9tz0Cua2dNy02SOhHzGwUw4vb9");
		twitterStream.setOAuthAccessToken(new AccessToken("1117732208842698752-u7vA0sWCW3BxuI2H3pvEdMBMb9vnLi",
				"Q7Cxvl7xlubYDdrrycIfyybTxTr1mOQzM3SyzzQGXIpXt"));

		try {
			printStream(twitterStream);
		} catch (TwitterException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void printStream(twitter4j.TwitterStream twitterStream) throws TwitterException, IOException {// 트위터 실시간 데이터 수집하는 부분
		StatusListener listener = new StatusListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onStatus(Status status) {
				String keyword = "";

				long filetime = status.getCreatedAt().getHours();

				if (status.getLang().equals("ko")) {

					String filename = "/home/lhb/yu/" + status.getCreatedAt().getDate() + "-" + filetime + ".txt";

					keyword = hannanum.noun_extractor(status.getText());

					try {
						HashMap<String, HashMap<String, Double>> file_tf_idf = new HashMap<String, HashMap<String, Double>>();
						
						FileWriter writer = new FileWriter(filename, true);

						keyword += "\n";
						writer.write(keyword);

						writer.flush();

					 
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					

				}

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}
		};

		twitterStream.addListener(listener);
		twitterStream.sample();
	}
}

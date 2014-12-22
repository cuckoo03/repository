import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

public class OAuthBasicExample {
	static final String REQUEST_TOKEN_URL = "https://apis.daum.net/oauth/requestToken";
	static final String AUTHORIZE_URL = "https://apis.daum.net/oauth/authorize";
	static final String ACCESS_TOKEN_URL = "https://apis.daum.net/oauth/accessToken";

	static final String CONSUMER_KEY = "777517ae-36d0-48d2-9e37-914e9c3e6c3b";
	static final String CONSUMER_SECRET = "hs25MwQaQciFwZVLtQUlC87TcBsjbqiii4RdA5x3yxXXSbDSBwd3Bg00";

	static final String API_URL = "https://apis.daum.net";

	// Service Provider 객체 생성
	static OAuthProvider provider = new DefaultOAuthProvider(REQUEST_TOKEN_URL,
			ACCESS_TOKEN_URL, AUTHORIZE_URL);

	// Consumer 객체 생성
	static OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY,
			CONSUMER_SECRET);

	public static void main(String[] args) throws Exception {
		String authUrl = provider.retrieveRequestToken(consumer,
				OAuth.OUT_OF_BAND);
		System.out.println("auth url:" + authUrl);

		System.out.println("인증코드 입력");
		Scanner s = new Scanner(System.in);
		String verifier = s.next();

		provider.retrieveAccessToken(consumer, verifier);

		final String access_token = consumer.getToken();
		System.out.println("access token:" + access_token);
		final String token_secret = consumer.getTokenSecret();
		System.out.println("token secret:" + token_secret);

		URL url = new URL(API_URL + "/cafe/favorite_cafes.json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();

		consumer.sign(request);
		
		request.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String tempStr = "";
		while ((tempStr = br.readLine()) != null) {
			System.out.println(tempStr);
		}
	}
}
package com.floopandbloop.game;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;
import com.mygdx.game.R;

public class AndroidLauncher extends AndroidApplication implements AdHandler, com.floopandbloop.game.PlayServices {

	private static final String TAG = "AndroidLauncher";
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	protected AdView adView;

	private GameHelper gameHelper;
	private final static int requestCode = 1;
	private long score = 0;

	Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case SHOW_ADS:
					adView.setVisibility(View.VISIBLE);
					break;
				case HIDE_ADS:
					adView.setVisibility(View.GONE);
					break;
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout layout = new RelativeLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MainGame(this, this), config);
		layout.addView(gameView);

		// ad
		adView = new AdView(this);

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});

		adView.setBackgroundColor(Color.TRANSPARENT);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-4407412952598459/9712371860");

		AdRequest.Builder builder = new AdRequest.Builder();
		builder.addTestDevice("CCBF49C9D2C4695E4E3EB713A77CFDA2");
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);

		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		layout.addView(adView, adParams);
		adView.loadAd(builder.build());

		setContentView(layout);


		// play services
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);


//	ca-app-pub-4407412952598459~6901896766
//		App ID: ca-app-pub-4407412952598459~6901896766
//		Ad unit ID: ca-app-pub-4407412952598459/9712371860
//		237125913808
//		237125913808-urb1u9c7jpaftqrfod06clurma4oresq.apps.googleusercontent.com
//		237125913808-3ga5p0hh6evm3jsggnat10o5trk3vaf9.apps.googleusercontent.com
//
	}

	@Override
	public void showAds(boolean show){
		if (show == true) {
			hander.sendEmptyMessage(SHOW_ADS);
		} else {
			hander.sendEmptyMessage(HIDE_ADS);
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
				}
			});
			loadScoreOfLeaderBoard();
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame()
	{
		String str = "Your PlayStore Link";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(int highScore) {
		if (isSignedIn() == true)
		{
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_floop_and_bloop_leaderboard), highScore);
		}
	}

	@Override
	public void showScore() {
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					getString(R.string.leaderboard_floop_and_bloop_leaderboard)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public void unlockAchievement()
	{
//		Games.Achievements.unlock(gameHelper.getApiClient(),
//				getString(R.string.achievement_dum_dum));
	}

	@Override
	public void showAchievement()
	{
//		if (isSignedIn() == true)
//		{
//			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient(),
//					getString(R.string.achievement_dum_dum)), requestCode);
//		}
//		else
//		{
//			signIn()
//		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	private void loadScoreOfLeaderBoard() {
		Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(),
				getString(R.string.leaderboard_floop_and_bloop_leaderboard),
				LeaderboardVariant.TIME_SPAN_ALL_TIME,
				LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
						new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
			@Override
			public void onResult(final Leaderboards.LoadPlayerScoreResult scoreResult) {
				if (isSignedIn() == true)
				{
					if (isScoreResultValid(scoreResult)) {
						// here you can get the score like this
						score = scoreResult.getScore().getRawScore();
					}
				}
			}
		});
	}

	@Override
	public long getHighScore() {
		return score;
	}

	private boolean isScoreResultValid(final Leaderboards.LoadPlayerScoreResult scoreResult) {
		return scoreResult != null && GamesStatusCodes.STATUS_OK == scoreResult.getStatus().getStatusCode() && scoreResult.getScore() != null;
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

}

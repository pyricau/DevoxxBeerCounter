package org.androidannotations.devoxx.beer;

import static android.text.Html.fromHtml;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class BeerCounterActivity extends SherlockActivity {

	TextView beerCountView;

	int beerCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.beers);

		beerCountView = (TextView) findViewById(R.id.beerCountView);

		View addBeerButton = findViewById(R.id.addBeerButton);
		addBeerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addBeerButtonClicked();
			}
		});

		loadBeerCount();
	}

	void addBeerButtonClicked() {
		beerCount++;
		saveBeerCount(beerCount);
		updateBeerViews();
	}

	void saveBeerCount(final int beerCount) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				getPreferences(MODE_PRIVATE) //
						.edit() //
						.putInt("beerCount", beerCount) //
						.commit();
				return null;
			}

		}.execute();
	}

	private void updateBeerViews() {
		if (beerCount == 0) {
			setTitle(R.string.still_sober);
			beerCountView.setText("");
		} else {
			String beerCountString = getResources().getQuantityString( //
					R.plurals.beers_title, //
					beerCount, //
					beerCount //
					);
			setTitle(fromHtml(beerCountString));
			SpannableStringBuilder beers = new SpannableStringBuilder();
			for (int i = 0; i < beerCount; i++) {
				beers.append('B');
				ImageSpan beerSpan = new ImageSpan(this, R.drawable.ic_beer);
				beers.setSpan(beerSpan, i, i + 1, 0);
			}
			beerCountView.setText(beers);
		}
	}

	void loadBeerCount() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				SharedPreferences preferences = getPreferences(MODE_PRIVATE);
				int beerCount = preferences.getInt("beerCount", 0);
				return beerCount;
			}

			@Override
			protected void onPostExecute(Integer beerCount) {
				beerCountLoaded(beerCount);
			}
		}.execute();
	}

	void beerCountLoaded(int beerCount) {
		this.beerCount = beerCount;
		updateBeerViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.beer_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.emergency) {
			emergencySelected();
			return true;
		}
		return false;
	}

	void emergencySelected() {
		beerCount = 0;
		saveBeerCount(beerCount);
		updateBeerViews();
	}
}

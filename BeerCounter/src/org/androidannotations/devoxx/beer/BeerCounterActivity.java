package org.androidannotations.devoxx.beer;

import static android.text.Html.fromHtml;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.beers)
@OptionsMenu(R.menu.beer_menu)
public class BeerCounterActivity extends SherlockActivity {

	@ViewById
	TextView beerCountView;

	int beerCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadBeerCount();
	}

	@Click
	void addBeerButtonClicked() {
		beerCount++;
		saveBeerCount(beerCount);
		updateBeerViews();
	}

	@Background
	void saveBeerCount(final int beerCount) {
		getPreferences(MODE_PRIVATE) //
				.edit() //
				.putInt("beerCount", beerCount) //
				.commit();
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

	@Background
	void loadBeerCount() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		int beerCount = preferences.getInt("beerCount", 0);
		beerCountLoaded(beerCount);
	}

	@UiThread
	void beerCountLoaded(int beerCount) {
		this.beerCount = beerCount;
		updateBeerViews();
	}

	@OptionsItem
	void emergencySelected() {
		beerCount = 0;
		saveBeerCount(beerCount);
		updateBeerViews();
	}
}

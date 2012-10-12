package org.solovyev.android.samples.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.IntegerRange;
import org.solovyev.android.view.Picker;

/**
 * User: Solovyev_S
 * Date: 12.10.12
 * Time: 13:18
 */
public class SamplesViewActivity extends Activity implements Picker.OnChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_layout);

		final Picker verticalPicker = (Picker) findViewById(R.id.vertical_picker);
		verticalPicker.setRange(new IntegerRange(1, 10, 1, 1, null));
		verticalPicker.setOnChangeListener(this);

		final Picker horizontalPicker = (Picker) findViewById(R.id.horizontal_picker);
		horizontalPicker.setRange(new IntegerRange(1, 10, 1, 1, null));
		horizontalPicker.setOnChangeListener(this);
	}

	@Override
	public void onChanged(@NotNull Picker picker, @NotNull Object value) {
		switch (picker.getId()) {
			case R.id.vertical_picker:
				Toast.makeText(this, "Vertical picker new value: " + value, Toast.LENGTH_SHORT).show();
				break;

			case R.id.horizontal_picker:
				Toast.makeText(this, "Horizontal picker new value: " + value, Toast.LENGTH_SHORT).show();
				break;
		}
	}
}

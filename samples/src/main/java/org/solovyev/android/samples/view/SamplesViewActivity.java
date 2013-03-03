/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.samples.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import javax.annotation.Nonnull;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.IntegerRange;
import org.solovyev.android.view.Picker;
import org.solovyev.android.view.drag.DirectionDragButton;
import org.solovyev.android.view.drag.DragButton;
import org.solovyev.android.view.drag.DragDirection;
import org.solovyev.android.view.drag.SimpleOnDragListener;
import org.solovyev.common.math.Point2d;

/**
 * User: Solovyev_S
 * Date: 12.10.12
 * Time: 13:18
 */
public class SamplesViewActivity extends Activity implements Picker.OnChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acl_view_layout);

		final Picker verticalPicker = (Picker) findViewById(R.id.vertical_picker);
		verticalPicker.setRange(new IntegerRange(1, 10, 1, 1, null));
		verticalPicker.setOnChangeListener(this);

		final Picker horizontalPicker = (Picker) findViewById(R.id.horizontal_picker);
		horizontalPicker.setRange(new IntegerRange(1, 10, 1, 1, null));
		horizontalPicker.setOnChangeListener(this);

		final DirectionDragButton dragButton = (DirectionDragButton) findViewById(R.id.drag_button);
		dragButton.setOnDragListener(new SimpleOnDragListener(new SimpleOnDragListener.DragProcessor() {
			@Override
			public boolean processDragEvent(@Nonnull DragDirection dragDirection, @Nonnull DragButton dragButton, @Nonnull Point2d startPoint2d, @Nonnull MotionEvent motionEvent) {
				Toast.makeText(SamplesViewActivity.this, "Button dragged: " + ((DirectionDragButton) dragButton).getText(dragDirection), Toast.LENGTH_SHORT).show();
				return true;
			}
		}, SimpleOnDragListener.getDefaultPreferences(this)));
        dragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SamplesViewActivity.this, "Button clicked: " + dragButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
	}

	@Override
	public void onChanged(@Nonnull Picker picker, @Nonnull Object value) {
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

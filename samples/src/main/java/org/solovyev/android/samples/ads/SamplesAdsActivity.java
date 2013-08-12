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

package org.solovyev.android.samples.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.Toast;
import org.solovyev.android.Fragments;
import org.solovyev.android.ads.AdsController;
import org.solovyev.android.list.ListItemAdapter;
import org.solovyev.android.menu.*;
import org.solovyev.android.samples.R;
import org.solovyev.android.samples.menu.MenuListItem;
import org.solovyev.android.samples.menu.SamplesStaticMenu;
import org.solovyev.common.JPredicate;
import org.solovyev.common.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:11 PM
 */
public class SamplesAdsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acl_ads_layout);
        final ViewGroup views = (ViewGroup) findViewById(R.id.acl_main_linearlayout);
        AdsController.getInstance().inflateAd(this, views, R.id.acl_main_linearlayout);
	}
}

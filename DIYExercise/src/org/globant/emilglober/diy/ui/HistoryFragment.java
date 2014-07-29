package org.globant.emilglober.diy.ui;

import org.globant.emilglober.diy.adapters.CustomListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emilglober.diy.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

/* Sources:
 * http://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
 * http://www.mikeplate.com/2010/01/21/show-a-context-menu-for-long-clicks-in-an-android-listview/
 */
public class HistoryFragment extends Fragment
{
	SwipeListView swipelistview;
	CustomListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_history, container,
				false);
		swipelistview = (SwipeListView) view.findViewById(R.id.swipe_lv_list);
		adapter = new CustomListAdapter(this.getActivity(), R.layout.custom_row);

		// swipelistview.setListAdapter(adapter);

		swipelistview = (SwipeListView) view.findViewById(
				R.id.swipe_lv_list);

		swipelistview.setAdapter(adapter);

		// Part of the context menu thing
		// registerForContextMenu(getListView());

		swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener()
		{
			@Override
			public void onOpened(int position, boolean toRight)
			{
			}

			@Override
			public void onClosed(int position, boolean fromRight)
			{
			}

			@Override
			public void onListChanged()
			{
			}

			@Override
			public void onMove(int position, float x)
			{
			}

			@Override
			public void onStartOpen(int position, int action, boolean right)
			{
				Log.d("swipe", String.format("onStartOpen %d - action %d",
						position, action));
			}

			@Override
			public void onStartClose(int position, boolean right)
			{
				Log.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position)
			{
				Log.d("swipe", String.format("onClickFrontView %d", position));

				swipelistview.openAnimate(position); // when you touch front
														// view it will open

			}

			@Override
			public void onClickBackView(int position)
			{
				Log.d("swipe", String.format("onClickBackView %d", position));

				swipelistview.closeAnimate(position);// when you touch back view
														// it will close
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions)
			{

			}

		});

		// These are the swipe listview settings. you can change these
		// setting as your requirement
		swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are
																	// five
																	// swiping
																	// modes
		swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS); // there
																				// are
																				// four
																				// swipe
																				// actions
		swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		swipelistview.setOffsetLeft(convertDpToPixel(260f)); // left side offset
		swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
		swipelistview.setAnimationTime(50); // animation time
		swipelistview.setSwipeOpenOnLongPress(true); // enable or disable
														// SwipeOpenOnLongPress
		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// Part of the context menu thing
		// if (v.getId() == R.id.lstWeightHistory)
		// {
		// AdapterView.AdapterContextMenuInfo info =
		// (AdapterView.AdapterContextMenuInfo)menuInfo;
		//
		// menu.setHeaderTitle(Countries[info.position]);
		//
		// String[] menuItems = getResources().getStringArray(R.array.menu);
		//
		// for (int i = 0; i<menuItems.length; i++) {
		// menu.add(Menu.NONE, i, i, menuItems[i]);
		// }
	}

	public HistoryFragment()
	{
	}

	public int convertDpToPixel(float dp)
	{
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

}

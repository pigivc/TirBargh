package com.irprogram.tirbargh;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ApplicationAdapter extends ArrayAdapter<HashMap<String , Object>> {
	private List<HashMap<String , Object>> items;
	private ArrayList<HashMap<String , Object>> orginalItems;
	private Filter filter;
	public ArrayList<HashMap<String , Object>> filtered;
	//ImageLoader imgLoader;

	public ApplicationAdapter(Context context, List<HashMap<String , Object>> items) {
		super(context, R.layout.ads_list_row, items);
		this.items = items;
		this.orginalItems = new ArrayList<HashMap<String , Object>>(items);
		//imgLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater li = LayoutInflater.from(getContext());
			v = li.inflate(R.layout.ads_list_row, null);
		}

		HashMap<String , Object> app = items.get(position);

		if (app != null) {
			ImageView icon = (ImageView) v.findViewById(R.id.ads_img);
			byte[] img = (byte[])app.get("image");
if(img != null) {
	Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
	icon.setImageBitmap(bmp);
}

			TextView titleText = (TextView) v.findViewById(R.id.ads_title);
			/*LinearLayout ratingCntr = (LinearLayout) v
					.findViewById(R.id.lytRating);*/
			TextView dlText = (TextView) v.findViewById(R.id.ads_date);
			TextView txtDesc = (TextView)v.findViewById(R.id.ads_intro);

			TextView txtCat = (TextView)v.findViewById(R.id.ads_cat);

			/*if (icon != null) {
				*//*Resources res = getContext().getResources();
				String sIcon = "com.example.irod:drawable/" + app.getIcon();
				icon.setImageDrawable(res.getDrawable(res.getIdentifier(sIcon,
						null, null)));*//*
				Utility utility = new Utility();
				//utility.loadImageAsync(icon, app.getIcon());
				 imgLoader.DisplayImage(app.getIcon(), icon);
			}*/

			if (titleText != null)
				titleText.setText(app.get("title").toString());

			if (dlText != null) {
				NumberFormat nf = NumberFormat.getNumberInstance();
				dlText.setText(app.get("phone").toString());
			}
			
			if(txtDesc != null)
			{

					txtDesc.setText(String.valueOf(app.get("desc")));
			}

			/*if (ratingCntr != null && ratingCntr.getChildCount() == 0) {
				*//*
				 * max rating: 5
				 *//*
				for (int i = 1; i <= 5; i++) {
					ImageView iv = new ImageView(getContext());

					if (i <= app.getRating()) {
						iv.setImageDrawable(getContext().getResources()
								.getDrawable(R.drawable.start_checked));
					} else {
						iv.setImageDrawable(getContext().getResources()
								.getDrawable(R.drawable.start_unchecked));
					}

					ratingCntr.addView(iv);
				}
			}*/
		}

		return v;
	}

	/*@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppNameFilter();
		return filter;
	}*/

	/*private class AppNameFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<ApplicationListItem> filt = new ArrayList<ApplicationListItem>();
				
				*//*synchronized (this) {
					lItems.addAll(items);
				}*//*
				for (int i = 0, l = orginalItems.size(); i < l; i++) {
					ApplicationListItem m = orginalItems.get(i);
					if (m.getTitle().toLowerCase().contains(constraint))
						filt.add(m);
				}
				result.count = filt.size();
				result.values = filt;
			} else {
				//synchronized (this) {
					result.values = orginalItems;
					result.count = orginalItems.size();
				//}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			filtered = (ArrayList<ApplicationListItem>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++){
				add(filtered.get(i));
			notifyDataSetInvalidated();}
		}
	}*/
}

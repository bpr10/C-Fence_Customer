package com.anipr.mosambe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Home extends Activity {
	ListView cardsList;
	TextView userDetails;
	ImageView forwardBtn;
	CardsAdapter mCardsAdapter;
	List<Card> cards;
	AlertDialog.Builder bulider;
	ParseObject card_obj;
	boolean status;
	private String TAG = getClass().getSimpleName();
	private ProgressDialog pDialog;
	@Override
	protected void onStart() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		mCardsAdapter = new CardsAdapter();
		cardsList.invalidateViews();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Log.d(TAG, "OnCreate");
		pDialog = new ProgressDialog(Home.this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
		cardsList = (ListView) findViewById(R.id.cards_list);
		userDetails = (TextView) findViewById(R.id.user_name);
		forwardBtn = (ImageView) findViewById(R.id.forward_btn);
		cards = new ArrayList<Card>();
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(ParseConstants.cardsObject);
		query.orderByAscending(ParseConstants.user);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> card, ParseException e) {
				if (e == null) {
					Log.d(TAG, " Parse Response Success");
					String[] cardnos = new String[card.size()];
					Log.d(TAG, "" + card.size());
					for (ParseObject mCard : card) {
						Number cardNo = mCard
								.getNumber(ParseConstants.cardNumber);
						String holderName = mCard.getString("user");
						String expiery = mCard.getCreatedAt().toString()
								.substring(4, 10);
						String company = "visa";
						boolean status = mCard.getBoolean("status");
						cards.add(new Card(cardNo, holderName, "Exp :"
								+ expiery, company, status));

					}

					mCardsAdapter = new CardsAdapter();
					if (pDialog.isShowing()) {
						pDialog.dismiss();
					}
					cardsList.setAdapter(mCardsAdapter);
				} else {
					if (pDialog.isShowing()) {
						pDialog.dismiss();
					}
					Toast.makeText(getApplicationContext(),
							"Failed " + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					Log.e(TAG, e.getMessage());
				}
			}
		});
		
	}

	class CardsAdapter extends BaseAdapter {
		Switch mSwitch;

		@Override
		public int getCount() {

			return cards.size();
		}

		@Override
		public Object getItem(int arg0) {
			return cards.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {

			if (convertView == null) {
				convertView = LayoutInflater.from(Home.this).inflate(
						R.layout.card_list_item, null);
			}
			Card currentCard = cards.get(arg0);
			mSwitch = (Switch) convertView.findViewById(R.id.card_switch);
			mSwitch.setChecked(currentCard.isStatus());
			if(currentCard.isStatus()){
				mSwitch.setBackgroundColor(getResources().getColor(R.color.green));
			}else{
				mSwitch.setBackgroundColor(getResources().getColor(R.color.text_red));
			}
			TextView cardNo = (TextView) convertView.findViewById(R.id.card_no);
			cardNo.setText("************"
					+ String.valueOf(currentCard.cardNo).substring(12, 16));
			TextView holderName = (TextView) convertView
					.findViewById(R.id.holder_name);
			holderName.setText("Kishore Sawant");
			TextView expiery = (TextView) convertView
					.findViewById(R.id.expiery);
			expiery.setText(currentCard.expiery);

			mSwitch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					pDialog.show();

					final int position = cardsList.getPositionForView((View) v
							.getParent());
					Log.d(TAG, cards.get(position).isStatus() + "");

					ParseQuery<ParseObject> query = ParseQuery
							.getQuery(ParseConstants.cardsObject);
					query.whereEqualTo(ParseConstants.cardNumber,
							cards.get(position).cardNo);
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> object,
								ParseException e) {

							if (e == null) {
								String objId = object.get(0).getObjectId();
								// Log.d(TAG,
								// object.get(0).getObjectId()+":"+object.get(0).get(ParseConstants.cardNumber));
								ParseQuery<ParseObject> updateQuery = ParseQuery
										.getQuery(ParseConstants.cardsObject);
								updateQuery.getInBackground(objId,
										new GetCallback<ParseObject>() {
											public void done(
													ParseObject cardObject,
													ParseException e) {
												card_obj = cardObject;
												if (e == null) {
													bulider = new AlertDialog.Builder(
															Home.this);
													if (cards.get(position)
															.isStatus()) {
														cards.get(position)
																.setStatus(
																		false);
														status = false;
														card_obj.put(
																ParseConstants.status,
																status);
														card_obj.saveInBackground();
														bulider.setMessage(
																"Card Deactivated")
																.setTitle(
																		"Card Status")
																.setPositiveButton(
																		"ok",
																		null);
														cardsList.invalidateViews();
														if (pDialog.isShowing()) {
															pDialog.dismiss();
														}
														AlertDialog dialog = bulider
																.create();
														dialog.show();
													} else {
														AlertDialog.Builder passwordBulider = new AlertDialog.Builder(
																Home.this);
														passwordBulider
																.setTitle("PASSWORD");
														passwordBulider
																.setMessage("Enter OTP");
														final EditText input = new EditText(
																Home.this);
														input.setInputType(InputType.TYPE_CLASS_NUMBER);
														LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
																LinearLayout.LayoutParams.MATCH_PARENT,
																LinearLayout.LayoutParams.MATCH_PARENT);
														input.setLayoutParams(lp);
														passwordBulider
																.setView(input);
														passwordBulider.setCancelable(false);
//														passwordBulider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//															
//															@Override
//															public void onClick(DialogInterface dialog, int which) {
//																status = false;mSwitch.setChecked(false);
//																if (pDialog
//																		.isShowing()) {
//																	pDialog.dismiss();
//																}
//															}
//														});
														passwordBulider
																.setPositiveButton(
																		"Submit",
																		new DialogInterface.OnClickListener() {

																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int which) {
																				String password = input
																						.getText()
																						.toString();
																				if (password
																						.equals("1234")) {
																					cards.get(
																							position)
																							.setStatus(
																									true);
																					status = true;
																					card_obj.put(
																							ParseConstants.status,
																							status);
																					card_obj.saveInBackground();
																					Log.d(TAG,
																							"set to true");
																					bulider.setMessage(
																							"Card Activated")
																							.setTitle(
																									"Card Status")
																							.setPositiveButton(
																									"ok",
																									null);
																					if (pDialog
																							.isShowing()) {
																						pDialog.dismiss();
																					}
																					AlertDialog adialog = bulider
																							.create();
																					adialog.show();
																				} else {
																					if (pDialog
																							.isShowing()) {
																						pDialog.dismiss();
																					}
																					status = false;
																					mSwitch.setChecked(false);
																					Toast.makeText(
																							Home.this,
																							"Wrong Password",
																							1000)
																							.show();
																					Log.d(TAG,
																							"set to False");

																				}
																			}
																		});

														AlertDialog passwordDialog = passwordBulider
																.create();
														passwordDialog.show();

													}

												}
											}
										});
							} else {
								if (pDialog.isShowing()) {
									pDialog.dismiss();
								}
								Toast.makeText(getApplicationContext(),
										"Fail " + e.getMessage(), 1000).show();
								Log.e(TAG, e.getMessage());
							}
						}

					});

				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent (Home.this,CardDetails.class);
					startActivity(i);
				}
			});
			return convertView;
		}

	}

	class Card {
		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		Number cardNo;
		String holderName, expiery, company;
		private boolean status;

		Card(Number cardNo, String holderName, String expiery, String company,
				Boolean status) {
			this.cardNo = cardNo;
			this.holderName = holderName;
			this.expiery = expiery;
			this.company = company;
			this.status = status;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_logout) {
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();

		}
		return super.onOptionsItemSelected(item);
	}
}

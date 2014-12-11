package bryan.lars.bbapp14;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bryan.lars.bbapp14.data.Player;
import bryan.lars.bbapp14.fragments.ProgressFragActivity;
import bryan.lars.bbapp14.fragments.TimeFragActivity;
import bryan.lars.bbapp14.impl.HttpSuccessListener;
import bryan.lars.bbapp14.utils.DataUtils;
import bryan.lars.bbapp14.utils.ToastBuilder;
import bryan.lars.bbapp14.utils.http.GetHttp;
import bryan.lars.bbapp14.utils.http.PostHttp;
import bryan.lars.bbapp14.utils.http.PutHttp;

public class MainActivity extends Activity implements HttpSuccessListener, TimeFragActivity.IDateSetListener {

    static final String WEBSITE = "http://sanclementedev.org/BBwebapi00/api/players";
    static final List<String> POSITIONS = Arrays.asList(
            "Pitcher", "Catcher", "First Baseman", "Second Baseman", "Third Baseman",
            "Shortstop", "Left Field", "Center Field", "Right Field"
    );

    boolean dateFlag = false;
    int currentPlayer = 0;

    Spinner playerSpinner, positionSpinner, rankSpinner;
    EditText txtName, txtSalary;
    TextView txtHireDate;

    private final List<Player> players = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*************************************
        // Finish activity_main.xml
        //*************************************
        this.playerSpinner = new Spinner(this); //(Spinner) findViewById(R.id.spnPlayers);
        this.positionSpinner = new Spinner(this);
        this.rankSpinner = new Spinner(this);

        this.playerSpinner.setGravity(Gravity.FILL_HORIZONTAL);
        this.playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPlayer(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.positionSpinner.setGravity(Gravity.FILL_HORIZONTAL);
        this.positionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, POSITIONS));

        this.rankSpinner.setGravity(Gravity.FILL_HORIZONTAL);

        loadRankSpinner();
        getAll();

        GridLayout layout = (GridLayout) findViewById(R.id.gridLayout);//
        TextView selectPlayer = new TextView(this);
                 selectPlayer.setText("Select Player");

        layout.addView(selectPlayer);
        layout.addView(playerSpinner);
        for (String str : Arrays.asList("Name", "Rank", "Hire Date", "Position", "Salary")) {
            if (str == "Position") { // This has to be done here otherwise the app will look awful or crash
                TextView view = new TextView(this);
                         view.setText(str);

                layout.addView(view);
                layout.addView(positionSpinner);

                continue; //Do not allow the normal "Position" Views to be created
            } else if (str == "Rank") {
                TextView view = new TextView(this);
                         view.setText(str);

                layout.addView(view);
                layout.addView(rankSpinner);

                continue; //Do not allow the normal "Rank" Views to be created
            }

            TextView textView = new TextView(this);
                     textView.setText(str);

            EditText editText = new EditText(this);
                     editText.setGravity(Gravity.FILL_HORIZONTAL);

            if (str == "Name") {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                txtName = editText;
            } else if (str == "Hire Date") {
                editText.setTextAppearance(this, android.R.style.Widget_EditText);
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHireDate(v);
                    }
                });
                txtHireDate = editText;
            } else if (str == "Salary") {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                txtSalary = editText;
            }

            layout.addView(textView);
            layout.addView(editText);
        }

    }

    public void onHireDate(View v) {
        dateFlag = true;
        String hireDate = txtHireDate.getText().toString();
        Bundle args = new Bundle();
        args.putString("date", hireDate);

        DialogFragment date = new TimeFragActivity();
                       date.setArguments(args);
                       date.show(getFragmentManager(), "timePicker");
    }

    private void loadPlayer(int position) {
        Player player = null;

        try {
            player = players.get(position);
        } catch (Exception e) { e.printStackTrace(); }

        if (player == null) {
            txtName.setText("Error Loading #" + position);
            return;
        }

        currentPlayer = player.getId();
        txtName.setText(player.getName());
        txtHireDate.setText(player.getHireDate());
        txtSalary.setText(String.valueOf(player.getSalary()));

        positionSpinner.setSelection(player.getPosition() - 1);
        rankSpinner.setSelection(player.getRank() - 1);
    }

    private void getAll() {
        showProgress("Getting All Players...");
        GetHttp get = new GetHttp();
                get.setListener(this);
                get.setURL(WEBSITE);
                get.execute();

        ToastBuilder.builder().setText("Get All").show(this);
    }

    public void loadPlayers(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Player player = new Player();
                       player.setId(obj.getInt("Id"));
                       player.setHireDate(obj.getString("HireDate"));
                       player.setName(obj.getString("Name"));
                       player.setRank(obj.getInt("Rank"));
                       player.setPosition(obj.getInt("Position"));
                       player.setSalary(obj.getInt("Salary"));

                players.add(player);
            }

            ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, players);
            playerSpinner.setAdapter(adapter);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        CharSequence title = getActionBar().getTitle();
        getActionBar().setTitle("Bryan Larson Final Project");
        getActionBar().setSubtitle(title);

        //*************************************
        // Write A Menu
        //*************************************
        SubMenu more = menu.addSubMenu("More");
                more.getItem().setVisible(true);
                more.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_dark);
                more.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem save = more.add("Save");
                 save.setVisible(true);
                 save.setIcon(R.drawable.content_save);
                 save.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem discard = more.add("Discard");
                 discard.setIcon(R.drawable.content_discard);
                 discard.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem newItem = more.add("New");
                 newItem.setVisible(true);
                 newItem.setIcon(R.drawable.content_new);
                 newItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        CharSequence title = item.getTitle();

        Player player = null;
        boolean update = false;
        boolean insert = false;
        if (title == "Save") {
            // Figure out which "Save" option should we run
            insert = currentPlayer == 0; // This is false if not a "blank" player
            update = !insert; // This is the opposite of insert cause you cannot have both update and insert to be true

            player = new Player();

            player.setId(currentPlayer);
            player.setName(txtName.getText().toString());
            player.setHireDate(txtHireDate.getText().toString());
            player.setRank(rankSpinner.getSelectedItemPosition() + 1);
            player.setPosition(positionSpinner.getSelectedItemPosition() + 1);
            player.setSalary(DataUtils.stringToInt(txtSalary.getText().toString()));
        } else if (title == "Discard") {
            // This is not implemented server-side
            // Do nothing... Contact teacher about it before turning in assignment
        } else if (title == "New") {
            currentPlayer = 0;
            txtName.setText("");
            txtName.setHint("Name Required");
            rankSpinner.setSelection(24);
            txtHireDate.setText(DataUtils.getCurrentDate());
            positionSpinner.setSelection(0);
            txtSalary.setText("0");
        }

        if (player != null) {
            ToastBuilder.builder().setText("Insert: " + insert + "\nUpdate: " + update).show(this);
            if (DataUtils.isPlayerValid(this, player)) {
                if (insert) {
                    ToastBuilder.builder().setText("Inserting Player...").show(this);
                    PostHttp post = new PostHttp();
                             post.setPlayer(player);
                             post.setListener(this);
                             post.setURL(WEBSITE);
                             post.execute();

                    players.add(player);
                   // ((ArrayAdapter) playerSpinner.getAdapter()).add(player); // This is not required as i believe the players List is linked to the adapted
                                                                               // any change to the players list will automatically update the playerSpinner
                }

                if (update) {
                    ToastBuilder.builder().setText("Updating Player...").show(this);
                    PutHttp put = new PutHttp();
                            put.setListener(this);
                            put.setURL(WEBSITE);
                            put.setPlayer(player);
                            put.execute();

                    players.set(playerSpinner.getSelectedItemPosition(), player);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHttpSuccess(Type type, String result) {
        switch (type) {
            case GET:
                loadPlayers(result);
                dismissProgress();
                break;
            case PUT:
                dismissProgress();
                ToastBuilder.builder().setText(result).show(this);
                ((ArrayAdapter) playerSpinner.getAdapter()).notifyDataSetChanged();
                break;
            case POST:
                // We can add the Serialize the JSONObject to a Player here, but whats the point
                // we instead add the player to the players List and call notifyDataSetChanged() (see line 290)
                // here in order to FORCe an update (if required)
                ((ArrayAdapter) playerSpinner.getAdapter()).notifyDataSetChanged();
                playerSpinner.setSelection(playerSpinner.getCount() - 1);
                break;
        }
    }

    @Override
    public void onDateSet(String dateString) {
        if (dateFlag) {
            txtHireDate.setText(dateString);
        }
    }

    private void loadRankSpinner() {
        String[] ranks = new String[25];

        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = "#" + (i + 1);
        }

        this.rankSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ranks));
    }

    private void showProgress(String text) {
        DialogFragment frag = new ProgressFragActivity();
        Bundle bundle = new Bundle();
               bundle.putString("msg", text);
        frag.setArguments(bundle);
        frag.show(getFragmentManager(), "progressFrag");
    }

    private void dismissProgress() {
        FragmentManager manager = getFragmentManager();
        DialogFragment fragment = (DialogFragment) manager.findFragmentByTag("progressFrag");

        if (fragment != null) {
            fragment.dismiss();
        }
    }
}

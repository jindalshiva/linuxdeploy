package ru.meefik.linuxdeploy;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MountsActivity extends SherlockActivity {

    private ListView listView;
    private List<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefStore.setLocale(this);
        setContentView(R.layout.activity_mounts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.mountsView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, listItems);
        listView.setAdapter(adapter);
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(PrefStore.getTheme(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PrefStore.setLocale(this);
        getSupportMenuInflater().inflate(R.menu.activity_mounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText input = new EditText(this);
        final int pos = listView.getCheckedItemPosition();
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;
        case R.id.menu_new:
            new AlertDialog.Builder(this).setTitle(R.string.new_mount_title)
                    .setView(input).setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    String text = input.getText().toString()
                                            .replaceAll(" ", "_");
                                    if (text.length() > 0) {
                                        listItems.add(text);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }).setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
            break;
        case R.id.menu_edit:
            if (pos >= 0 && pos < listItems.size()) {
                input.setText(listItems.get(pos));
                input.setSelection(input.getText().length());
                new AlertDialog.Builder(this).setTitle(
                        R.string.edit_mount_title).setView(input)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                        String text = input.getText()
                                                .toString()
                                                .replaceAll(" ", "_");
                                        if (text.length() > 0) {
                                            listItems.set(pos, text);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }).setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                        dialog.cancel();
                                    }
                                }).show();
            }
            break;
        case R.id.menu_discard:
            if (pos >= 0 && pos < listItems.size()) {
                new AlertDialog.Builder(this).setTitle(
                        R.string.confirm_mount_discard_title).setMessage(
                        R.string.confirm_mount_discard_message).setIcon(
                        android.R.drawable.ic_dialog_alert)
                        .setCancelable(false).setPositiveButton(
                                android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                        listItems.remove(pos);
                                        adapter.notifyDataSetChanged();
                                    }
                                }).setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {
                                        dialog.cancel();
                                    }
                                }).show();
            }
            break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        String titleMsg = getString(R.string.title_activity_mounts) + ": "
                + PrefStore.getCurrentProfileTitle(getApplicationContext());
        setTitle(titleMsg);

        listItems.addAll(PrefStore.getMountsList(getApplicationContext()));
    }

    @Override
    public void onPause() {
        super.onPause();

        PrefStore.setMountsList(getApplicationContext(), listItems);
    }

}

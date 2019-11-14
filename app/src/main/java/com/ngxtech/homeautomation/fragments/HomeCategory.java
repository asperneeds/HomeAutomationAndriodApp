package com.ngxtech.homeautomation.fragments;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ngxtech.homeautomation.App;
import com.ngxtech.homeautomation.DeviceConfiguration;
import com.ngxtech.homeautomation.DeviceList;
import com.ngxtech.homeautomation.RecyclerTouchListener;
import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.db.DatabaseHelper;
import com.ngxtech.homeautomation.R;
import com.ngxtech.homeautomation.adapter.CategoryAdapter;
import com.ngxtech.homeautomation.utils.DebugLog;


import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCategory extends Fragment {
   private App app;
   private RecyclerView recyclerView;
    private Category category;
    private ArrayList<Category> names;
    private CategoryAdapter adapter;
    private Context context;
    private final int SDK_INT = Build.VERSION.SDK_INT;
    private FloatingActionButton fab;
    private  DatabaseHelper dbHelper;
//    android.support.v7.widget.Toolbar toolbar;
    Activity activity;



    public HomeCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homecategory, container, false);
        FloatingActionButton button = view.findViewById(R.id.fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        names = new ArrayList<>();
        category = new Category();
//        toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        assert activity != null;
//        activity.setSupportActionBar(toolbar);
//        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DebugLog.logTrace();



        app = (App) Objects.requireNonNull(getActivity()).getApplication();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategorydialog();
//                Intent intent=new Intent(getContext(), DeviceConfiguration.class);
//                startActivity(intent);



            }
        });



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                clickAction(position);

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);

            }
        }));

        return view;
    }

    private void showAddCategorydialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Category");
        alertDialog.setMessage("Enter Category Name");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryname = input.getText().toString();
                        Category category=new Category();
                        category.setCategory_name(categoryname);
                        dbHelper.insertData(category);

                        names =  dbHelper.GetData();
                        adapter = new CategoryAdapter(getContext(), names);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();




}
    private void clickAction(int position) {
        Category category =names.get(position);
        Intent intent=new Intent(getContext(), DeviceList.class);
        intent.putExtra("Itemid", category.getId());
        startActivity(intent);


    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, names.get(position), position);
                } else {
                   deleteCategoryname(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Category category, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.category_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && category != null) {
            inputNote.setText(category.getCategory_name());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(getActivity(), "Enter Category Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && category != null) {
                    // update note by it's id
                    updateCategoryname(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    //  createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }


    @Override
    public void onResume() {

   //     getDeviceList();
        dbHelper = DatabaseHelper.getInstance(getActivity());
        names =  dbHelper.GetData();

        adapter = new CategoryAdapter(getContext(), names);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    private void updateCategoryname(String note, int position) {
        Category n = names.get(position);
        // updating note text
        n.setCategory_name(note);

        // updating note in db
      dbHelper.updateCategoryname(n);

        // refreshing the list
        names.set(position, n);
        adapter.notifyItemChanged(position);


    }

    private void deleteCategoryname(int position) {
        // deleting the note from db
       dbHelper.deleteCategoryname(names.get(position));

        // removing the note from the list
        names.remove(position);
        adapter.notifyItemRemoved(position);


    }



}

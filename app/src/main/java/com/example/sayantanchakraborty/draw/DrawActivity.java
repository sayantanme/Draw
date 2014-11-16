package com.example.sayantanchakraborty.draw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class DrawActivity extends Activity {

    private static final String TAG = "DrawActivity";
    private DrawView drawView;
    private ImageButton currPaint,drwBtn;
    private float smallBrush,mediumBrush,largeBrush;
    String mFileName;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView=(DrawView)findViewById(R.id.drawing);
        LinearLayout paintLayout=(LinearLayout)findViewById(R.id.paint_colors);

        currPaint=(ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush=getResources().getInteger(R.integer.small_size);
        mediumBrush=getResources().getInteger(R.integer.medium_size);
        largeBrush=getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize(smallBrush);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Drawings/";
        makeDirectorySdCard(mFileName);



    }

    private void makeDirectorySdCard(String path) {
        File dir=new File(path);
        try{
            if(dir.mkdir())
                Log.d(TAG, "created direc:" + dir);
            else
                Log.d(TAG,"created not direc:");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.NewFile:

                AlertDialog.Builder newDialog=new AlertDialog.Builder(this);
                newDialog.setTitle("Create New Drawing");
                newDialog.setMessage("Really??You wan to make fun of another person.?(Suit yourself you are gonna loose the current one!!)");
                newDialog.setPositiveButton("Heck Ya!",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        drawView.startNew();
                        dialogInterface.dismiss();
                    }
                });
                newDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                newDialog.show();

                return true;

            case R.id.Brush:
                final Dialog brushDialog=new Dialog(this);
                brushDialog.setTitle("Brush Sizes:");
                brushDialog.setContentView(R.layout.brush_chooser);

                ImageButton smllBtn=(ImageButton)brushDialog.findViewById(R.id.small_brush);
                smllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawView.setBrushSize(smallBrush);
                        drawView.setLastBrushSize(smallBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });

                ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(mediumBrush);
                        drawView.setLastBrushSize(mediumBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });
                ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(largeBrush);
                        drawView.setLastBrushSize(largeBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });

                brushDialog.show();
                Toast.makeText(this,"brushhs",Toast.LENGTH_LONG).show();
                return true;

            case R.id.Eraser:

                final Dialog eraseDialog=new Dialog(this);
                eraseDialog.setTitle("Eraser Size:");
                eraseDialog.setContentView(R.layout.brush_chooser);

                ImageButton smllButton=(ImageButton)eraseDialog.findViewById(R.id.small_brush);
                smllButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawView.setErase(true);
                        drawView.setBrushSize(smallBrush);
                        eraseDialog.dismiss();
                    }
                });

                ImageButton medButton=(ImageButton)eraseDialog.findViewById(R.id.medium_brush);
                medButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawView.setErase(true);
                        drawView.setBrushSize(mediumBrush);
                        eraseDialog.dismiss();
                    }
                });

                ImageButton lrgButton=(ImageButton)eraseDialog.findViewById(R.id.large_brush);
                lrgButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawView.setErase(true);
                        drawView.setBrushSize(largeBrush);
                        eraseDialog.dismiss();
                    }
                });
                eraseDialog.show();

                return true;

            case R.id.Save:
                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                LayoutInflater layoutInflater=this.getLayoutInflater();
                View view=layoutInflater.inflate(R.layout.dialog_name,null);
                editText=(EditText)view.findViewById(R.id.editText);
                builder.setView(view)
                        .setPositiveButton("Yahh! Save this baby!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                               String picName= editText.getText().toString();
                               mFileName=mFileName+picName+".png";
                                Toast.makeText(DrawActivity.this,mFileName,Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
                return true;

            default: return false;
        }


    }

    public void paintClicked(View view) {
        drawView.setErase(false);
        if(view!=currPaint){
            ImageButton imgView=(ImageButton)view;
            String color=view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }
}

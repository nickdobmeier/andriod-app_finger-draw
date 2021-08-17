// Dobmeier

// In this file, the XML is inflated (including the custom Follow-View class), and also creates a listener for the SeekBar,
//    and click-listener for all of the color buttons, and another click-listener for the undo button

package com.utd.drawasgnmnt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    int seekBarProgress = 1;

    Button redButton;
    Button greenButton;
    Button blueButton;
    Button blackButton;

    Follow followView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redButton = findViewById(R.id.redBtn);
        greenButton = findViewById(R.id.greenBtn);
        blueButton = findViewById(R.id.blueBtn);
        blackButton = findViewById(R.id.blackBtn);
        followView = findViewById(R.id.follow1);

        SeekBar seekBar = findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  // set onChangeListener so that program can keep track of when user wants to change the radius of the circles/lines being drawn
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                seekBarProgress = seekBar.getProgress();
                followView.setCurrentCircleRadius(seekBarProgress);     // change circle/line radius size
            }
        });

        seekBarProgress = seekBar.getProgress();                        // default SeekBar progress of 15 is initialized in the XML code
        followView.setCurrentCircleRadius(seekBarProgress);             // tell the custom View object that by default, circles/lines of radius 15 are to be drawn
    }


    public void colorOnClick(View view)
    {
        Button buttonClicked = (Button) view;
        if(buttonClicked.hashCode() == redButton.hashCode()){
            //System.out.println("red");
            followView.setCurrentCircleColor(Color.rgb(0xFF, 0x00, 0x00));  // hex-code for RED
        }

        else if(buttonClicked.hashCode() == greenButton.hashCode()){
            //System.out.println("green");
            followView.setCurrentCircleColor(Color.rgb(0x00, 0xFF, 0x00));  // hex-code for GREEN
        }

        else if(buttonClicked.hashCode() == blueButton.hashCode()){
            //System.out.println("blue");
            followView.setCurrentCircleColor(Color.rgb(0x00, 0x00, 0xFF));  // hex-code for BLUE
        }

        else if(buttonClicked.hashCode() == blackButton.hashCode()){
            //System.out.println("black");
            followView.setCurrentCircleColor(Color.BLACK);
        }
    }

    public void undoOnClick(View view)
    {
        boolean didRemoveSomething = followView.removeLastDraw();
        if(didRemoveSomething == false){
            Toast.makeText(getApplicationContext(),"Nothing to UNDO",Toast.LENGTH_LONG).show();
        }
    }
}

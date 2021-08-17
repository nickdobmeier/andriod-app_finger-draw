// Dobmeier

// In this file,  the constructor creates a Stack of ArrayLists (of Circles), and also creates an onTouchListener for this View object that appears in the XML
//    onDraw() is overridden to draw EVERY circle stored in the Stack of ArrayLists (of Circles) each time invalidate is called
//    Additionally, a Circle INNER-class is present to act as an information-expert to represent the attributes of a circle/point on-screen

package com.utd.drawasgnmnt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Stack;

                                                                            // the view derived class must be its own Java file
public class Follow extends androidx.appcompat.widget.AppCompatImageView    // the surface we draw on must extend View
{
        // when you draw on a View, what is actually being drawn on is a Canvas object. The View gives onDraw() method a Canvas, and that is what you draw on

    Circle currentCircle;                                                   // state of the most recent circle/point that is to be drawn
    Stack<ArrayList<Circle>> StackOfArrays = new Stack<>();


        // instantiated by appearing in the XML code...
    public Follow(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        currentCircle = new Circle(0.0f, Color.RED, 0.0f, 0.0f);        // circle/point drawings have DEFAULT color of red
        StackOfArrays.push(new ArrayList<>(30));
        //StackOfArrays.peek().add(currentCircle);

        // must save previous X & Y coordinates so that we can remember everything we have drawn in the past (could look up each points X & Y in a hash table and see if that location has already been pointed over? no bad idea)
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == event.ACTION_UP)                         // indicates the user has PICKED UP their finger from the screen
                {
                    StackOfArrays.push(new ArrayList<>(50));        // create a new & separate sequence of Circles/LINES
                                                                                 // do NOT add currentCircle, because when the action is ACTION_UP, the coordinates of the event (and thus currentCircle) are a repeat will have already have been added
                }else{
                    currentCircle.setX(event.getX());
                    currentCircle.setY(event.getY());
                                                                                 // here we are performing a DEEP-copy of the current circle (thus copying the actual contents of the circle state vs. just a shallow copy where only a reference is copied)
                                                                                        // .getColor() returns an integer, NOT a Paint object, so we are NOT ever shallow copying a Paint object reference
                    StackOfArrays.peek().add(new Circle(currentCircle.getRadius(), currentCircle.getColor(), currentCircle.getX(), currentCircle.getY()));

                    invalidate();                                                 // this in-directly calls onDraw(), which will RE-DRAWs all Circles in the Stack of Arrays
                }
                return true;
            }
        });


    }


    Paint paintCurrCircle = new Paint();        // PRE-allocate a Paint object for the onDraw() method, so can avoid doing an allocation while drawing on-screen

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int stackSize = StackOfArrays.size();

        for(int i = 0; i < stackSize; i++)                                          // iterate through each disjoint drawing sequence, represented as an element in the stack
        {
            int currentArraySize = StackOfArrays.get(i).size();                     // iterate through every circle WITHIN a given drawing sequence
            for(int g = 1; g < currentArraySize; g++)
            {
                Circle prevPoint = StackOfArrays.get(i).get(g-1);                   // need previous point when drawing LINES
                Circle circle = StackOfArrays.get(i).get(g);

                paintCurrCircle.setColor(circle.getColor());                        // re-use same paint object just for the purposes of drawing circles, instead of having EVERY circle saved in memory have it's own Paint object (to save lots of memory)
                paintCurrCircle.setStrokeWidth(circle.getRadius());                 // need to set StrokeWidth when drawing LINES because drawLine() does not implement a width value directly

                    // CIRCLE implementation
                //canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), paintCurrCircle);     // draw each circle
                                                                                                            // (circles were mostly for the follow-me in-class assignment that was like a warm-up/precursor to this assignment)

                    // LINE implementation
                canvas.drawLine(prevPoint.getX(), prevPoint.getY(), circle.getX(), circle.getY(), paintCurrCircle); // draw line from previous point to current point
            }
        }
    }


    public void setCurrentCircleRadius(float currentCircleRadius) { this.currentCircle.setRadius(currentCircleRadius); }

    public void setCurrentCircleColor(int currentCircleColor) { this.currentCircle.setColor(currentCircleColor); }

    public boolean removeLastDraw()
    {
        int stackSize = StackOfArrays.size();
        if(stackSize <= 1){                 // stack was ALREADY empty (ie. NOTHING was previously drawn)
            return false;
        }

        StackOfArrays.pop();                // the top of the stack WAS just where new an empty but initialized list
        StackOfArrays.peek().clear();       // clear the more recently drawn item, thus making the new top an empty but initialized list

        invalidate();                       // RE-DRAW stack WITHOUT the most recent drawing
        return true;
    }




        // information-expert class for representing the attributes of each circle/point that appears on-screen
    private class Circle
    {
        private float radius;
        //private Paint paintCircle;        // every circle does NOT need to save a Paint Object, as all we really care about is the color, so we can just save that as a smaller integer value to save memory
        private int color;
        private float Xcoord;
        private float Ycoord;

        public Circle(float radius, int color, float Xcoord, float Ycoord){
            this.radius = radius;
            //paintCircle = new Paint();
            //paintCircle.setColor(color);
            this.color = color;
            this.Xcoord = Xcoord;
            this.Ycoord = Ycoord;
        }

        public float getRadius() { return radius; }
        public int getColor() { return color; }
        public float getX() { return Xcoord; }
        public float getY() { return Ycoord; }
        //public Paint getPaint() { return paintCircle; }

        public void setRadius(float radius) { this.radius = radius; }
        public void setColor(int color) { this.color = color; }
        public void setX(float X) { this.Xcoord = X; }
        public void setY(float Y) { this.Ycoord = Y; }
    }
}

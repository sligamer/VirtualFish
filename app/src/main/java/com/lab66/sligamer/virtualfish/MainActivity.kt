package com.lab66.sligamer.virtualfish

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.content.ContentValues.TAG
import android.util.DisplayMetrics
import android.util.Log




/**
 * Created by Justin Freres on 3/31/2018.
 * Lab 6-6 Virtual Fish
 * Plugin Support with kotlin_version = '1.2.31'
 */
class MainActivity : Activity() {

    // ANIMATION IS SPLIT INTO TWO THREADS
    // CALCULATING MOVEMENT
    // FISH TANK UPDATES: UI THREAD
    private lateinit var calculateMovementThread: Thread

    // FISH TANK ELEMENTS AND PROPERTIES
    private lateinit var foliageImageView: ImageView
    private lateinit var fishImageView: ImageView
    private lateinit var mFish: Fish
    private var tankWidth: Int = 0
    private var tankHeight: Int = 0
    private lateinit var fishTankLayout: FrameLayout

    companion object {
        const val DELAY = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 1: SET THE LAYOUT
        setContentView(R.layout.activity_frame)

        // TASK 2: CREATE REFS OF THE FRAME LAYOUT CONTAINER
        fishTankLayout = findViewById(R.id.container)

        // TASK 3: GET THE DIMENSIONS OF THE SCREEN
        // TO USE FOR THE TANK SIZE

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        tankWidth = size.x
        tankHeight = size.y

        // TASK 4: INSTANTIATE A FISH
        val initialXPosition = 0
        val initialYPosition = 0

        mFish = Fish(initialXPosition, initialYPosition, Fish.IsSwimming, tankWidth, tankHeight)

        // TASK 5: BUILD THE TANK ELEMENTS
        buildTank()

        // TASK 6: CONSTRUCT THE THREAD TO CALCULATE MOVEMENT
        // AND ANIMATE THE MOVEMENT
        calculateMovementThread = Thread(calculateMovement)

        // TASK 7:  START THE THREAD
        calculateMovementThread.start()

    }

    private fun buildTank() {

        // TASK 1: CREATE A LAYOUT INFLATER TO
        // ADD VISUAL VIEWS TO THE LAYOUT
        layoutInflater.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)

        // TASK 2: ADD THE FOLIAGE
        foliageImageView = layoutInflater.inflate(R.layout.foliage_layout, null) as ImageView
        foliageImageView.x = 0F
        foliageImageView.y = 0F
        foliageImageView.alpha = .97F

        fishTankLayout.addView(foliageImageView, 0)

        // TASK 3: ADD THE VIRTUAL FISH
        fishImageView = layoutInflater.inflate(R.layout.fish_image, null) as ImageView
        fishImageView.scaleX = .3F
        fishImageView.scaleY = .3F
        fishImageView.x  = mFish.x.toFloat()
        fishImageView.y  =  mFish.y.toFloat()


        fishTankLayout.addView(fishImageView, 0)


    }

    private val calculateMovement: Runnable = Runnable {
        try {
            while (true) {
                mFish.move()
                Thread.sleep(DELAY.toLong())
                updateTankHandler.sendEmptyMessage(0)
            }
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private val updateTankHandler: Handler = object: Handler() {
        override fun handleMessage(msg: Message?) {

            // TASK 1: FACE THE FISH IN THE CORRECT DIRECTION
            fishImageView.scaleX = (.3F * mFish.getFacingDirection())

            // TASK 2: SET THE FISH AT THE CORRECT XY LOCATION
            fishImageView.x = mFish.x.toFloat()
            fishImageView.y = mFish.y.toFloat()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        if(id == R.string.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

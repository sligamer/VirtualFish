package com.lab66.sligamer.virtualfish

/**
 * Created by Justin Freres on 3/31/2018.
 * Lab 6-6 Virtual Fish
 * Plugin Support with kotlin_version = '1.2.31'
 */
class Fish// FOOT AND EXPLORE LOCATIONS ARE FIXED
// IN THE TOP AND BOTTOM OF THE TANK
(xPos: Int, yPos: Int, condition: Int, tankWidth: Int, tankHeight: Int) {

    // FISH ARTWORK OBJECT HAS A CURRENT POSITION
    // POSITION TO THAT CAN BE ACCESSED PUBLICLY
    var x: Int = xPos
    var y: Int = yPos

    companion object {
        const val IsHungry: Int = 1
        const val IsSwimming: Int = 2
        const val IsEating: Int = 3
    }

    private var mCondition: Int = condition
    private var mVelocity: Int = 0
    private var mStomachCapacity: Int = 0
    private var mFoodInStomach: Int = 0
    private var mTankWidth: Int = tankWidth
    private var mTankHeight: Int = tankHeight
    private var mDirection: Int = 0

    // SEPARATE THE DECLARES JAVA BEST PRACTICE
    private var playX: Int = 0
    private var playY: Int = 0
    private var foodX: Int = 0
    private var foodY: Int = 0

    fun move(){

        // EXAMINE POSSIBLE CONDITIONS
        when(mCondition){
            IsSwimming -> { swim() }
            IsHungry -> { findFood() }
            IsEating -> { eatFood() }
        }
    }

    private fun swim() {

        // TASK 1: BURN A CALORIE OF FOOD
        mFoodInStomach--

        // TASK 2: SWIM TOWARD A POINT OF INTEREST: playX, playY
        val xDistance = playX - x
        val yDistance = playY - y

        x += xDistance / mVelocity
        y += yDistance / mVelocity

        mDirection = when {
            playX < x -> {
                -1
            }
            else -> {
                1
            }
        }

        // TASK 3: FIND ANOTHER PLACE TO EXPLORE
        // IN THE TOP HALF OF THE TANK
        // FIND A PLACE TO EAT IN THE BOTTOM OF THE TANK
        when {
            Math.abs(xDistance) < 5 && Math.abs(yDistance) < 5 -> {
                playX = (Math.ceil(Math.random() * mTankWidth) - mTankWidth / 2).toInt()
                playY = (-(Math.random() * mTankHeight / 2).toInt() + 100)
            }
        }


        // TASK 4: DETERMINE IF STOMACH IS EMPTY
        when {
            mFoodInStomach <= 0 -> {
                mCondition = IsHungry
                // FIND A PLACE TO EAT IN THE BOTTOM OF THE TANK
                foodX = ((Math.ceil(Math.random() * mTankWidth) - mTankWidth / 2).toInt() - 100)
            }
        }
    }

    private fun findFood() {

        // TASK 1: SWIM TOWARD FOOD: foodX, foodY
        val xDistance: Int = foodX - x
        val yDistance: Int = foodY - y

        x += xDistance / mVelocity
        y += yDistance / mVelocity

        // TASK 2: TURN FISH IN DIRECTION OF FOOD IF FOUND
        when {
            foodX < x -> mDirection = -1
            else -> mDirection = 1
        }

        // TASK 3: DETERMINE IF FOOD IS FOUND
        when {
            Math.abs((x - foodX)) <= 10 && Math.abs((y  - foodY)) <= 10 -> {
                mCondition = IsEating
            }
        }
    }

    private fun eatFood() {

        // TASK 1: ADD A CALORIE OF FOOD TO THE STOMACH
        mFoodInStomach += 4

        // TASK 2: DETERMINE IF STOMACH IS FULL
        when {
            mFoodInStomach >= mStomachCapacity -> {
                mCondition = IsSwimming

                // FIND A NEW PLACE TO SWIM
                playX = (Math.ceil(Math.random() * mTankWidth) - mTankWidth / 2).toInt()
                playY = (-(Math.random() * mTankHeight / 2).toInt() + 100)
            }
        }
    }

    fun getFacingDirection(): Int{
        return mDirection
    }

    // DEFAULT INIT
    init {
        mVelocity = 3
        mStomachCapacity = 80
        mFoodInStomach = mStomachCapacity
        mDirection = 1
        foodY = (tankHeight / 2) - 100
        foodX = (Math.ceil(Math.random() * mTankWidth) - mTankWidth / 2).toInt()
        playY = (-(Math.random() * mTankHeight / 2).toInt() + 100)
        playX = (Math.ceil(Math.random() * mTankWidth) - mTankWidth / 2).toInt()
    }
}
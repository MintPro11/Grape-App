package com.example.grape_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val GRAPE_STATE = "GRAPE_STATE"
    private val GRAPE_SIZE = "GRAPE_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    private val SELECT = "select"
    private val SQUEEZE = "squeeze"
    private val DRINK = "drink"
    private val RESTART = "restart"
    private var grapeState = "select"
    private var grapeSize = -1
    private var squeezeCount = -1
    private var grapeTree = GrapeTree()
    private var grapeImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            grapeState = savedInstanceState.getString(GRAPE_STATE, "select")
            grapeSize = savedInstanceState.getInt(GRAPE_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        grapeImage = findViewById(R.id.image_grape_state)
        setViewElements()
        grapeImage!!.setOnClickListener { clickGrapeImage() }
        grapeImage!!.setOnLongClickListener { showSnackbar() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(GRAPE_STATE, grapeState)
        outState.putInt(GRAPE_SIZE, grapeSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    private fun clickGrapeImage() {
        when (grapeState) {
            SELECT -> {
                grapeSize = grapeTree.pick()
                squeezeCount = 0
                grapeState = SQUEEZE
            }
            SQUEEZE -> {
                squeezeCount++
                grapeSize--
                if (grapeSize < 1) {
                    grapeState = DRINK
                    grapeSize = -1
                }
            }
            DRINK -> {
                grapeState = RESTART
            }
            else -> {
                grapeState = SELECT
            }
        }
        setViewElements()
    }

    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        val drawableResource = when (grapeState) {
            SELECT -> R.drawable.im_tree
            SQUEEZE -> R.drawable.im_fruit
            DRINK -> R.drawable.im_glass2
            else -> R.drawable.im_glass1
        }
        val stringResource = when (grapeState) {
            SELECT -> "Click to select a grape"
            SQUEEZE -> "Click to Juice the grape"
            DRINK -> "Click to drink your grape"
            else -> "Click to start again!"
        }
        grapeImage?.setImageResource(drawableResource)
        textAction.text = stringResource
    }

    private fun showSnackbar(): Boolean {
        if (grapeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(androidx.constraintlayout.widget.R.id.constraint),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

class GrapeTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
package com.example.drawingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawingapp.DrawingView.Companion.currentColorShape
import com.example.drawingapp.DrawingView.Companion.currentShape
import com.example.drawingapp.DrawingView.Companion.mDrawPaint
import com.example.drawingapp.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()

        mDrawPaint.color = resources.getColor(R.color.black, context?.theme)
        currentColor(mDrawPaint.color)


    }

    private fun setOnClickListener() {
        binding.toolbar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> currentShape = Shapes.DRAW_A_STROKE_SHAPE
                    1 -> currentShape = Shapes.DRAW_AN_ARROW_SHAPE
                    2 -> currentShape = Shapes.DRAW_A_RECTANGLE_SHAPE
                    3 -> currentShape = Shapes.DRAW_A_CIRCLE_SHAPE
                    4 -> selectColor()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    4 -> {
                        binding.colorsPalette.visibility = View.GONE
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    4 -> {
                        binding.colorsPalette.visibility = View.VISIBLE
                    }
                }
            }
        })

        binding.redColor.setOnClickListener {
            setColorShapes(0)
        }
        binding.greenColor.setOnClickListener {
            setColorShapes(1)
        }
        binding.blueColor.setOnClickListener {
            setColorShapes(2)
        }
        binding.blackColor.setOnClickListener {
            setColorShapes(3)
        }
    }

    private fun setColorShapes(position: Int) {
        when (position) {
            0 -> {
                mDrawPaint.color = requireContext().getColor(R.color.red)
                currentColor(mDrawPaint.color)
                selectColor()
            }
            1 -> {
                mDrawPaint.color = requireContext().getColor(R.color.green)
                currentColor(mDrawPaint.color)
                selectColor()
            }
            2 -> {
                mDrawPaint.color = requireContext().getColor(R.color.blue)
                currentColor(mDrawPaint.color)
                selectColor()
            }
            3 -> {
                mDrawPaint.color = requireContext().getColor(R.color.blackk)
                currentColor(mDrawPaint.color)
                selectColor()
            }
        }
    }


    private fun selectColor() {
        if (binding.colorsPalette.visibility == View.VISIBLE)
            binding.colorsPalette.visibility = View.GONE
        else
            binding.colorsPalette.visibility = View.VISIBLE
    }

    private fun currentColor(color: Int) {
        currentColorShape = color
    }
}
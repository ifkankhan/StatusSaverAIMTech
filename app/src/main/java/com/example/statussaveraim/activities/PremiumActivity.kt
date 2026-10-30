package com.example.statussaveraim.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.statussaveraim.R
import com.example.statussaveraim.databinding.ActivityPremiumBinding

class PremiumActivity : AppCompatActivity() {

    private val binding: ActivityPremiumBinding by lazy {
        ActivityPremiumBinding.inflate(layoutInflater)
    }

    private var isYearlySelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.clWeeklyPlan.setOnClickListener {
            if (isYearlySelected) {
                isYearlySelected = false
                updatePlanUI()
            }
        }

        binding.clYearlyPlan.setOnClickListener {
            if (!isYearlySelected) {
                isYearlySelected = true
                updatePlanUI()
            }
        }

        binding.btnContinue.setOnClickListener {
            val plan = if (isYearlySelected) "Yearly" else "Weekly"
            Toast.makeText(this, "$plan plan selected!", Toast.LENGTH_SHORT).show()
            // Implement billing logic here
        }

        binding.tvTerms.setOnClickListener {
            Toast.makeText(this, "Terms of Service", Toast.LENGTH_SHORT).show()
        }

        binding.tvRestore.setOnClickListener {
            Toast.makeText(this, "Restore Purchases", Toast.LENGTH_SHORT).show()
        }

        binding.tvPrivacy.setOnClickListener {
            Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePlanUI() {
        if (isYearlySelected) {
            // Select Yearly
            binding.clYearlyPlan.setBackgroundResource(R.drawable.bg_premium_plan_selected)
            binding.ivYearlyRadio.setImageResource(R.drawable.selected_plan)

            // Deselect Weekly
            binding.clWeeklyPlan.setBackgroundResource(R.drawable.bg_premium_plan_unselected)
            binding.ivWeeklyRadio.setImageResource(R.drawable.unselected_plan)
        } else {
            // Select Weekly
            binding.clWeeklyPlan.setBackgroundResource(R.drawable.bg_premium_plan_selected)
            binding.ivWeeklyRadio.setImageResource(R.drawable.selected_plan)

            // Deselect Yearly
            binding.clYearlyPlan.setBackgroundResource(R.drawable.bg_premium_plan_unselected)
            binding.ivYearlyRadio.setImageResource(R.drawable.unselected_plan)
        }
    }
}
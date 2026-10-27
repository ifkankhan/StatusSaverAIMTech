package com.example.statussaveraim.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.statussaveraim.adapters.LanguageAdapter
import com.example.statussaveraim.databinding.ActivityLanguageBinding
import com.example.statussaveraim.models.LanguageModel
import kotlin.getValue

class LanguageActivity : AppCompatActivity() {

    private val binding: ActivityLanguageBinding by lazy {
        ActivityLanguageBinding.inflate(layoutInflater)
    }

    private var selectedLanguage: LanguageModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        binding.btnSelect.setOnClickListener {
            if (selectedLanguage != null) {
                Toast.makeText(this, "Selected: ${selectedLanguage?.name}", Toast.LENGTH_SHORT).show()
                // Navigate to Intro Activity
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val languages = listOf(
            LanguageModel("Spanish", "es"),
            LanguageModel("French", "fr"),
            LanguageModel("Arabic", "sa"),
            LanguageModel("Germany", "de"),
            LanguageModel("English", "us", true),
            LanguageModel("Turkish", "tr"),
            LanguageModel("Russian", "ru"),
            LanguageModel("Chinese", "cn"),
            LanguageModel("Urdu", "pk"),
            LanguageModel("Indonesia", "id"),
            LanguageModel("Korean", "kr"),
            LanguageModel("Japanese", "jp"),
            LanguageModel("Italian", "it"),
            LanguageModel("Hindi", "in"),
            LanguageModel("Persian", "ir"),
            LanguageModel("Greek", "gr")
        )

        selectedLanguage = languages.find { it.isSelected }

        val adapter = LanguageAdapter(languages) { language ->
            selectedLanguage = language
        }

        binding.rvLanguages.adapter = adapter
    }
}
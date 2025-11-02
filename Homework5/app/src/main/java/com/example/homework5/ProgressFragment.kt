package com.example.homework5

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bar = view.findViewById<ProgressBar>(R.id.progressBar)
        val text = view.findViewById<TextView>(R.id.tvProgress)

        bar.isIndeterminate = true

        vm.status.observe(viewLifecycleOwner) { s ->
            text.text = s
            if (s.startsWith("工作中")) bar.isIndeterminate = false
        }

        vm.progress.observe(viewLifecycleOwner) { p ->
            if (!bar.isIndeterminate) {
                bar.max = 100
                bar.progress = p
                text.text = "工作中 $p%"
            }
        }
    }
}

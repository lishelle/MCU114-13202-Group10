package com.example.threadhandlerandprogressbar

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val circle = view.findViewById<CircularProgressView>(R.id.circularProgress)
        val text = view.findViewById<TextView>(R.id.txt)

        vm.status.observe(viewLifecycleOwner) { s ->
            text.text = s
        }

        vm.progress.observe(viewLifecycleOwner) { p ->
            circle.setProgress(p)
        }
    }
}

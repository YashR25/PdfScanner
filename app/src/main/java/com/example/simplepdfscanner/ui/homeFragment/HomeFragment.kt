package com.example.simplepdfscanner.ui.homeFragment

import android.app.Notification.Action
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplepdfscanner.R
import com.example.simplepdfscanner.adapter.OnItemClickListener
import com.example.simplepdfscanner.adapter.PdfAdapter
import com.example.simplepdfscanner.databinding.FragmentHomeBinding
import com.example.simplepdfscanner.model.PdfModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var list: List<PdfModel> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        val viewModel: HomeViewModel by viewModels()

        val adapter = PdfAdapter(OnItemClickListener {
//            val intent = Intent()
//            intent.action = Intent.ACTION_VIEW
//            intent.setDataAndType(Uri.parse(list[it].uri),"application/pdf")
//            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//            startActivity(intent)
            Toast.makeText(requireContext(),list[it].uri,Toast.LENGTH_SHORT).show()
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.pdfList.observe(viewLifecycleOwner){
            list = it
            adapter.submitList(it)
        }

        binding.startScan.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_createPdfFragment)
        }
        return binding.root
    }

}
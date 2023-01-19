package com.example.assignment3_courseregistrationapp.fragments.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.database.entity.User
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminLoginBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class AdminLoginFragment : Fragment() {
    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!


    private val viewModel: CourseViewModel by activityViewModels {
        CourseViewModelFactory(
            (activity?.application as CourseRegistrationApplication).database.courseDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.ivAdminLoginToUserLogin.setOnClickListener {
            val action = AdminLoginFragmentDirections.actionAdminLoginFragmentToUserLoginFragment()
            findNavController().navigate(action)
        }

        binding.btAdminLoginSubmit.setOnClickListener {
            checkAdminLoginPassword()
        }

    }


    private fun isAdminLoginEntryValid(): Boolean {
        return viewModel.isAdminLoginEntryValid(
            binding.etAdminLoginId.text.toString(),
            binding.etAdminLoginPassword.text.toString()
        )
    }

    private fun isAdminLoginCredentialsCorrect(): Boolean {
        return viewModel.isAdminLoginCredentialsCorrect(
            binding.etAdminLoginId.text.toString(),
            binding.etAdminLoginPassword.text.toString()
        )
    }

    private fun checkAdminLoginPassword() {
        if (isAdminLoginEntryValid() && isAdminLoginCredentialsCorrect()) {
            val action =
                AdminLoginFragmentDirections.actionAdminLoginFragmentToAdminCourseListFragment()
            findNavController().navigate(action)
            Toast.makeText(requireContext(),
                "Welcome ${binding.etAdminLoginId.text}",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Please Enter Correct Details", Toast.LENGTH_LONG)
                .show()
        }
    }

}
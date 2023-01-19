package com.example.assignment3_courseregistrationapp.fragments.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.database.entity.User
import com.example.assignment3_courseregistrationapp.databinding.FragmentUserLoginBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class UserLoginFragment : Fragment() {

    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var user: User


    private val viewModel: CourseViewModel by activityViewModels {
        CourseViewModelFactory(
            (activity?.application as CourseRegistrationApplication).database.courseDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btStudentRegister.setOnClickListener {
            val action =
                UserLoginFragmentDirections.actionUserLoginFragmentToUserRegistrationFragment()
            findNavController().navigate(action)
        }

        binding.btStudentLoginSubmit.setOnClickListener {
            checkLoginPassword()
        }

        binding.ivUserLoginToAdminLogin.setOnClickListener {
            val action = UserLoginFragmentDirections.actionUserLoginFragmentToAdminLoginFragment()
            findNavController().navigate(action)
        }

    }


    private fun isLoginEntryValid(): Boolean {
        return viewModel.isStudentLoginEntryValid(
            binding.etStudentLoginStudentId.text.toString(),
            binding.etStudentLoginPassword.text.toString()
        )
    }

    private fun isStudentLoginCredentialsCorrect(): Boolean {
        return viewModel.isStudentLoginCredentialsCorrect(
            binding.etStudentLoginStudentId.text.toString(),
            binding.etStudentLoginPassword.text.toString()
        )
    }

    private fun checkLoginPassword() {
        if (isLoginEntryValid() && isStudentLoginCredentialsCorrect()) {
            val action =
                UserLoginFragmentDirections.actionUserLoginFragmentToCourseListFragment(studentId = binding.etStudentLoginStudentId.text.toString())
            findNavController().navigate(action)
            Toast.makeText(requireContext(),
                "Welcome ${binding.etStudentLoginStudentId.text}",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Please Enter Correct Details", Toast.LENGTH_LONG)
                .show()
        }
    }

}
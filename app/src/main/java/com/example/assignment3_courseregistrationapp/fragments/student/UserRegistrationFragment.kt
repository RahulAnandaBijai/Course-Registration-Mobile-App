package com.example.assignment3_courseregistrationapp.fragments.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.databinding.FragmentUserRegistrationBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class UserRegistrationFragment : Fragment() {

    private var _binding: FragmentUserRegistrationBinding? = null
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
        _binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btRegisterSubmit.setOnClickListener {
            if (isUserPresent(binding.etRegisterStudentId.text.toString())){
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Alert")
                builder.setMessage(getString(R.string.user_id_already_exists))
                builder.setNeutralButton("OK") { dialog2, _ -> dialog2.cancel() }
                builder.create().show()
            }
            else
            {
                addNewStudent()
            }
        }

    }

    private fun isUserPresent(userId: String): Boolean{
        val fetchedUserId = viewModel.retrieveUser()
        if(fetchedUserId.contains(userId)){
            return true
        }
        return false
    }

    private fun isRegisterEntryValid(): Boolean {
        return viewModel.isStudentRegisterEntryValid(
            binding.etRegisterStudentId.text.toString(),
            binding.etRegisterStudentName.text.toString(),
            binding.etRegisterPassword.text.toString(),
            binding.etRegisterRepassword.text.toString()
        )
    }

    private fun addNewStudent() {
        if (isRegisterEntryValid() && binding.etRegisterPassword.text.toString() == binding.etRegisterRepassword.text.toString()) {
            viewModel.addNewStudent(
                binding.etRegisterStudentId.text.toString(),
                binding.etRegisterStudentName.text.toString(),
                binding.etRegisterPassword.text.toString()
            )

            val action =
                UserRegistrationFragmentDirections.actionUserRegistrationFragmentToUserLoginFragment()
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "You have successfully Registered", Toast.LENGTH_LONG)
                .show()

        } else {
            Toast.makeText(requireContext(), "Please fill all the details", Toast.LENGTH_SHORT)
                .show()
        }
    }


}
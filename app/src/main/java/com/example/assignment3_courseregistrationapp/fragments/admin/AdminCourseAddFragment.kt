package com.example.assignment3_courseregistrationapp.fragments.admin

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminCourseAddBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class AdminCourseAddFragment : Fragment() {

    private val navigationArgs: AdminCourseAddFragmentArgs by navArgs()
    lateinit var course: Course

    private var _binding: FragmentAdminCourseAddBinding? = null
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
        _binding = FragmentAdminCourseAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun isAddEntryValid(): Boolean {
        return viewModel.isAddEntryValid(
            binding.etCourseName.text.toString(),
            binding.etCourseId.text.toString(),
            binding.etCourseTerm.text.toString(),
            binding.etCoursePrerequisite.text.toString(),
            binding.etCourseType.text.toString()
        )
    }

    private fun bind(course: Course) {
        binding.apply {
            etCourseName.setText(course.courseName, TextView.BufferType.SPANNABLE)
            etCourseId.setText(course.courseId, TextView.BufferType.SPANNABLE)
            etCourseTerm.setText(course.courseTerm, TextView.BufferType.SPANNABLE)
            etCoursePrerequisite.setText(course.coursePrerequisite, TextView.BufferType.SPANNABLE)
            etCourseType.setText(course.courseType, TextView.BufferType.SPANNABLE)
            btSave.setOnClickListener { updateCourse() }
        }
    }

    private fun addNewCourse() {
        if (isAddEntryValid()) {
            viewModel.addNewCourse(
                binding.etCourseName.text.toString(),
                binding.etCourseId.text.toString(),
                binding.etCourseTerm.text.toString(),
                binding.etCoursePrerequisite.text.toString(),
                binding.etCourseType.text.toString()
            )
            val action =
                AdminCourseAddFragmentDirections.actionAdminCourseAddFragmentToAdminCourseListFragment()
            findNavController().navigate(action)
        }
    }


    private fun updateCourse() {
        if (isAddEntryValid()) {
            viewModel.updateCourse(
                binding.etCourseName.text.toString(),
                binding.etCourseId.text.toString(),
                binding.etCourseTerm.text.toString(),
                binding.etCoursePrerequisite.text.toString(),
                binding.etCourseType.text.toString()
            )
            val action =
                AdminCourseAddFragmentDirections.actionAdminCourseAddFragmentToAdminCourseListFragment()
            findNavController().navigate(action)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.courseId
        val term = navigationArgs.courseTerm
        if (id == "None") {
            binding.btSave.setOnClickListener {
                addNewCourse()
            }
        } else {
            viewModel.retrieveCourse(id, term).observe(this.viewLifecycleOwner) { selectedCourse ->
                course = selectedCourse
                bind(course)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userLoginFragment -> {
                val action =
                    AdminCourseAddFragmentDirections.actionAdminCourseAddFragmentToUserLoginFragment()
                view?.findNavController()?.navigate(action)
                Toast.makeText(requireContext(),
                    getString(R.string.logout_message),
                    Toast.LENGTH_SHORT).show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }


}
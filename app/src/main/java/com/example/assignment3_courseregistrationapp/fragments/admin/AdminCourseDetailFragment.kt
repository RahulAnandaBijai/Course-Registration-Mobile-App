package com.example.assignment3_courseregistrationapp.fragments.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminCourseDetailBinding
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminCourseListBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class AdminCourseDetailFragment : Fragment() {

    private val navigationArgs: AdminCourseDetailFragmentArgs by navArgs()
    lateinit var course: Course

    private var _binding: FragmentAdminCourseDetailBinding? = null
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
        _binding = FragmentAdminCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(course: Course) {
        binding.apply {
            tvAdminDetailsCourseName.text = course.courseName
            tvAdminDetailsCourseId.text = course.courseId
            tvAdminDetailsCourseTerm.text = course.courseTerm
            tvAdminDetailsCoursePrerequisite.text = course.coursePrerequisite
            tvAdminDetailsCourseType.text = course.courseType
            deleteSession.setOnClickListener { showConfirmationDialog() }
            editSession.setOnClickListener { editSession() }

        }
    }

    private fun editSession() {
        val action =
            AdminCourseDetailFragmentDirections.actionAdminCourseDetailFragmentToAdminCourseAddFragment(
                getString(R.string.edit_fragment_title),
                course.courseId,
                course.courseTerm
            )
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteCourse()
                Toast.makeText(requireContext(),
                    "Successfully Deleted the Course",
                    Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun deleteCourse() {
        viewModel.deleteCourse(course)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.courseId
        val term = navigationArgs.courseTerm

        viewModel.retrieveCourse(id, term).observe(this.viewLifecycleOwner) { selectedCourse ->
            course = selectedCourse
            bind(course)
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
                    AdminCourseDetailFragmentDirections.actionAdminCourseDetailFragmentToUserLoginFragment()
                view?.findNavController()?.navigate(action)
                Toast.makeText(requireContext(),
                    getString(R.string.logout_message),
                    Toast.LENGTH_SHORT).show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
package com.example.assignment3_courseregistrationapp.fragments.student

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.databinding.FragmentCourseDetailBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class CourseDetailFragment : Fragment() {


    private val navigationArgs: CourseDetailFragmentArgs by navArgs()
    lateinit var course: Course


    private var _binding: FragmentCourseDetailBinding? = null
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
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun bind(course: Course) {
        binding.apply {
            tvDetailsCourseName.text = course.courseName
            tvDetailsCourseId.text = course.courseId
            tvDetailsCourseTerm.text = course.courseTerm
            tvDetailsCoursePrerequisite.text = course.coursePrerequisite
            val studentId = navigationArgs.studentId
            val registeredCourseList = viewModel.getRegisteredCourseList(studentId)
            val completedCourseList = viewModel.getCompletedCourseList(studentId)
            val courseRegisteredId = registeredCourseList.map { it.courseId }
            val courseCompletedId = completedCourseList.map { it.courseId }
            when {
                courseCompletedId.contains(binding.tvDetailsCourseId.text.toString()) -> {
                    binding.tvDetailsCourseStatus.text = getString(R.string.completed)
                }
                courseRegisteredId.contains(binding.tvDetailsCourseId.text.toString()) -> {
                    binding.tvDetailsCourseStatus.text = getString(R.string.registered)
                }
                else -> {
                    binding.tvDetailsCourseStatus.text = getString(R.string.not_registered)
                }
            }


        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cId = navigationArgs.courseId
        val cTerm = navigationArgs.courseTerm
        val studentId = navigationArgs.studentId

        viewModel.retrieveCourse(cId, cTerm).observe(this.viewLifecycleOwner) { selectedCourse ->
            course = selectedCourse
            bind(course)
        }
        binding.btHomeNav.setOnClickListener {
            val action =
                CourseDetailFragmentDirections.actionCourseDetailFragmentToCourseListFragment(
                    studentId = studentId)
            view.findNavController().navigate(action)
        }


        binding.btRegister.setOnClickListener {
            validateCourseToRegister(studentId)
        }


    }

    private fun validateCourseToRegister(studentId: String) {
        val selectedCourseId = binding.tvDetailsCourseId.text.toString()
        val selectedCoursePrerequisite = binding.tvDetailsCoursePrerequisite.text.toString()
        val selectedCourseTerm = binding.tvDetailsCourseTerm.text.toString()
        val registeredCourseList = viewModel.getRegisteredCourseList(studentId)
        val completedCourseList = viewModel.getCompletedCourseList(studentId)
        val courseRegisteredId = registeredCourseList.map { it.courseId }
        val courseRegisteredTerm = registeredCourseList.map { it.courseTerm }
        val courseCompletedId = completedCourseList.map { it.courseId }
        val term1 = courseRegisteredTerm.count { it == "1" }
        val term2 = courseRegisteredTerm.count { it == "2" }

        val termReq: String = if (courseRegisteredId.contains(selectedCoursePrerequisite)) {
            val pos = courseRegisteredId.indexOf(selectedCoursePrerequisite)
            courseRegisteredTerm[pos]
        } else {
            "0"
        }

        if (courseCompletedId.contains(selectedCoursePrerequisite) || selectedCoursePrerequisite == "None") {
            if (courseCompletedId.contains(selectedCourseId)) {
                showAlertBox(getString(R.string.completed_course_message))
            } else if (selectedCourseTerm == "1" && term1 < 3) {
                if (courseRegisteredId.contains(selectedCourseId)) {
                    showAlertBox(getString(R.string.already_registered_message))
                } else {
                    addRegisteredCourse(studentId)
                    Toast.makeText(requireActivity(),
                        getString(R.string.success_register_message),
                        Toast.LENGTH_SHORT).show()
                    binding.tvDetailsCourseStatus.text = getString(R.string.registered)

                }
            } else if (selectedCourseTerm == "2" && term2 < 3) {
                if (courseRegisteredId.contains(selectedCourseId)) {
                    showAlertBox(getString(R.string.already_registered_message))
                } else {
                    addRegisteredCourse(studentId)
                    Toast.makeText(requireActivity(),
                        getString(R.string.success_register_message),
                        Toast.LENGTH_SHORT).show()
                    binding.tvDetailsCourseStatus.text = getString(R.string.registered)
                }
            } else {
                showAlertBox(getString(R.string.term_full_message,selectedCourseTerm))
            }
        } else if (courseRegisteredId.contains(selectedCoursePrerequisite) && selectedCourseTerm != termReq) {
            if (selectedCourseTerm == "1" && term1 < 3) {
                if (courseRegisteredId.contains(selectedCourseId)) {
                    showAlertBox(getString(R.string.already_registered_message))
                } else {
                    addRegisteredCourse(studentId)
                    Toast.makeText(requireActivity(),
                        getString(R.string.success_register_message),
                        Toast.LENGTH_SHORT).show()
                    binding.tvDetailsCourseStatus.text = getString(R.string.registered)
                }
            } else if (selectedCourseTerm == "2" && term2 < 3) {
                if (courseRegisteredId.contains(selectedCourseId)) {
                    showAlertBox(getString(R.string.already_registered_message))
                } else {
                    addRegisteredCourse(studentId)
                    Toast.makeText(requireActivity(),
                        getString(R.string.success_register_message),
                        Toast.LENGTH_SHORT).show()
                    binding.tvDetailsCourseStatus.text = getString(R.string.registered)
                }
            } else {
                showAlertBox(getString(R.string.term_full_message, selectedCourseTerm))
            }
        } else {
            showAlertBox(getString(R.string.prerequisite_not_satisfied_message))
        }
    }

    private fun showAlertBox(message: String){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Alert")
        builder.setMessage(message)
        builder.setNeutralButton("OK") { dialog2, _ -> dialog2.cancel() }
        builder.create().show()
    }

    private fun addRegisteredCourse(studentId: String) {
        viewModel.addRegisteredCourse(
            studentId,
            binding.tvDetailsCourseId.text.toString(),
            binding.tvDetailsCourseName.text.toString(),
            binding.tvDetailsCourseTerm.text.toString(),
            binding.tvDetailsCoursePrerequisite.text.toString()
        )
        val action =
            CourseDetailFragmentDirections.actionCourseDetailFragmentToRegisteredCourseFragment(
                studentId = studentId)
        view?.findNavController()?.navigate(action)
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
                    CourseDetailFragmentDirections.actionCourseDetailFragmentToUserLoginFragment()
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
        _binding = null
    }


}
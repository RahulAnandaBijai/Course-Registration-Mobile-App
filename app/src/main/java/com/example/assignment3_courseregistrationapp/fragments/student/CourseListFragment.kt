package com.example.assignment3_courseregistrationapp.fragments.student

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.adapter.CourseListAdapter
import com.example.assignment3_courseregistrationapp.databinding.FragmentCourseListBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class CourseListFragment : Fragment() {

    private val navigationArgs: CourseListFragmentArgs by navArgs()

    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseViewModel by activityViewModels {
        CourseViewModelFactory(
            (activity?.application as CourseRegistrationApplication).database.courseDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentId = navigationArgs.studentId

        val courseListAdapter = CourseListAdapter {
            val action =
                CourseListFragmentDirections.actionCourseListFragmentToCourseDetailFragment(courseId = it.courseId,
                    courseTerm = it.courseTerm,
                    studentId = studentId)
            view.findNavController().navigate(action)
        }

        binding.rvCourseList.layoutManager = LinearLayoutManager(this.context)

        binding.rvCourseList.adapter = courseListAdapter


        viewModel.allCourses.observe(this.viewLifecycleOwner) { courses ->
            courses.let {
                courseListAdapter.submitList(it)
            }
        }

        binding.btViewRegisteredCourses.setOnClickListener {
            val action =
                CourseListFragmentDirections.actionCourseListFragmentToRegisteredCourseFragment(
                    studentId = studentId)
            view.findNavController().navigate(action)
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
                    CourseListFragmentDirections.actionCourseListFragmentToUserLoginFragment()
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
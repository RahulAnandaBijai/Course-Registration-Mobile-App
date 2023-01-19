package com.example.assignment3_courseregistrationapp.fragments.admin

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.adapter.CourseListAdapter
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminCourseListBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class AdminCourseListFragment : Fragment() {

    private var _binding: FragmentAdminCourseListBinding? = null
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
        _binding = FragmentAdminCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val courseListAdapter = CourseListAdapter {
            val action =
                AdminCourseListFragmentDirections.actionAdminCourseListFragmentToAdminCourseDetailFragment(
                    courseId = it.courseId,
                    courseTerm = it.courseTerm)
            view.findNavController().navigate(action)
        }

        binding.rvAdminCourseList.layoutManager = LinearLayoutManager(this.context)

        binding.rvAdminCourseList.adapter = courseListAdapter


        viewModel.allCourses.observe(this.viewLifecycleOwner) { courses ->
            courses.let {
                courseListAdapter.submitList(it)
            }
        }

        binding.btViewAllUserRegisteredCourse.setOnClickListener {
            val action =
                AdminCourseListFragmentDirections.actionAdminCourseListFragmentToAdminAllUserRegisteredFragment()
            this.findNavController().navigate(action)
        }

        binding.floatingActionButton.setOnClickListener {
            val action =
                AdminCourseListFragmentDirections.actionAdminCourseListFragmentToAdminCourseAddFragment(
                    getString(R.string.add_fragment_title), courseId = "None", courseTerm = "None"
                )
            this.findNavController().navigate(action)
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
                    AdminCourseListFragmentDirections.actionAdminCourseListFragmentToUserLoginFragment()
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
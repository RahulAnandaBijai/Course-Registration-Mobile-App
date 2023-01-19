package com.example.assignment3_courseregistrationapp.fragments.admin

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
import com.example.assignment3_courseregistrationapp.adapter.AllUserRegisteredCourseListAdapter
import com.example.assignment3_courseregistrationapp.adapter.CourseListAdapter
import com.example.assignment3_courseregistrationapp.databinding.FragmentAdminAllUserRegisteredBinding
import com.example.assignment3_courseregistrationapp.databinding.FragmentCourseListBinding
import com.example.assignment3_courseregistrationapp.fragments.student.CourseListFragmentArgs
import com.example.assignment3_courseregistrationapp.fragments.student.CourseListFragmentDirections
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class AdminAllUserRegisteredFragment : Fragment() {


    private var _binding: FragmentAdminAllUserRegisteredBinding? = null
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
        _binding = FragmentAdminAllUserRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val allUserRegisteredCourseListAdapter = AllUserRegisteredCourseListAdapter {}

        binding.rvAllUserRegisteredList.layoutManager = LinearLayoutManager(this.context)

        binding.rvAllUserRegisteredList.adapter = allUserRegisteredCourseListAdapter


        viewModel.allUserRegisteredCourses.observe(this.viewLifecycleOwner) { allRegisteredcourses ->
            allRegisteredcourses.let {
                allUserRegisteredCourseListAdapter.submitList(it)
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
                    AdminAllUserRegisteredFragmentDirections.actionAdminAllUserRegisteredFragmentToUserLoginFragment()
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
package com.example.assignment3_courseregistrationapp.fragments.student

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3_courseregistrationapp.CourseRegistrationApplication
import com.example.assignment3_courseregistrationapp.R
import com.example.assignment3_courseregistrationapp.adapter.RecyclerViewClickListener
import com.example.assignment3_courseregistrationapp.adapter.RegisteredCourseListAdapter
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.databinding.FragmentCourseDetailBinding
import com.example.assignment3_courseregistrationapp.databinding.FragmentRegisteredCourseBinding
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModel
import com.example.assignment3_courseregistrationapp.viewmodel.CourseViewModelFactory


class RegisteredCourseFragment : Fragment(), RecyclerViewClickListener {

    private val navigationArgs: RegisteredCourseFragmentArgs by navArgs()
    lateinit var registeredCourse: RegisteredCourse


    private var _binding: FragmentRegisteredCourseBinding? = null
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
        _binding = FragmentRegisteredCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentId = navigationArgs.studentId

        binding.btGoHome.setOnClickListener {
            val action =
                RegisteredCourseFragmentDirections.actionRegisteredCourseFragmentToCourseListFragment(
                    studentId = studentId)
            view.findNavController().navigate(action)
        }

        val registeredCourseListAdapter = RegisteredCourseListAdapter(this)

        binding.rvRegisteredList.layoutManager = LinearLayoutManager(this.context)

        binding.rvRegisteredList.adapter = registeredCourseListAdapter


        viewModel.getRegisteredCourse(studentId)
            .observe(this.viewLifecycleOwner) { registeredCourse ->
                registeredCourse.let {
                    registeredCourseListAdapter.submitList(it)
                }
            }

    }

    override fun onClickName(position: Int) {
        validateCourseToUnregister(position)
    }

    private fun validateCourseToUnregister(position: Int) {
        val studentId = navigationArgs.studentId
        val registeredCourseList = viewModel.getRegisteredCourseList(studentId)
        val completedCourseList = viewModel.getCompletedCourseList(studentId)
        val mandatoryCourseList = viewModel.getMandatoryCourseList()
        val courseRegisteredId = registeredCourseList.map { it.courseId }
        val courseRegisteredTerm = registeredCourseList.map { it.courseTerm }
        val courseRegisteredPrerequisite = registeredCourseList.map { it.coursePrerequisite }
        val courseCompletedId = completedCourseList.map { it.courseId }
        val mandatoryCourseId = mandatoryCourseList.map { it.courseId }
        val selectedRegisteredCourseId = courseRegisteredId[position]
        val selectedRegisteredCourseTerm = courseRegisteredTerm[position]


        if (mandatoryCourseId.contains(selectedRegisteredCourseId)) {
            val builder2 = AlertDialog.Builder(requireActivity())
            builder2.setTitle("Alert")
            builder2.setMessage("$selectedRegisteredCourseId is a Mandatory Course and Cannot Be Removed")
            builder2.setNeutralButton("OK") { dialog2, _ -> dialog2.cancel() }
            builder2.create().show()
        } else if (courseCompletedId.contains(selectedRegisteredCourseId)) {
            Toast.makeText(
                requireActivity(),
                "You have already completed the course. Hence No Action Performed",
                Toast.LENGTH_SHORT
            ).show()
        } else if (courseRegisteredId.contains(selectedRegisteredCourseId)) {
            if (courseRegisteredPrerequisite.contains(selectedRegisteredCourseId)) {
                val posPre = courseRegisteredPrerequisite.indexOf(selectedRegisteredCourseId)
                val toRemovePreId = registeredCourseList[posPre]
                val crId = courseRegisteredId[posPre]
                val toRemoveCrId = registeredCourseList[position]

                val builder2 = AlertDialog.Builder(requireActivity())
                builder2.setTitle("Please Confirm")
                builder2.setMessage("$selectedRegisteredCourseId is a prerequisite of $crId. Un-Registering Course $selectedRegisteredCourseId would also Un-Register Course $crId. Do You Still want to proceed")
                builder2.setPositiveButton("Yes") { dialog2, id ->
                    viewModel.deleteRegisteredCourse(toRemovePreId)
                    viewModel.deleteRegisteredCourse(toRemoveCrId)
                    Toast.makeText(
                        requireActivity(),
                        "$selectedRegisteredCourseId has been successfully been un-registered and hence $crId has been un-registered",
                        Toast.LENGTH_LONG
                    ).show()
                    dialog2.cancel()
                }
                builder2.setNegativeButton("No", { dialog2, id -> dialog2.cancel() })
                builder2.create().show()

            } else {
                if (selectedRegisteredCourseTerm.equals(courseRegisteredTerm[courseRegisteredId.indexOf(
                        selectedRegisteredCourseId)])
                ) {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setTitle("Please Confirm")
                    builder.setMessage("Do You Want To Un-Register For Course $selectedRegisteredCourseId")
                    builder.setPositiveButton("Yes") { dialog, id ->
                        val toRemoveCrId = registeredCourseList[position]
                        //val posId = shareViewModel.courseRegisteredId.indexOf(selectedRegisteredCourseId)
                        viewModel.deleteRegisteredCourse(toRemoveCrId)

                        Toast.makeText(
                            requireActivity(),
                            "Course have been successfully been un-registered",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.cancel()
                    }
                    builder.setNegativeButton("No", { dialog, id -> dialog.cancel() })
                    builder.create().show()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Course is currently not Registered. Hence No Action Performed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        } else {
            Toast.makeText(
                requireActivity(),
                "Course is currently not Registered. Hence No Action Performed",
                Toast.LENGTH_SHORT
            ).show()
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
                    RegisteredCourseFragmentDirections.actionRegisteredCourseFragmentToUserLoginFragment()
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
package com.example.assignment3_courseregistrationapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.databinding.RegisterViewBinding

class RegisteredCourseListAdapter(
    private val recyclerViewClickListener: RecyclerViewClickListener
) : androidx.recyclerview.widget.ListAdapter<RegisteredCourse, RegisteredCourseListAdapter.RegisteredCourseViewHolder>(
    RegisteredCourseListAdapter.DiffCallback){

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RegisteredCourse>() {
            override fun areItemsTheSame(oldItem: RegisteredCourse, newItem: RegisteredCourse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RegisteredCourse, newItem: RegisteredCourse): Boolean {
                return oldItem == newItem
            }
        }
    }



    class RegisteredCourseViewHolder(
        private var binding: RegisterViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(registeredCourse: RegisteredCourse, recyclerViewClickListener: RecyclerViewClickListener) {
            binding.tvRegisteredCourseName.text = registeredCourse.courseName
            binding.tvRegisteredCourseId.text= registeredCourse.courseId
            binding.tvRegisteredCourseTerm.text = registeredCourse.courseTerm

            binding.ivRemove.setOnClickListener {

                recyclerViewClickListener.onClickName(position)

            }

        }



    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredCourseViewHolder {
        val viewHolder = RegisteredCourseViewHolder(
            RegisterViewBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )



        return viewHolder
    }


    override fun onBindViewHolder(holder: RegisteredCourseViewHolder, position: Int) {
        holder.bind(getItem(position),recyclerViewClickListener)

    }
}
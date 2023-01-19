package com.example.assignment3_courseregistrationapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.databinding.AlluserRegisterViewBinding

class AllUserRegisteredCourseListAdapter (
    private  val onItemClicked: (RegisteredCourse) -> Unit
) : androidx.recyclerview.widget.ListAdapter<RegisteredCourse, AllUserRegisteredCourseListAdapter.AllUserRegisteredCourseViewHolder>(
    DiffCallback) {

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



    class AllUserRegisteredCourseViewHolder(
        private var binding: AlluserRegisterViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(registeredCourse: RegisteredCourse) {
            binding.tvAllUserRegisteredCourseName.text = registeredCourse.courseName
            binding.tvAllUserRegisteredCourseId.text= registeredCourse.courseId
            binding.tvAllUserRegisteredCourseTerm.text = registeredCourse.courseTerm
            binding.tvAllUserId.text = registeredCourse.userId
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserRegisteredCourseViewHolder {
        val viewHolder = AllUserRegisteredCourseViewHolder(
            AlluserRegisterViewBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: AllUserRegisteredCourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
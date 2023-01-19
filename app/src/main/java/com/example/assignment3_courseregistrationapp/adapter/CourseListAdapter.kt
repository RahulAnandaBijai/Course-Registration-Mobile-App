package com.example.assignment3_courseregistrationapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.databinding.CourseViewBinding


class CourseListAdapter (
    private  val onItemClicked: (Course) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Course, CourseListAdapter.CourseViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.courseId == newItem.courseId
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }



    class CourseViewHolder(
        private var binding: CourseViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course) {
            binding.btCourses.text = course.courseName
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val viewHolder = CourseViewHolder(
            CourseViewBinding.inflate(
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


    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
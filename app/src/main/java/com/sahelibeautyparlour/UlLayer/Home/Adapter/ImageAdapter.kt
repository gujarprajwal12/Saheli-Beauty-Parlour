package com.sahelibeautyparlour.UlLayer.Home.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sahelibeautyparlour.UlLayer.Home.Dataclass.DriveFile
import com.sahelibeautyparlour.R
import com.sahelibeautyparlour.UlLayer.FullScrren.FullScreenActivity



class ImageAdapter(private val images: MutableList<DriveFile>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        Glide.with(holder.itemView.context)
            .load(image.webContentLink)
            .error(R.drawable.ic_launcher_background)
            .timeout(30000)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FullScreenActivity::class.java)
            intent.putExtra("image_url", image.webContentLink)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = images.size

    // Update existing data
    fun updateData(newImages: List<DriveFile>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }

    // Add more data to the existing list
    fun addMoreData(newImages: List<DriveFile>) {
        val startPosition = images.size
        images.addAll(newImages)
        notifyItemRangeInserted(startPosition, newImages.size)
    }
}


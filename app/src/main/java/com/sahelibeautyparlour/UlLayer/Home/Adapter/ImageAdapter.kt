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



class ImageAdapter(private val images: List<DriveFile>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

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
            .into(holder.imageView)

        // Set an onClickListener to pass the image URL to the new activity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FullScreenActivity::class.java)
            intent.putExtra("image_url", image.webContentLink) // Pass the image URL
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = images.size
}

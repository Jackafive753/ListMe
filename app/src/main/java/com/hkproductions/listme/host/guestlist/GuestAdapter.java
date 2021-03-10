package com.hkproductions.listme.host.guestlist;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostItemGuestlistBinding;
import com.hkproductions.listme.host.database.HostData;
import com.hkproductions.listme.host.guestdetailview.GuestDetailViewFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GuestAdapter extends ListAdapter<HostData, GuestAdapter.GuestListViewHolder> {

    protected GuestAdapter() {
        super(new DiffUtil.ItemCallback<HostData>() {
            @Override
            public boolean areItemsTheSame(@NonNull HostData oldItem, @NonNull HostData newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull HostData oldItem, @NonNull HostData newItem) {
                return oldItem.getHostDataId() == newItem.getHostDataId();
            }
        });
    }

    @NonNull
    @Override
    public GuestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return GuestListViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestListViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public static class GuestListViewHolder extends RecyclerView.ViewHolder{
        private HostItemGuestlistBinding binding;
        public GuestListViewHolder(HostItemGuestlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        static GuestListViewHolder from(ViewGroup parent){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            HostItemGuestlistBinding binding;
            binding = HostItemGuestlistBinding.inflate(layoutInflater, parent, false);
            return new GuestListViewHolder(binding);
        }

        /**
         * bind Method
         * @param data
         * Method to set the Text of the Button Item according to the data of the Guests
         */
        public void bind(HostData data){
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
            String checkInString = df.format(data.getStartTimeMilli());
            DateFormat dfEnd = new SimpleDateFormat("dd/MM/yy HH:mm");
            String checkOutString = dfEnd.format(data.getEndTimeMilli());
            binding.buttonGuest.setText(data.getFirstName() + data.getLastName() + "\n" + checkInString + " - " + checkOutString);
            binding.buttonGuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(GuestListFragmentDirections.actionShowGuestDetails(data.getHostDataId()));
                }
            });
        }
    }
}

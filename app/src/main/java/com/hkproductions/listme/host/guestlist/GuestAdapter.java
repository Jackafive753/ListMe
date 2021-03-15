package com.hkproductions.listme.host.guestlist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hkproductions.listme.R;
import com.hkproductions.listme.databinding.HostItemGuestlistBinding;
import com.hkproductions.listme.host.database.HostData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


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


    public static class GuestListViewHolder extends RecyclerView.ViewHolder {
        private final HostItemGuestlistBinding binding;
        private final ViewGroup parent;

        public GuestListViewHolder(HostItemGuestlistBinding binding, ViewGroup parent) {
            super(binding.getRoot());
            this.binding = binding;
            this.parent = parent;
        }

        static GuestListViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            HostItemGuestlistBinding binding;
            binding = HostItemGuestlistBinding.inflate(layoutInflater, parent, false);
            return new GuestListViewHolder(binding, parent);
        }

        /**
         * bind Method
         *
         * @param data Method to set the Text of the Button Item according to the data of the Guests
         */
        @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
        public void bind(HostData data) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String checkInString = df.format(data.getStartTimeMilli());
            DateFormat dfEnd = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String checkOutString;
            if (data.getEndTimeMilli() == -1) {
                checkOutString = parent.getResources().getString(R.string.no_endtime_text);
            } else {
                checkOutString = dfEnd.format(data.getEndTimeMilli());
            }
            binding.buttonGuest.setText(data.getFirstName() + "" + data.getLastName() + "\n" + checkInString + " -\n" + checkOutString);
            binding.buttonGuest.setOnClickListener(view -> Navigation.findNavController(view).navigate(GuestListFragmentDirections.actionShowGuestDetails(data.getHostDataId())));
        }
    }
}
